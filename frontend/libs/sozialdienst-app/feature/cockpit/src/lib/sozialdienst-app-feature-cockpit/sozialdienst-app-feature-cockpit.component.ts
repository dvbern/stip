import { A11yModule } from '@angular/cdk/a11y';
import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  OnInit,
  QueryList,
  ViewChildren,
  computed,
  effect,
  inject,
  input,
  viewChild,
} from '@angular/core';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { NonNullableFormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDialog } from '@angular/material/dialog';
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
import { Store } from '@ngrx/store';
import { TranslatePipe, isDefined } from '@ngx-translate/core';
import {
  differenceInCalendarMonths,
  differenceInCalendarYears,
  differenceInDays,
  format,
} from 'date-fns';
import { debounceTime, distinctUntilChanged } from 'rxjs';

import { selectVersion } from '@dv/shared/data-access/config';
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
import { provideDvDateAdapter } from '@dv/shared/util/date-adapter';
import { paginatorTranslationProvider } from '@dv/shared/util/paginator-translation';
import {
  getDiffFormat,
  parseDate,
  toBackendLocalDate,
} from '@dv/shared/util/validator-date';
import {
  inverseSortMap,
  makeEmptyStringPropertiesNull,
  restrictNumberParam,
} from '@dv/shared/util-fn/filter-util';
import { DelegationStore } from '@dv/sozialdienst-app/data-access/delegation';
import { DelegierungDialogComponent } from '@dv/sozialdienst-app/feature/delegierung-dialog';
import {
  LetzteAktivitaetFromToKeys,
  SozCockitComponentInputs,
  SozCockpitFilterFormKeys,
} from '@dv/sozialdienst-app/model/delegation';

const DEFAULT_FILTER: GetDelegierungSozQueryType = 'ALLE_BEARBEITBAR_MEINE';

