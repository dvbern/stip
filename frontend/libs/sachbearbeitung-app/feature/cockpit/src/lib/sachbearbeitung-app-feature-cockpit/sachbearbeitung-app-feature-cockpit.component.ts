import { A11yModule } from '@angular/cdk/a11y';
import { CommonModule } from '@angular/common';
import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  Injector,
  InputSignal,
  QueryList,
  Signal,
  ViewChildren,
  computed,
  effect,
  inject,
  input,
  runInInjectionContext,
  untracked,
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
import { TranslocoDirective } from '@jsverse/transloco';
import { Store } from '@ngrx/store';
import {
  differenceInCalendarMonths,
  differenceInCalendarYears,
  differenceInDays,
  format,
} from 'date-fns';
import { debounceTime, distinctUntilChanged } from 'rxjs';

import { GesuchStore } from '@dv/sachbearbeitung-app/data-access/gesuch';
import { MassendruckStore } from '@dv/sachbearbeitung-app/data-access/massendruck';
import { selectVersion } from '@dv/shared/data-access/config';
import { DarlehenStore } from '@dv/shared/data-access/darlehen';
import { PermissionStore } from '@dv/shared/global/permission';
import {
  DarlehenStatus,
  FreiwilligDarlehenDashboard,
  GesuchServiceGetGesucheSbRequestParams,
  GesuchTrancheTyp,
  Gesuchstatus,
  SbFreiwilligDarlehenDashboardColumn,
  SbGesucheDashboardColumn,
  SortOrder,
} from '@dv/shared/model/gesuch';
import { SortAndPageInputs } from '@dv/shared/model/table';
import { isDefined } from '@dv/shared/model/type-util';
import {
  DEFAULT_PAGE_SIZE,
  INPUT_DELAY,
  PAGE_SIZES,
} from '@dv/shared/model/ui-constants';
import { SharedUiClearButtonComponent } from '@dv/shared/ui/clear-button';
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
import { SharedUiVersionTextComponent } from '@dv/shared/ui/version-text';
import {
  DashboardFormFields,
  DashboardFormSimpleFields,
  DashboardFormStartEndFields,
  DashboardQuery,
  DashboardTableEntryFields,
  FilterTabParam,
  ScopeParam,
  WorkableParam,
  gesucheStatusByTyp,
  getControlVisibility,
  getDefaultQueryForRole,
  getQueryFromParams,
  isDarlehenQuery,
  isGesuchQuery,
} from '@dv/shared/util/dashboard';
import { provideDvDateAdapter } from '@dv/shared/util/date-adapter';
import { paginatorTranslationProvider } from '@dv/shared/util/paginator-translation';
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

