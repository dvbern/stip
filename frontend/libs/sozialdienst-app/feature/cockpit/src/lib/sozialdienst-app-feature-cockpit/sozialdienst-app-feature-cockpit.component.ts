import { A11yModule } from '@angular/cdk/a11y';
import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  QueryList,
  ViewChildren,
  booleanAttribute,
  computed,
  effect,
  inject,
  input,
  viewChild,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { NonNullableFormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {
  MatPaginator,
  MatPaginatorModule,
  PageEvent,
} from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import { MatSidenav, MatSidenavModule } from '@angular/material/sidenav';
import { MatSort, MatSortModule, Sort } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Router, RouterModule } from '@angular/router';
import { TranslatePipe, isDefined } from '@ngx-translate/core';
import {
  differenceInCalendarMonths,
  differenceInCalendarYears,
  differenceInDays,
  format,
} from 'date-fns';
import { debounceTime, distinctUntilChanged } from 'rxjs';

import { PermissionStore } from '@dv/shared/global/permission';
import { SozialdienstBenutzerRole } from '@dv/shared/model/benutzer';
import {
  DelegierenServiceGetDelegierungSozRequestParams,
  FallWithDelegierung,
  GetDelegierungSozQueryType,
  SortOrder,
  SozDashboardColumn,
} from '@dv/shared/model/gesuch';
import {
  DEFAULT_PAGE_SIZE,
  INPUT_DELAY,
  PAGE_SIZES,
} from '@dv/shared/model/ui-constants';
import { SharedPatternAppHeaderComponent } from '@dv/shared/pattern/app-header';
import { SharedPatternMobileSidenavComponent } from '@dv/shared/pattern/mobile-sidenav';
import { SharedUiClearButtonComponent } from '@dv/shared/ui/clear-button';
import {
  SharedUiFocusableListDirective,
  SharedUiFocusableListItemDirective,
} from '@dv/shared/ui/focusable-list';
import { SharedUiHasRolesDirective } from '@dv/shared/ui/has-roles';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import {
  TypeSafeMatCellDefDirective,
  TypeSafeMatRowDefDirective,
} from '@dv/shared/ui/table-helper';
import { SharedUiTruncateTooltipDirective } from '@dv/shared/ui/truncate-tooltip';
import { SharedUiVersionTextComponent } from '@dv/shared/ui/version-text';
import { getDiffFormat } from '@dv/shared/util/validator-date';
import { restrictNumberParam } from '@dv/shared/util-fn/filter-util';
import { DelegationStore } from '@dv/sozialdienst-app/data-access/delegation';
import {
  LetzteAktivitaetFromToKeys,
  SozCockitComponentInputs,
  SozCockpitFilterFormKeys,
} from '@dv/sozialdienst-app/model/delegation';

const DEFAULT_FILTER: GetDelegierungSozQueryType = 'ALLE_BEARBEITBAR_MEINE';

const createQuery = <
  T extends Partial<DelegierenServiceGetDelegierungSozRequestParams>,
>(
  value: T,
) => {
  return value;
};

