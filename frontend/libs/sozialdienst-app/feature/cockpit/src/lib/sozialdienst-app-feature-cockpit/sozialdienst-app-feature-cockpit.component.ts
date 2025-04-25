import { A11yModule } from '@angular/cdk/a11y';
import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  QueryList,
  ViewChildren,
  computed,
  effect,
  inject,
  input,
  signal,
  viewChild,
} from '@angular/core';
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
import { TranslatePipe } from '@ngx-translate/core';

import { PermissionStore } from '@dv/shared/global/permission';
import { SozialdienstBenutzerRole } from '@dv/shared/model/benutzer';
import {
  GesuchFilter,
  GesuchServiceGetGesucheSbRequestParams,
} from '@dv/shared/model/gesuch';
import { GesuchFormStep } from '@dv/shared/model/gesuch-form';
import { isDefined } from '@dv/shared/model/type-util';
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

// todo: move to shared
const PAGE_SIZES = [10, 20, 50];
const DEFAULT_PAGE_SIZE = 10;
const DEFAULT_FILTER: GesuchFilter = 'ALLE_BEARBEITBAR_MEINE';
const INPUT_DELAY = 600;

// todo: move to shared
const restrictNumberParam =
  (restriction: { max: number; min: number }) =>
  (value: number | undefined) => {
    if (!isDefined(value)) {
      return undefined;
    }
    if (+value > restriction.max) {
      return restriction.max;
    }
    return +value < restriction.min ? restriction.min : +value;
  };

// todo: move to shared and narrow down type
const createQuery = <T extends Partial<GesuchServiceGetGesucheSbRequestParams>>(
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
export class SozialdienstAppFeatureCockpitComponent {
  stepSig = signal<GesuchFormStep | undefined>(undefined);
  private sidenavSig = viewChild.required(MatSidenav);
  private formBuilder = inject(NonNullableFormBuilder);
  private permissionStore = inject(PermissionStore);

  closeMenuSig = input<{ value: boolean } | null>(null, { alias: 'closeMenu' });

  private router = inject(Router);

  page = input(<number | undefined>undefined, {
    transform: restrictNumberParam({ min: 0, max: 999 }),
  });
  pageSize = input(<number | undefined>undefined, {
    transform: restrictNumberParam({
      min: PAGE_SIZES[0],
      max: PAGE_SIZES[PAGE_SIZES.length - 1],
    }),
  });

  @ViewChildren(SharedUiFocusableListItemDirective)
  items?: QueryList<SharedUiFocusableListItemDirective>;
  displayedColumns = [
    'FALLNUMMER',
    'PIA_NACHNAME',
    'PIA_VORNAME',
    'PIA_GEBURTSDATUM',
    'PIA_WOHNORT',
    'STATUS',
    'LETZTE_AKTIVITAET',
  ];

  // todo: type annotation once available
  filterForm = this.formBuilder.group({
    fallNummer: [<string | undefined>undefined],
    piaNachname: [<string | undefined>undefined],
    piaVorname: [<string | undefined>undefined],
    piaGeburtsdatum: [<Date | undefined>undefined],
    piaWohnort: [<string | undefined>undefined],
    status: [<any | undefined>undefined],
  });

  // todo: type annotation once available
  filterStartEndForm = this.formBuilder.group({
    letzteAktivitaetFrom: [<Date | undefined>undefined],
    letzteAktivitaetTo: [<Date | undefined>undefined],
  });

  quickFilterForm = this.formBuilder.group({
    query: [<GesuchFilter | undefined>undefined],
  });

  pageSizes = PAGE_SIZES;
  defaultPageSize = DEFAULT_PAGE_SIZE;

  sortSig = viewChild.required(MatSort);
  paginatorSig = viewChild.required(MatPaginator);

  quickFilters: {
    typ: GesuchFilter;
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

  availableQuickFiltersSig = computed(() => {
    const roles = this.permissionStore.rolesMapSig();

    return this.quickFilters.filter((filter) =>
      filter.roles.some((role) => roles?.[role]),
    );
  });

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

  letzteAktivitaetRangeSig = computed(() => {
    // const start = this.letzteAktivitaetFromChangedSig();
    // const end = this.letzteAktivitaetToChangedSig();
    // if (!start || !end) {
    //   return '';
    // }
    // const difference = {
    //   days: differenceInDays(end, start),
    //   months: differenceInCalendarMonths(end, start),
    //   years: differenceInCalendarYears(end, start),
    // };
    // return difference.days
    //   ? [
    //       `${getDiffFormat(start, difference)}`,
    //       `${format(end, 'dd.MM.yy')}`,
    //     ].join(' - ')
    //   : format(start, 'dd.MM.yyyy');
  });

  faelleDataSourceSig = computed(() => {
    const dataSource = new MatTableDataSource<any>([]);
    return dataSource;
  });

  constructor() {
    effect(() => {
      if (this.closeMenuSig()?.value) {
        this.sidenavSig().close();
      }
    });
  }

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
}
