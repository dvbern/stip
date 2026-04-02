import { A11yModule } from '@angular/cdk/a11y';
import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  InputSignal,
  OnInit,
  QueryList,
  Signal,
  ViewChildren,
  computed,
  effect,
  inject,
  input,
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
import {
  GesuchServiceGetGesucheSbRequestParams,
  GesuchTrancheStatus,
  GesuchTrancheTyp,
  Gesuchstatus,
  SbDashboardGesuch,
  SbGesucheDashboardColumn,
  SortOrder,
} from '@dv/shared/model/gesuch';
import { SortAndPageInputs } from '@dv/shared/model/table';
import { AppendFromTo, isDefined } from '@dv/shared/model/type-util';
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
import { provideDvDateAdapter } from '@dv/shared/util/date-adapter';
import {
  DashboardQuery,
  FilterTabParam,
  ScopeParam,
  WorkableParam,
  getControlVisibility,
  getQueryFromParams,
  getQueryParamsFromToggleValues,
  isGesuchQuery,
} from '@dv/shared/util/navigation';
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

// todo: eventually move!
const DEFAULT_SCOPE: ScopeParam = 'MEINE' as const;
const DEFAULT_FILTER_TAB: FilterTabParam = 'GESUCHE' as const;
const DEFAULT_WORKABLE: WorkableParam = 'TRUE';

const statusByTyp = {
  TRANCHE: Object.values(Gesuchstatus).filter(
    (key: Gesuchstatus) => key !== 'IN_BEARBEITUNG_GS',
  ),
  AENDERUNG: Object.values(GesuchTrancheStatus).filter(
    (key: GesuchTrancheStatus) => key !== 'IN_BEARBEITUNG_GS',
  ),
} satisfies Record<GesuchTrancheTyp, unknown>;

type DashboardFormStatus = Gesuchstatus | GesuchTrancheStatus;

type DashboardEntry = Omit<
  SbDashboardGesuch,
  'id' | 'gesuchTrancheId' | 'gesuchStatus' | 'trancheStatus'
> & { status: DashboardFormStatus };
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