@Component({
  selector: 'dv-sozialdienst-app-feature-cockpit',
  standalone: true,
  imports: [
    A11yModule,
    CommonModule,
    TranslatePipe,
    MatSidenavModule,
    MatTableModule,
    MatSortModule,
    // MatSlideToggleModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatSelectModule,
    MatTooltipModule,
    // MatRadioModule,
    ReactiveFormsModule,
    RouterModule,
    MatPaginatorModule,
    SharedPatternMobileSidenavComponent,
    SharedPatternAppHeaderComponent,
    SharedUiHasRolesDirective,
    SharedUiIconChipComponent,
    SharedUiFocusableListItemDirective,
    SharedUiFocusableListDirective,
    SharedUiLoadingComponent,
    SharedUiVersionTextComponent,
    SharedUiMaxLengthDirective,
    SharedUiClearButtonComponent,
    SharedUiTruncateTooltipDirective,
    TypeSafeMatCellDefDirective,
    TypeSafeMatRowDefDirective,
  ],
  templateUrl: './sozialdienst-app-feature-cockpit.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SozialdienstAppFeatureCockpitComponent
  implements SozCockitComponentInputs, OnInit
{
  private sidenavSig = viewChild.required(MatSidenav);
  private formBuilder = inject(NonNullableFormBuilder);
  private permissionStore = inject(PermissionStore);
  private router = inject(Router);
  delegationStore = inject(DelegationStore);

  closeMenuSig = input<{ value: boolean } | null>(null, { alias: 'closeMenu' });
  // Due to lack of space, the following inputs are not suffixed with 'Sig'
  show = input<GetDelegierungSozQueryType | undefined>(undefined);
  sortColumn = input<SozDashboardColumn | undefined>(undefined);
  sortOrder = input<SortOrder | undefined>(undefined);
  fallNummer = input<string | undefined>(undefined);
  nachname = input<string | undefined>(undefined);
  vorname = input<string | undefined>(undefined);
  geburtsdatum = input<string | undefined>(undefined);
  ort = input<string | undefined>(undefined);
  letzteAktivitaetFrom = input<string | undefined>(undefined);
  letzteAktivitaetTo = input<string | undefined>(undefined);
  page = input<number | undefined, string | undefined>(undefined, {
    transform: restrictNumberParam({ min: 0, max: 999 }),
  });
  pageSize = input<number | undefined, string | undefined>(undefined, {
    transform: restrictNumberParam({
      min: PAGE_SIZES[0],
      max: PAGE_SIZES[PAGE_SIZES.length - 1],
    }),
  });
  delegierungAngenommen = input<boolean | undefined, string | undefined>(
    undefined,
    {
      transform: booleanAttribute,
    },
  );

  @ViewChildren(SharedUiFocusableListItemDirective)
  items?: QueryList<SharedUiFocusableListItemDirective>;
  displayedColumns = Object.keys(SozDashboardColumn);

  filterForm = this.formBuilder.group({
    fallNummer: [<string | undefined>undefined],
    nachname: [<string | undefined>undefined],
    vorname: [<string | undefined>undefined],
    geburtsdatum: [<Date | undefined>undefined],
    ort: [<string | undefined>undefined],
    delegierungAngenommen: [<boolean | undefined>undefined],
  } satisfies Record<SozCockpitFilterFormKeys, unknown>);

  filterFromToForm = this.formBuilder.group({
    letzteAktivitaetFrom: [<Date | undefined>undefined],
    letzteAktivitaetTo: [<Date | undefined>undefined],
  } satisfies Record<LetzteAktivitaetFromToKeys, unknown>);

  quickFilterForm = this.formBuilder.group({
    query: [<GetDelegierungSozQueryType | undefined>undefined],
  });

  pageSizes = PAGE_SIZES;
  defaultPageSize = DEFAULT_PAGE_SIZE;
  sortSig = viewChild.required(MatSort);
  paginatorSig = viewChild.required(MatPaginator);
  showViewSig = computed<GetDelegierungSozQueryType>(() => {
    const show = this.show();
    return show ?? DEFAULT_FILTER;
  });

  dataSoruce = new MatTableDataSource<FallWithDelegierung>([]);

  quickFilters: {
    typ: GetDelegierungSozQueryType;
    icon: string;
    roles: SozialdienstBenutzerRole[];
  }[] = [
    {
      typ: 'ALLE_BEARBEITBAR_MEINE',
      icon: 'person',
      roles: ['V0_Sozialdienst-Mitarbeiter'],
    },
    {
      typ: 'ALLE',
      icon: 'all_inclusive',
      roles: ['V0_Sozialdienst-Mitarbeiter'],
    },
  ];

  private letzteAktivitaetFromChangedSig = toSignal(
    this.filterFromToForm.controls.letzteAktivitaetFrom.valueChanges,
  );
  private letzteAktivitaetToChangedSig = toSignal(
    this.filterFromToForm.controls.letzteAktivitaetTo.valueChanges,
  );
  letzteAktivitaetRangeSig = computed(() => {
    const start = this.letzteAktivitaetFromChangedSig();
    const end = this.letzteAktivitaetToChangedSig();

    if (!start || !end) {
      return '';
    }
    const difference = {
      days: differenceInDays(end, start),
      months: differenceInCalendarMonths(end, start),
      years: differenceInCalendarYears(end, start),
    };
    return difference.days
      ? [
          `${getDiffFormat(start, difference)}`,
          `${format(end, 'dd.MM.yy')}`,
        ].join(' - ')
      : format(start, 'dd.MM.yyyy');
  });
  availableQuickFiltersSig = computed(() => {
    const roles = this.permissionStore.rolesMapSig();

    return this.quickFilters.filter((filter) =>
      filter.roles.some((role) => roles?.[role]),
    );
  });

  // todo: is this even necessary?
  statusValuesSig = computed(() => {
    // const typ = this.typChangedSig();
    // if (!typ) {
    //   return null;
    // }

    // return {
    //   typ: typ === 'AENDERUNG' ? 'tranche' : 'contract',
    //   status: statusByTyp[typ],
    // };

    return {
      typ: 'bla',
      status: [
        {
          id: '1',
          name: 'Status 1',
        },
        {
          id: '2',
          name: 'Status 2',
        },
      ],
    };
  });

  filterFormChangedSig = toSignal(
    this.filterForm.valueChanges.pipe(debounceTime(INPUT_DELAY)),
  );
  filterStartEndFormChangedSig = toSignal(
    this.filterFromToForm.valueChanges.pipe(
      distinctUntilChanged(
        (a, b) =>
          // Only emit if both fields are defined or both are undefined
          // otherwise the list will update on first range picker interaction
          isDefined(b.letzteAktivitaetFrom) ===
            isDefined(b.letzteAktivitaetTo) && a === b,
      ),
      debounceTime(INPUT_DELAY),
    ),
  );

  // todo: implement
  faelleDataSourceSig = computed(() => {
    const dataSource = new MatTableDataSource<FallWithDelegierung>([]);
    return dataSource;
  });

  constructor() {
    effect(() => {
      if (this.closeMenuSig()?.value) {
        this.sidenavSig().close();
      }
    });
  }

  private getInputs() {}

  sortData(event: Sort) {}

  paginate(event: PageEvent) {
    this.router.navigate(['.'], {
      queryParams: createQuery({
        page: event.pageIndex,
        pageSize: event.pageSize,
      }),
      queryParamsHandling: 'merge',
      replaceUrl: true,
    });
  }

  ngOnInit() {}
}
