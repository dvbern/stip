import { A11yModule } from '@angular/cdk/a11y';
import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  InputSignal,
  OnInit,
  QueryList,
  ViewChildren,
  computed,
  effect,
  inject,
  input,
  signal,
  viewChild,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { NonNullableFormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatMenuModule } from '@angular/material/menu';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatRadioModule } from '@angular/material/radio';
import { MatSelectModule } from '@angular/material/select';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { TranslocoPipe, isDefined } from '@jsverse/transloco';
import { Store } from '@ngrx/store';
import {
  differenceInCalendarMonths,
  differenceInCalendarYears,
  differenceInDays,
  format,
} from 'date-fns';
import { debounceTime, distinctUntilChanged } from 'rxjs';

import { SachbearbeitungAppPatternOverviewLayoutComponent } from '@dv/sachbearbeitung-app/pattern/overview-layout';
import { selectVersion } from '@dv/shared/data-access/config';
import { DarlehenStore } from '@dv/shared/data-access/darlehen';
import { PermissionStore } from '@dv/shared/global/permission';
import { BenutzerRole } from '@dv/shared/model/benutzer';
import {
  DarlehenServiceGetFreiwilligDarlehenDashboardSbRequestParams,
  DarlehenStatus,
  FreiwilligDarlehenDashboard,
  GesuchTrancheTyp,
  GetFreiwilligDarlehenSbQueryType,
  SbFreiwilligDarlehenDashboardColumn,
  SortOrder,
} from '@dv/shared/model/gesuch';
import { SortAndPageInputs } from '@dv/shared/model/table';
import { AppendFromTo } from '@dv/shared/model/type-util';
import {
  DEFAULT_PAGE_SIZE,
  INPUT_DELAY,
  PAGE_SIZES,
} from '@dv/shared/model/ui-constants';
import { SharedUiClearButtonComponent } from '@dv/shared/ui/clear-button';
import { SharedUiFilterMenuButtonComponent } from '@dv/shared/ui/filter-menu-button';
import {
  SharedUiFocusableListDirective,
  SharedUiFocusableListItemDirective,
} from '@dv/shared/ui/focusable-list';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import {
  TypeSafeMatCellDefDirective,
  TypeSafeMatRowDefDirective,
} from '@dv/shared/ui/table-helper';
import { SharedUiTruncateTooltipDirective } from '@dv/shared/ui/truncate-tooltip';
import {
  getSortAndPageInputs,
  inverseSortMap,
  limitPageToNumberOfEntriesEffect,
  makeEmptyStringPropertiesNull,
  paginateList,
  partiallyDebounceFormValueChangesSig,
  restrictNumberParam,
  sortList,
} from '@dv/shared/util/table';
import {
  getDiffFormat,
  parseDate,
  toBackendLocalDate,
} from '@dv/shared/util/validator-date';

type DarlehenFilter = keyof typeof GetFreiwilligDarlehenSbQueryType;

const DEFAULT_FILTER: DarlehenFilter = 'MEINE_BEARBEITBAR';

type DashboardEntry = Omit<
  FreiwilligDarlehenDashboard,
  'id' | 'fallId' | 'relatedGesuchId'
>;
type DashboardEntryFields = keyof DashboardEntry;

/**
 * Special date fields which are treated as start-end fields only during filtering
 */
type StartEndFields = keyof Pick<DashboardEntry, 'letzteAktivitaet'>;
type DashboardFormSimpleFields = Exclude<DashboardEntryFields, StartEndFields>;
type DashboardFormStartEndFields = AppendFromTo<StartEndFields>;
type DashboardFormFields =
  | DashboardFormSimpleFields
  | DashboardFormStartEndFields;

type QuickFilterGroup = 'DARLEHEN';

type AvailableFilters = {
  group: QuickFilterGroup;
  filters: {
    typ: DarlehenFilter;
    roles: BenutzerRole[];
  }[];
}[];