@Component({
  selector: 'dv-sachbearbeitung-app-feature-cockpit',
  imports: [
    A11yModule,
    CommonModule,
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
    SharedUiVersionTextComponent,
    SharedUiMaxLengthDirective,
    SharedUiTruncateTooltipDirective,
    TypeSafeMatCellDefDirective,
    TypeSafeMatRowDefDirective,
    SharedUiIconChipComponent,
    SharedUiClearButtonComponent,
    TranslocoDirective,
  ],
  templateUrl: './sachbearbeitung-app-feature-cockpit.component.html',
  styleUrls: ['./sachbearbeitung-app-feature-cockpit.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [provideDvDateAdapter(), paginatorTranslationProvider()],
})
export class SachbearbeitungAppFeatureCockpitComponent
  implements
    Record<DashboardFormFields, InputSignal<string | undefined>>,
    SortAndPageInputs<
      SbGesucheDashboardColumn | SbFreiwilligDarlehenDashboardColumn
    >,
    AfterViewInit
{
  private store = inject(Store);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private permissionStore = inject(PermissionStore);
  private formBuilder = inject(NonNullableFormBuilder);
  private injector = inject(Injector);
  massendruckStore = inject(MassendruckStore);
  gesuchStore = inject(GesuchStore);
  darlehenStore = inject(DarlehenStore);
  // Due to lack of space, the following inputs are not suffixed with 'Sig'
  filterTab = input<FilterTabParam | undefined>(undefined);
  scope = input<ScopeParam | undefined>(undefined);
  workable = input<WorkableParam | undefined>(undefined);
  fallNummer = input<string | undefined>(undefined);
  typ = input<string | undefined>(undefined);
  piaNachname = input<string | undefined>(undefined);
  piaVorname = input<string | undefined>(undefined);
  piaGeburtsdatum = input<string | undefined>(undefined);
  status = input<string | undefined>(undefined);
  bearbeiter = input<string | undefined>(undefined);
  letzteAktivitaetFrom = input<string | undefined>(undefined);
  letzteAktivitaetTo = input<string | undefined>(undefined);
  sortColumn = input<
    SbGesucheDashboardColumn | SbFreiwilligDarlehenDashboardColumn | undefined
  >(undefined);
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
  displayedGesucheColumns = Object.keys(SbGesucheDashboardColumn);
  displayedDarlehenColumns = Object.keys(SbFreiwilligDarlehenDashboardColumn);

  filterForm = this.formBuilder.group({
    fallNummer: [<string | undefined>undefined],
    typ: [<GesuchTrancheTyp | undefined>undefined],
    piaNachname: [<string | undefined>undefined],
    piaVorname: [<string | undefined>undefined],
    piaGeburtsdatum: [<Date | undefined>undefined],
    status: [<Gesuchstatus | undefined>undefined],
    bearbeiter: [<string | undefined>undefined],
  } satisfies Record<DashboardFormSimpleFields, unknown>);

  filterStartEndForm = this.formBuilder.group({
    letzteAktivitaetFrom: [<Date | undefined>undefined],
    letzteAktivitaetTo: [<Date | undefined>undefined],
  } satisfies Record<DashboardFormStartEndFields, unknown>);

  togglesGroup = this.formBuilder.group({
    scope: [<boolean | undefined>undefined],
    workable: [<boolean | undefined>undefined],
  });

  pageSizes = PAGE_SIZES;
  defaultPageSize = DEFAULT_PAGE_SIZE;
  availableTypes = Object.values(GesuchTrancheTyp);
  versionSig = this.store.selectSignal(selectVersion);

  defaultFilter = getDefaultQueryForRole(this.permissionStore.rolesMapSig());

  filterInputsSig = computed(() => {
    return {
      fallNummer: this.fallNummer(),
      typ: parseTyp(this.typ()) ?? 'TRANCHE',
      piaNachname: this.piaNachname(),
      piaVorname: this.piaVorname(),
      piaGeburtsdatum: this.piaGeburtsdatum(),
      status: parseStatus(this.status()),
      bearbeiter: this.bearbeiter(),
    };
  });

  startEndFilterInputsSig = computed(() => {
    return {
      letzteAktivitaetFrom: this.letzteAktivitaetFrom(),
      letzteAktivitaetTo: this.letzteAktivitaetTo(),
    };
  });

  queryFromInputsSig = computed<{
    query: DashboardQuery;
    scopeConfig: { show: boolean; value: boolean };
    workableConfig: { show: boolean; value: boolean };
  }>(() => {
    const scope = this.scope() ?? this.defaultFilter.scope;
    const workable = this.workable() ?? this.defaultFilter.workable;
    const filterTab = this.filterTab() ?? this.defaultFilter.filterTab;

    const query = getQueryFromParams(scope, workable, filterTab);

    const { scopeConfig, workableConfig } = getControlVisibility(
      scope,
      workable,
      filterTab,
    );

    return {
      query,
      scopeConfig,
      workableConfig,
    };
  });

  isDarlehenModeSig = computed(() => this.filterTab() === 'DARLEHEN');

  showScopeToggleSig = computed(() => {
    const { scopeConfig } = this.queryFromInputsSig();

    return scopeConfig.show;
  });

  showWorkableToggleSig = computed(() => {
    const { workableConfig } = this.queryFromInputsSig();

    return workableConfig.show;
  });

  sortList = sortList(this.router, this.route);
  paginateList = paginateList(this.router, this.route);
  sortSig = viewChild.required(MatSort);
  paginatorSig = viewChild.required(MatPaginator);

  // Signals and computed values for form changes and filtering
  private letzteAktivitaetFromChangedSig = toSignal(
    this.filterStartEndForm.controls.letzteAktivitaetFrom.valueChanges,
  );
  private letzteAktivitaetToChangedSig = toSignal(
    this.filterStartEndForm.controls.letzteAktivitaetTo.valueChanges,
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

  typChangedSig = toSignal(this.filterForm.controls.typ.valueChanges);
  statusValuesSig = computed(() => {
    const typ = this.typChangedSig();
    if (!typ) {
      return null;
    }

    return {
      typ: typ === 'AENDERUNG' ? 'tranche' : 'contract',
      status: gesucheStatusByTyp[typ],
    };
  });

  darlehenStatusValues = Object.values(DarlehenStatus);

  filterFormChangedSig = partiallyDebounceFormValueChangesSig(this.filterForm, [
    'status',
    'typ',
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

  gesucheDataSourceSig = computed(() => {
    const gesuche = this.gesuchStore
      ?.cockpitViewSig()
      ?.gesuche?.entries?.map((entry) => {
        const status =
          entry.typ == 'TRANCHE' ? entry.gesuchStatus : entry.trancheStatus;
        const translationKey = `sachbearbeitung-app.gesuch.status.${entry.typ == 'TRANCHE' ? 'contract' : 'tranche'}.${status}`;
        return {
          id: entry.id,
          trancheId: entry.gesuchTrancheId,
          fallNummer: entry.fallNummer,
          typ: entry.typ,
          piaNachname: entry.piaNachname,
          piaVorname: entry.piaVorname,
          piaGeburtsdatum: entry.piaGeburtsdatum,
          status,
          translationKey,
          bearbeiter: entry.bearbeiter,
          letzteAktivitaet: entry.letzteAktivitaet,
        } satisfies Record<DashboardTableEntryFields, unknown> & {
          id: string;
          trancheId: string;
          translationKey: string;
        };
      });
    const dataSource = new MatTableDataSource(gesuche);

    return dataSource;
  });

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

  gesucheTotalEntriesSig = computed(() => {
    return this.gesuchStore.cockpitViewSig()?.gesuche?.totalEntries;
  });

  darlehenTotalEntriesSig = computed(() => {
    return this.darlehenStore.dashboardViewSig()?.darlehen?.totalEntries;
  });

  totalEntriesSig = computed(() => {
    if (this.isDarlehenModeSig()) {
      return this.darlehenTotalEntriesSig();
    }
    return this.gesucheTotalEntriesSig();
  });

  loadingSig = computed(() => {
    if (this.isDarlehenModeSig()) {
      return this.darlehenStore.dashboardViewSig().loading;
    }
    return this.gesuchStore.cockpitViewSig().loading;
  });

  canCreateMassendruckSig: Signal<boolean> = computed(() => {
    const filterTab = this.filterTab();
    const isFilterTabDruck = filterTab?.includes('DRUCKBAR');
    this.filterFormChangedSig();

    if (!isFilterTabDruck) {
      return false;
    }

    const hasEntries = (this.totalEntriesSig() ?? 0) > 0;
    const hasFilters = Object.entries(this.filterForm.getRawValue())
      .filter(
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        ([key, _]) => {
          return key !== 'typ';
        },
      )
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      .some(([_, value]) => value);

    return hasEntries && !hasFilters;
  });

  createMassendruckJobForQueryType$() {
    const query = this.queryFromInputsSig().query;
    if (this.isDarlehenModeSig() || !isGesuchQuery(query)) {
      const message =
        'Invalid query type for Massendruck. Massendruck is only available for Gesuche with DRUCKBAR filter tab.';
      console.error(message);
      throw new Error(message);
    }
    this.massendruckStore.createMassendruckJobForQueryType$({
      req: { getGesucheSBQueryType: query },
      onSuccess: () => {
        this.router.navigate(['/massendruck']);
      },
    });
  }

  // ngAfterViewInit() {
  //   runInInjectionContext(this.injector, () => {
  //     effect(() => {
  //       this.filterTab();

  //       const sortOrder = untracked(this.sortOrder);
  //       const sortColumn = untracked(this.sortColumn);

  //       const sorter = untracked(this.sortSig);
  //       sorter.active = '';
  //       sorter.direction = '';

  //       sorter.sortChange.emit({ active: '', direction: '' });

  //       // if (sortColumn && sortOrder) {
  //       //   // sorter.sort({
  //       //   //   id: sortColumn,
  //       //   //   start: inverseSortMap[sortOrder],
  //       //   //   disableClear: false,
  //       //   // });
  //       //   sorter.active = sortColumn;
  //       //   sorter.direction = inverseSortMap[sortOrder];
  //       // } else {
  //       //   sorter.active = '';
  //       //   sorter.direction = '';
  //       // }
  //     });
  //   });
  // }

  constructor() {
    limitPageToNumberOfEntriesEffect(
      this,
      this.totalEntriesSig,
      this.router,
      this.route,
    );

    // effect to set form values on tab change and init of component
    effect(() => {
      this.filterTab();

      const { scopeConfig, workableConfig } = untracked(
        this.queryFromInputsSig,
      );
      const filter = untracked(this.filterInputsSig);
      const startEndFilter = untracked(this.startEndFilterInputsSig);

      this.togglesGroup.controls.scope.setValue(scopeConfig.value, {
        emitEvent: false,
      });
      this.togglesGroup.controls.workable.setValue(workableConfig.value, {
        emitEvent: false,
      });

      this.filterForm.patchValue(
        {
          ...filter,
          piaGeburtsdatum: parseDate(filter.piaGeburtsdatum ?? ''),
        },
        { emitEvent: false },
      );
      this.filterStartEndForm.patchValue(
        {
          ...startEndFilter,
          letzteAktivitaetFrom: parseDate(
            startEndFilter.letzteAktivitaetFrom ?? '',
          ),
          letzteAktivitaetTo: parseDate(
            startEndFilter.letzteAktivitaetTo ?? '',
          ),
        },
        { emitEvent: false },
      );
    });

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

    const scopeChangedSig = toSignal(
      this.togglesGroup.controls.scope.valueChanges,
    );
    const workableChangedSig = toSignal(
      this.togglesGroup.controls.workable.valueChanges,
    );

    // scope changed
    effect(() => {
      const scopeChanged = scopeChangedSig();

      if (scopeChanged === undefined) {
        return;
      }

      const scope = scopeChanged === true ? 'MEINE' : 'ALLE';

      this.router.navigate(['.'], {
        relativeTo: this.route,
        queryParams: {
          scope,
        },
        queryParamsHandling: 'merge',
      });
    });

    // workable changed
    effect(() => {
      const workableChanged = workableChangedSig();

      if (workableChanged === undefined) {
        return;
      }

      const workable = workableChanged === true ? 'TRUE' : 'FALSE';

      this.router.navigate(['.'], {
        relativeTo: this.route,
        queryParams: {
          workable,
        },
        queryParamsHandling: 'merge',
      });
    });

    // Load Gesuche effect
    effect(() => {
      const query = this.queryFromInputsSig().query;
      const filter = this.filterInputsSig();
      const startEndFilter = this.startEndFilterInputsSig();

      if (untracked(this.isDarlehenModeSig) || !isGesuchQuery(query)) {
        return;
      }

      this.gesuchStore.loadGesuche$({
        getGesucheSBQueryType: query,
        ...filter,
        ...startEndFilter,
        ...getSortAndPageInputs(this),
      });
    });

    // Load Darlehen effect
    effect(() => {
      const query = this.queryFromInputsSig().query;
      const filter = this.filterInputsSig();
      const startEndFilter = this.startEndFilterInputsSig();

      if (!untracked(this.isDarlehenModeSig) || !isDarlehenQuery(query)) {
        return;
      }

      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const { typ: _typ, status: _status, ...darlehenFilter } = filter;

      const { sortColumn, ...restSortAndPage } = getSortAndPageInputs(this);

      if (sortColumn && !isDarlehenDashboardColumn(sortColumn)) {
        return;
      }

      this.darlehenStore.getDarlehenDashboardSb$({
        getFreiwilligDarlehenSbQueryType: query,
        ...darlehenFilter,
        status: parseDarlehenStatus(this.status()),
        ...startEndFilter,
        sortColumn: sortColumn,
        ...restSortAndPage,
      });
    });
  }

  resetStatus() {
    this.filterForm.controls.status.reset();
  }
}

const parseTyp = (typ: string | undefined): GesuchTrancheTyp | undefined => {
  if (typ && Object.keys(GesuchTrancheTyp).includes(typ)) {
    return typ as GesuchTrancheTyp;
  }

  return undefined;
};

const parseStatus = (status: string | undefined): Gesuchstatus | undefined => {
  if (!status || !Object.keys(Gesuchstatus).includes(status)) {
    return undefined;
  }
  return status as Gesuchstatus;
};

const parseDarlehenStatus = (
  status: string | undefined,
): DarlehenStatus | undefined => {
  if (!status || !Object.keys(DarlehenStatus).includes(status)) {
    return undefined;
  }
  return status as DarlehenStatus;
};

const isDarlehenDashboardColumn = (
  column: SbGesucheDashboardColumn | SbFreiwilligDarlehenDashboardColumn,
): column is SbFreiwilligDarlehenDashboardColumn => {
  return column !== 'TYP';
};

const createQuery = <T extends Partial<GesuchServiceGetGesucheSbRequestParams>>(
  value: T,
) => {
  return value;
};