// todo: remove this dummy data
const dummyFallWithDelegierung: FallWithDelegierung = {
  id: 'sdfsdf-sdfsfd-sdf-sdf-sdf',
  fallNummer: 'A123456',
  mandant: 'sdflkf-flj-lk',
  delegierung: {
    delegierungAngenommen: true,
    persoenlicheAngaben: {
      nachname: 'Mustermann',
      vorname: 'Max',
      geburtsdatum: '1990-01-01',
      adresse: {
        strasse: 'Musterstraße',
        hausnummer: '1',
        plz: '12345',
        ort: 'Musterstadt',
        land: 'CH',
      },
      anrede: 'HERR',
    },
  },
  letzteAktivitaet: new Date().toISOString(),
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
  providers: [provideDvDateAdapter(), paginatorTranslationProvider()],
})
export class SozialdienstAppFeatureCockpitComponent
  implements SozCockitComponentInputs, OnInit
{
  private sidenavSig = viewChild.required(MatSidenav);
  private formBuilder = inject(NonNullableFormBuilder);
  private permissionStore = inject(PermissionStore);
  private router = inject(Router);
  private store = inject(Store);
  private destroyRef = inject(DestroyRef);
  private dialog = inject(MatDialog);
  delegationStore = inject(DelegationStore);
  versionSig = this.store.selectSignal(selectVersion);

  closeMenuSig = input<{ value: boolean } | null>(null, { alias: 'closeMenu' });
  // Due to lack of space, the following inputs are not suffixed with 'Sig'
  show = input<GetDelegierungSozQueryType | undefined>(undefined);
  sortColumn = input<SozDashboardColumn | undefined>(undefined);
  sortOrder = input<SortOrder | undefined>(undefined);
  fallNummer = input<string | undefined>(undefined);
  nachname = input<string | undefined>(undefined);
  vorname = input<string | undefined>(undefined);
  geburtsdatum = input<string | undefined>(undefined);
  wohnort = input<string | undefined>(undefined);
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
      transform: booleanOrUndefined,
    },
  );

  @ViewChildren(SharedUiFocusableListItemDirective)
  items?: QueryList<SharedUiFocusableListItemDirective>;
  displayedColumns = [...Object.keys(SozDashboardColumn), 'AKTIONEN'];

  filterForm = this.formBuilder.group({
    fallNummer: [<string | undefined>undefined],
    nachname: [<string | undefined>undefined],
    vorname: [<string | undefined>undefined],
    geburtsdatum: [<Date | undefined>undefined],
    wohnort: [<string | undefined>undefined],
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

  // for delegierungAngenommen
  statusValues = [
    {
      key: 'DELEGIERUNG',
      value: true,
    },
    {
      key: 'DELEGIERUNG_ANFRAGE',
      value: false,
    },
  ];

  filterFormChangedSig = toSignal(
    this.filterForm.valueChanges.pipe(debounceTime(INPUT_DELAY)),
  );
  filterFromToFormChangedSig = toSignal(
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

  faelleDataSourceSig = computed(() => {
    const faelle = this.delegationStore.cockpitViewSig().paginatedSozDashboard
      ?.entries ?? [dummyFallWithDelegierung];

    const dataSource = new MatTableDataSource<FallWithDelegierung>(faelle);
    return dataSource;
  });

  constructor() {
    effect(() => {
      if (this.closeMenuSig()?.value) {
        this.sidenavSig().close();
      }
    });

    // Handle the case where the page is higher than the total number of pages
    effect(
      () => {
        const { page, pageSize } = this.getInputs();
        const totalEntries =
          this.delegationStore.cockpitViewSig().paginatedSozDashboard
            ?.totalEntries;

        if (
          page &&
          pageSize &&
          totalEntries &&
          page * pageSize > totalEntries
        ) {
          this.router.navigate(['.'], {
            queryParams: {
              page: Math.ceil(totalEntries / pageSize) - 1,
            },
            queryParamsHandling: 'merge',
            replaceUrl: true,
          });
        }
      },
      { allowSignalWrites: true },
    );

    // Handle string filter form control changes
    effect(
      () => {
        this.filterFormChangedSig();
        const formValue = this.filterForm.getRawValue();
        const query = createQuery({
          ...formValue,
          geburtsdatum: formValue.geburtsdatum
            ? toBackendLocalDate(formValue.geburtsdatum)
            : undefined,
        });

        this.router.navigate(['.'], {
          queryParams: makeEmptyStringPropertiesNull(query),
          queryParamsHandling: 'merge',
          replaceUrl: true,
        });
      },
      { allowSignalWrites: true },
    );

    // Handle from-to filter form control changes seperately
    effect(
      () => {
        this.filterFromToFormChangedSig();
        const formValue = this.filterFromToForm.getRawValue();
        const query = createQuery({
          letzteAktivitaetFrom:
            formValue.letzteAktivitaetTo && formValue.letzteAktivitaetFrom
              ? toBackendLocalDate(formValue.letzteAktivitaetFrom)
              : undefined,
          letzteAktivitaetTo:
            formValue.letzteAktivitaetFrom && formValue.letzteAktivitaetTo
              ? toBackendLocalDate(formValue.letzteAktivitaetTo)
              : undefined,
        });

        this.router.navigate(['.'], {
          queryParams: makeEmptyStringPropertiesNull(query),
          queryParamsHandling: 'merge',
          replaceUrl: true,
        });
      },
      {
        allowSignalWrites: true,
      },
    );

    // Handle the quick filter form control changes (show / getGesucheSBQueryType)
    const quickFilterChanged = toSignal(
      this.quickFilterForm.controls.query.valueChanges,
    );
    effect(
      () => {
        const query = quickFilterChanged();
        if (!query) {
          return;
        }
        this.router.navigate(['.'], {
          queryParams: {
            show: query === DEFAULT_FILTER ? undefined : query,
          },
          queryParamsHandling: 'merge',
          replaceUrl: true,
        });
      },
      { allowSignalWrites: true },
    );

    // when the route param inputs change, load the data
    effect(
      () => {
        const {
          query,
          filter,
          fromToFilter,
          sortColumn,
          sortOrder,
          page,
          pageSize,
        } = this.getInputs();

        this.delegationStore.loadPaginatedSozDashboard$({
          getDelegierungSozQueryType: query,
          ...filter,
          ...fromToFilter,
          sortColumn,
          sortOrder,
          page: page ?? 0,
          pageSize: pageSize ?? DEFAULT_PAGE_SIZE,
        });
      },
      { allowSignalWrites: true },
    );
  }

  openDialog(fall: FallWithDelegierung) {
    DelegierungDialogComponent.open(this.dialog, { fall })
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((result) => {});
  }

  private getInputs() {
    const query = this.showViewSig();
    const filter = {
      fallNummer: this.fallNummer(),
      nachname: this.nachname(),
      vorname: this.vorname(),
      geburtsdatum: this.geburtsdatum(),
      wohort: this.wohnort(),
      delegierungAngenommen: this.delegierungAngenommen(),
    };
    const fromToFilter = {
      letzteAktivitaetFrom: this.letzteAktivitaetFrom(),
      letzteAktivitaetTo: this.letzteAktivitaetFrom(),
    };
    const page = this.page();
    const pageSize = this.pageSize();
    const sortColumn = this.sortColumn();
    const sortOrder = this.sortOrder();

    return {
      query,
      filter,
      fromToFilter,
      page,
      pageSize,
      sortColumn,
      sortOrder,
    };
  }

  sortData(event: Sort) {
    this.router.navigate(['.'], {
      queryParams: createQuery({
        sortColumn: event.active as SozDashboardColumn,
        sortOrder: event.direction as SortOrder,
      }),
      queryParamsHandling: 'merge',
      replaceUrl: true,
    });
  }

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

  ngOnInit() {
    const { query, filter, fromToFilter, sortColumn, sortOrder } =
      this.getInputs();

    this.quickFilterForm.reset({ query });
    this.filterForm.reset({
      ...filter,
      delegierungAngenommen: filter.delegierungAngenommen,
      geburtsdatum: parseDate(filter.geburtsdatum ?? ''),
    });
    this.filterFromToForm.reset({
      ...fromToFilter,
      letzteAktivitaetFrom: parseDate(fromToFilter.letzteAktivitaetFrom),
      letzteAktivitaetTo: parseDate(fromToFilter.letzteAktivitaetTo),
    });

    if (sortColumn && sortOrder) {
      this.sortSig().sort({
        id: sortColumn,
        start: inverseSortMap[sortOrder],
        disableClear: false,
      });
    }

    this.filterForm.markAllAsTouched();
  }
}

const booleanOrUndefined = (value: string | undefined): boolean | undefined => {
  if (value === undefined) {
    return undefined;
  }
  return value === 'true' ? true : false;
};

const createQuery = <
  T extends Partial<DelegierenServiceGetDelegierungSozRequestParams>,
>(
  value: T,
) => {
  return value;
};