@Component({
  selector: 'dv-sachbearbeitung-app-feature-darlehen-dashboard',
  imports: [
    A11yModule,
    CommonModule,
    TranslocoPipe,
    MatTableModule,
    MatSortModule,
    MatSlideToggleModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatSelectModule,
    MatTooltipModule,
    MatRadioModule,
    ReactiveFormsModule,
    RouterModule,
    MatMenuModule,
    MatPaginatorModule,
    SharedUiIconChipComponent,
    SharedUiFocusableListItemDirective,
    SharedUiFocusableListDirective,
    SharedUiLoadingComponent,
    SharedUiMaxLengthDirective,
    SharedUiTruncateTooltipDirective,
    TypeSafeMatCellDefDirective,
    TypeSafeMatRowDefDirective,
    SharedUiIconChipComponent,
    SharedUiClearButtonComponent,
    SachbearbeitungAppPatternOverviewLayoutComponent,
    SharedUiFilterMenuButtonComponent,
  ],
  templateUrl:
    './sachbearbeitung-app-feature-darlehen-dashboard.component.html',
  styleUrl: './sachbearbeitung-app-feature-darlehen-dashboard.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureDarlehenDashboardComponent
  implements
    OnInit,
    Record<DashboardFormFields, InputSignal<string | undefined>>,
    SortAndPageInputs<SbFreiwilligDarlehenDashboardColumn>
{
  private store = inject(Store);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private permissionStore = inject(PermissionStore);
  private formBuilder = inject(NonNullableFormBuilder);
  // Due to lack of space, the following inputs are not suffixed with 'Sig'
  show = input<DarlehenFilter | undefined>(undefined);
  fallNummer = input<string | undefined>(undefined);
  typ = input<string | undefined>(undefined);
  piaNachname = input<string | undefined>(undefined);
  piaVorname = input<string | undefined>(undefined);
  piaGeburtsdatum = input<string | undefined>(undefined);
  status = input<string | undefined>(undefined);
  bearbeiter = input<string | undefined>(undefined);
  letzteAktivitaetFrom = input<string | undefined>(undefined);
  letzteAktivitaetTo = input<string | undefined>(undefined);
  sortColumn = input<SbFreiwilligDarlehenDashboardColumn | undefined>(
    undefined,
  );
  sortOrder = input<SortOrder | undefined>(undefined);
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
  displayedColumns = Object.keys(SbFreiwilligDarlehenDashboardColumn);
  refreshQuickfilterSig = signal<unknown>(null);
  private defaultFilterSig = computed(() => {
    return DEFAULT_FILTER;
  });

  filterForm = this.formBuilder.group({
    fallNummer: [<string | undefined>undefined],
    piaNachname: [<string | undefined>undefined],
    piaVorname: [<string | undefined>undefined],
    piaGeburtsdatum: [<Date | undefined>undefined],
    status: [<DarlehenStatus | undefined>undefined],
    bearbeiter: [<string | undefined>undefined],
  } satisfies Record<DashboardFormSimpleFields, unknown>);
  filterStartEndForm = this.formBuilder.group({
    letzteAktivitaetFrom: [<Date | undefined>undefined],
    letzteAktivitaetTo: [<Date | undefined>undefined],
  } satisfies Record<DashboardFormStartEndFields, unknown>);
  quickFilterForm = this.formBuilder.group({
    query: [<DarlehenFilter | undefined>undefined],
  });
  pageSizes = PAGE_SIZES;
  defaultPageSize = DEFAULT_PAGE_SIZE;
  availableTypes = Object.values(GesuchTrancheTyp);
  versionSig = this.store.selectSignal(selectVersion);
  showViewSig = computed<DarlehenFilter>(() => {
    const show = this.show();
    return show ?? this.defaultFilterSig();
  });
  sortList = sortList(this.router, this.route);
  paginateList = paginateList(this.router, this.route);
  sortSig = viewChild.required(MatSort);
  paginatorSig = viewChild.required(MatPaginator);
  darlehenStore = inject(DarlehenStore);

  // Exhaustive quick filter configuration
  private readonly quickFilterConfig = {
    MEINE_DARLEHEN: {
      roles: ['V0_Sachbearbeiter', 'V0_Freigabestelle', 'V0_Jurist'],
      group: 'DARLEHEN',
    },
    ALLE_DARLEHEN: {
      roles: ['V0_Sachbearbeiter', 'V0_Freigabestelle', 'V0_Jurist'],
      group: 'DARLEHEN',
    },
    MEINE_BEARBEITBAR: {
      roles: ['V0_Sachbearbeiter', 'V0_Freigabestelle'],
      group: 'DARLEHEN',
    },
    ALLE_BEARBEITBAR: {
      roles: ['V0_Sachbearbeiter', 'V0_Freigabestelle'],
      group: 'DARLEHEN',
    },
  } satisfies Record<
    DarlehenFilter,
    { roles: BenutzerRole[]; group: QuickFilterGroup }
  >;
  // Signals and computed values for form changes and filtering
  private letzteAktivitaetFromChangedSig = toSignal(
    this.filterStartEndForm.controls.letzteAktivitaetFrom.valueChanges,
  );
  private letzteAktivitaetToChangedSig = toSignal(
    this.filterStartEndForm.controls.letzteAktivitaetTo.valueChanges,
  );
  availableQuickFiltersSig = computed<AvailableFilters>(() => {
    const activeRoles = this.permissionStore.rolesMapSig();
    return Object.entries(this.quickFilterConfig)
      .filter(([, { roles }]) => roles.some((r) => activeRoles?.[r]))
      .map(([typ, { roles }]) => ({
        typ: typ as DarlehenFilter,
        roles,
      }))
      .reduce((groups, filter) => {
        const group = this.quickFilterConfig[filter.typ].group;
        const existingGroup = groups.find((g) => g.group === group);
        if (existingGroup) {
          existingGroup.filters.push(filter);
        } else {
          groups.push({ group, filters: [filter] });
        }
        return groups;
      }, [] as AvailableFilters);
  });
  handleQuickFilterClick(filter: DarlehenFilter) {
    if (filter === this.quickFilterForm.controls.query.value) {
      // Refresh the quick filter even if the same filter is selected again
      this.refreshQuickfilterSig.set({});
    } else {
      this.quickFilterForm.controls.query.setValue(filter);
    }
  }
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
  statusValues = Object.values(DarlehenStatus);

  filterFormChangedSig = partiallyDebounceFormValueChangesSig(this.filterForm, [
    'status',
  ]);
  filterStartEndFormChangedSig = toSignal(
    this.filterStartEndForm.valueChanges.pipe(
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

  darlehenDataSourceSig = computed(() => {
    const darlehen = this.darlehenStore
      ?.dashboardViewSig()
      ?.darlehen?.entries?.map((entry) => {
        const status = entry.status;
        const translationKey = `sachbearbeitung-app.darlehen.status.${status}`;
        return {
          ...entry,
          translationKey,
        } satisfies FreiwilligDarlehenDashboard & {
          translationKey: string;
        };
      });
    const dataSource = new MatTableDataSource(darlehen);
    return dataSource;
  });
  totalEntriesSig = computed(() => {
    return this.darlehenStore.dashboardViewSig()?.darlehen?.totalEntries;
  });

  constructor() {
    limitPageToNumberOfEntriesEffect(
      this,
      this.totalEntriesSig,
      this.router,
      this.route,
    );
    // Handle normal filter form control changes
    effect(() => {
      this.filterFormChangedSig();
      const formValue = this.filterForm.getRawValue();
      const query = createQuery({
        ...formValue,
        piaGeburtsdatum: formValue.piaGeburtsdatum
          ? toBackendLocalDate(formValue.piaGeburtsdatum)
          : undefined,
      });
      this.router.navigate(['.'], {
        relativeTo: this.route,
        queryParams: makeEmptyStringPropertiesNull(query),
        queryParamsHandling: 'merge',
        replaceUrl: true,
      });
    });
    // Handle start-end filter form control changes seperately
    effect(() => {
      this.filterStartEndFormChangedSig();
      const formValue = this.filterStartEndForm.getRawValue();
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
        relativeTo: this.route,
        queryParams: makeEmptyStringPropertiesNull(query),
        queryParamsHandling: 'merge',
        replaceUrl: true,
      });
    });
    // Handle the quick filter form control changes (show / getGesucheSBQueryType)
    const quickFilterChanged = toSignal(
      this.quickFilterForm.controls.query.valueChanges,
    );
    effect(() => {
      const query = quickFilterChanged();
      if (!query) {
        return;
      }
      this.router.navigate(['.'], {
        relativeTo: this.route,
        queryParams: {
          show: query,
        },
        queryParamsHandling: 'merge',
        replaceUrl: true,
      });
    });
    // When the route param inputs change, load the gesuche
    effect(() => {
      this.refreshQuickfilterSig();
      const { query, filter, startEndFilter } = this.getInputs();
      this.darlehenStore.getDarlehenDashboardSb$({
        getFreiwilligDarlehenSbQueryType: query,
        ...filter,
        ...startEndFilter,
        ...getSortAndPageInputs(this),
      });
    });
  }
  private getInputs() {
    const query = this.showViewSig();
    const filter = {
      fallNummer: this.fallNummer(),
      piaNachname: this.piaNachname(),
      piaVorname: this.piaVorname(),
      piaGeburtsdatum: this.piaGeburtsdatum(),
      status: parseStatus(this.status()),
      bearbeiter: this.bearbeiter(),
    };
    const startEndFilter = {
      letzteAktivitaetFrom: this.letzteAktivitaetFrom(),
      letzteAktivitaetTo: this.letzteAktivitaetTo(),
    };
    return {
      query,
      filter,
      startEndFilter,
    };
  }
  resetStatus() {
    this.filterForm.controls.status.reset();
  }
  ngOnInit() {
    const { query, filter, startEndFilter } = this.getInputs();
    const sortOrder = this.sortOrder();
    const sortColumn = this.sortColumn();
    this.filterForm.reset({
      ...filter,
      piaGeburtsdatum: parseDate(filter.piaGeburtsdatum ?? ''),
    });
    this.filterStartEndForm.reset({
      ...startEndFilter,
      letzteAktivitaetFrom: parseDate(
        startEndFilter.letzteAktivitaetFrom ?? '',
      ),
      letzteAktivitaetTo: parseDate(startEndFilter.letzteAktivitaetTo ?? ''),
    });
    this.quickFilterForm.reset({ query });
    if (sortColumn && sortOrder) {
      this.sortSig().sort({
        id: sortColumn,
        start: inverseSortMap[sortOrder],
        disableClear: false,
      });
    }
    // Enable validation from the beginning
    this.filterForm.markAllAsTouched();
  }
}

const parseStatus = (
  status: string | undefined,
): DarlehenStatus | undefined => {
  if (!status || !Object.keys(DarlehenStatus).includes(status)) {
    return undefined;
  }
  return status as DarlehenStatus;
};

const createQuery = <
  T extends
    Partial<DarlehenServiceGetFreiwilligDarlehenDashboardSbRequestParams>,
>(
  value: T,
) => {
  return value;
};