@Component({
  selector: 'dv-sachbearbeitung-app-feature-gesuche',
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
  templateUrl: './sachbearbeitung-app-feature-gesuche.component.html',
  providers: [provideDvDateAdapter(), paginatorTranslationProvider()],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureGesucheComponent
  implements
    OnInit,
    Record<DashboardFormFields, InputSignal<string | undefined>>,
    SortAndPageInputs<SbGesucheDashboardColumn>
{
  private store = inject(Store);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private formBuilder = inject(NonNullableFormBuilder);

  massendruckStore = inject(MassendruckStore);
  // Due to lack of space, the following inputs are not suffixed with 'Sig'

  // think about better types
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
  sortColumn = input<SbGesucheDashboardColumn | undefined>(undefined);
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
  displayedColumns = Object.keys(SbGesucheDashboardColumn);

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

  queryFromInputsSig = computed<{
    query: DashboardQuery;
    scopeControl: { show: boolean; value: boolean };
    workableControl: { show: boolean; value: boolean };
  }>(() => {
    const filterTab = this.filterTab() ?? DEFAULT_FILTER_TAB;
    const scope = this.scope() ?? DEFAULT_SCOPE;
    const workable = this.workable() ?? DEFAULT_WORKABLE;

    const query = getQueryFromParams(scope, filterTab, workable);

    const { scopeControl, workableControl } = getControlVisibility(
      scope,
      filterTab,
      workable,
    );

    return {
      query,
      scopeControl,
      workableControl,
    };
  });

  showScopeToggleSig = computed(() => {
    const { scopeControl } = this.queryFromInputsSig();
    this.togglesGroup.controls.scope.setValue(scopeControl.value, {
      emitEvent: false,
    });

    return scopeControl.show;
  });

  showWorkableToggleSig = computed(() => {
    const { workableControl } = this.queryFromInputsSig();
    this.togglesGroup.controls.workable.setValue(workableControl.value, {
      emitEvent: false,
    });

    return workableControl.show;
  });

  sortList = sortList(this.router, this.route);
  paginateList = paginateList(this.router, this.route);
  sortSig = viewChild.required(MatSort);
  paginatorSig = viewChild.required(MatPaginator);
  gesuchStore = inject(GesuchStore);

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
      status: statusByTyp[typ],
    };
  });

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
        } satisfies Record<DashboardEntryFields, unknown> & {
          id: string;
          trancheId: string;
          translationKey: string;
        };
      });
    const dataSource = new MatTableDataSource(gesuche);

    return dataSource;
  });
  totalEntriesSig = computed(() => {
    return this.gesuchStore.cockpitViewSig()?.gesuche?.totalEntries;
  });

  canCreateMassendruckSig: Signal<boolean> = computed(() => {
    const quickFilter = this.filterTab();
    const isQuickfilterDruck = quickFilter?.includes('DRUCKBAR');
    this.filterFormChangedSig();

    if (!isQuickfilterDruck) {
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
    if (!isGesuchQuery(query)) {
      throw new Error('Invalid query type for Gesuch context');
    }
    this.massendruckStore.createMassendruckJobForQueryType$({
      req: { getGesucheSBQueryType: query },
      onSuccess: () => {
        this.router.navigate(['/massendruck']);
      },
    });
  }

  constructor() {
    limitPageToNumberOfEntriesEffect(
      this,
      this.totalEntriesSig,
      this.router,
      this.route,
    );

    // Handle table filter form control changes
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

      const params = makeEmptyStringPropertiesNull(query);

      this.router.navigate(['.'], {
        relativeTo: this.route,
        queryParams: params,
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

    // Handle switch changes for 'scope' and 'workable'
    effect(() => {
      const scopeChanged = scopeChangedSig();
      const workableChanged = workableChangedSig();
      const filterTab = untracked(this.filterTab);

      if (!filterTab) {
        return;
      }

      const { scope, workable } = getQueryParamsFromToggleValues(
        scopeChanged,
        workableChanged,
        filterTab,
      );

      this.router.navigate(['.'], {
        relativeTo: this.route,
        queryParams: {
          scope,
          workable,
        },
        queryParamsHandling: 'merge',
      });
    });

    // Load effect - When the route param inputs change, load the gesuche
    effect(() => {
      const { query, filter, startEndFilter } = this.getInputs();

      if (!isGesuchQuery(query.query)) {
        // Skip loading if the query is not a Gesuch query
        return;
      }

      this.gesuchStore.loadGesuche$({
        getGesucheSBQueryType: query.query,
        ...filter,
        ...startEndFilter,
        ...getSortAndPageInputs(this),
      });
    });
  }

  private getInputs() {
    const query = this.queryFromInputsSig();
    const filter = {
      fallNummer: this.fallNummer(),
      typ: parseTyp(this.typ()) ?? 'TRANCHE',
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
    this.filterForm.reset(
      {
        ...filter,
        piaGeburtsdatum: parseDate(filter.piaGeburtsdatum ?? ''),
      },
      { emitEvent: false },
    );
    this.filterStartEndForm.reset(
      {
        ...startEndFilter,
        letzteAktivitaetFrom: parseDate(
          startEndFilter.letzteAktivitaetFrom ?? '',
        ),
        letzteAktivitaetTo: parseDate(startEndFilter.letzteAktivitaetTo ?? ''),
      },
      { emitEvent: false },
    );

    this.togglesGroup.controls.scope.setValue(query.scopeControl.value);
    this.togglesGroup.controls.workable.setValue(query.workableControl.value);

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

const createQuery = <T extends Partial<GesuchServiceGetGesucheSbRequestParams>>(
  value: T,
) => {
  return value;
};
