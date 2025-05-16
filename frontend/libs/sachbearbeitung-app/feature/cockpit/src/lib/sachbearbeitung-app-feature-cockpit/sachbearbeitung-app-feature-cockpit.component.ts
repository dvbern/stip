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
import { MatRadioModule } from '@angular/material/radio';
import { MatSelectModule } from '@angular/material/select';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSort, MatSortModule, Sort } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Router, RouterModule } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';
import {
  differenceInCalendarMonths,
  differenceInCalendarYears,
  differenceInDays,
  format,
} from 'date-fns';
import { debounceTime, distinctUntilChanged } from 'rxjs';

import { GesuchStore } from '@dv/sachbearbeitung-app/data-access/gesuch';
import { SachbearbeitungAppPatternOverviewLayoutComponent } from '@dv/sachbearbeitung-app/pattern/overview-layout';
import { selectVersion } from '@dv/shared/data-access/config';
import { PermissionStore } from '@dv/shared/global/permission';
import { BenutzerRole } from '@dv/shared/model/benutzer';
import {
  GesuchFilter,
  GesuchServiceGetGesucheSbRequestParams,
  GesuchTrancheStatus,
  GesuchTrancheTyp,
  Gesuchstatus,
  SbDashboardColumn,
  SbDashboardGesuch,
  SharedModelGesuch,
  SortOrder,
} from '@dv/shared/model/gesuch';
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
  sortMap,
} from '@dv/shared/util-fn/filter-util';

const DEFAULT_FILTER = {
  jurist: 'ALLE_JURISTISCHE_ABKLAERUNG_MEINE',
  other: 'ALLE_BEARBEITBAR_MEINE',
} satisfies Record<string, GesuchFilter>;

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
  selector: 'dv-sachbearbeitung-app-feature-cockpit',
  standalone: true,
  imports: [
    A11yModule,
    CommonModule,
    TranslatePipe,
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
    SachbearbeitungAppPatternOverviewLayoutComponent,
  ],
  templateUrl: './sachbearbeitung-app-feature-cockpit.component.html',
  styleUrls: ['./sachbearbeitung-app-feature-cockpit.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [provideDvDateAdapter(), paginatorTranslationProvider()],
})
export class SachbearbeitungAppFeatureCockpitComponent
  implements
    OnInit,
    Record<DashboardFormFields, InputSignal<string | undefined>>
{
  private store = inject(Store);
  private router = inject(Router);
  private permissionStore = inject(PermissionStore);
  private formBuilder = inject(NonNullableFormBuilder);
  // Due to lack of space, the following inputs are not suffixed with 'Sig'
  show = input<GesuchFilter | undefined>(undefined);
  fallNummer = input<string | undefined>(undefined);
  typ = input<string | undefined>(undefined);
  piaNachname = input<string | undefined>(undefined);
  piaVorname = input<string | undefined>(undefined);
  piaGeburtsdatum = input<string | undefined>(undefined);
  status = input<string | undefined>(undefined);
  bearbeiter = input<string | undefined>(undefined);
  letzteAktivitaetFrom = input<string | undefined>(undefined);
  letzteAktivitaetTo = input<string | undefined>(undefined);
  sortColumn = input<SbDashboardColumn | undefined>(undefined);
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
  displayedColumns = Object.keys(SbDashboardColumn);

  private defaultFilterSig = computed(() => {
    const rolesMap = this.permissionStore.rolesMapSig();

    return rolesMap.V0_Jurist ? DEFAULT_FILTER.jurist : DEFAULT_FILTER.other;
  });
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

  quickFilterForm = this.formBuilder.group({
    query: [<GesuchFilter | undefined>undefined],
  });

  pageSizes = PAGE_SIZES;
  defaultPageSize = DEFAULT_PAGE_SIZE;
  availableTypes = Object.values(GesuchTrancheTyp);
  versionSig = this.store.selectSignal(selectVersion);
  showViewSig = computed<GesuchFilter>(() => {
    const show = this.show();
    return show ?? this.defaultFilterSig();
  });
  sortSig = viewChild.required(MatSort);
  paginatorSig = viewChild.required(MatPaginator);
  gesuchStore = inject(GesuchStore);
  dataSoruce = new MatTableDataSource<SharedModelGesuch>([]);

  quickFilters: {
    typ: GesuchFilter;
    icon: string;
    roles: BenutzerRole[];
  }[] = [
    {
      typ: 'ALLE_JURISTISCHE_ABKLAERUNG_MEINE',
      icon: 'person',
      roles: ['V0_Jurist'],
    },
    {
      typ: 'ALLE_BEARBEITBAR_MEINE',
      icon: 'person',
      roles: ['V0_Sachbearbeiter'],
    },
    {
      typ: 'ALLE_BEARBEITBAR',
      icon: 'people',
      roles: ['V0_Sachbearbeiter'],
    },
    {
      typ: 'ALLE',
      icon: 'all_inclusive',
      roles: ['V0_Sachbearbeiter', 'V0_Jurist'],
    },
  ];

  private letzteAktivitaetFromChangedSig = toSignal(
    this.filterStartEndForm.controls.letzteAktivitaetFrom.valueChanges,
  );
  private letzteAktivitaetToChangedSig = toSignal(
    this.filterStartEndForm.controls.letzteAktivitaetTo.valueChanges,
  );
  availableQuickFiltersSig = computed(() => {
    const roles = this.permissionStore.rolesMapSig();

    return this.quickFilters.filter((filter) =>
      filter.roles.some((role) => roles?.[role]),
    );
  });
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

  filterFormChangedSig = toSignal(
    this.filterForm.valueChanges.pipe(debounceTime(INPUT_DELAY)),
  );
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

  constructor() {
    // Handle the case where the page is higher than the total number of pages
    effect(
      () => {
        const { page, pageSize } = this.getInputs();
        const totalEntries =
          this.gesuchStore.cockpitViewSig()?.gesuche?.totalEntries;

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

    // Handle normal filter form control changes
    effect(
      () => {
        this.filterFormChangedSig();
        const formValue = this.filterForm.getRawValue();
        const query = createQuery({
          ...formValue,
          piaGeburtsdatum: formValue.piaGeburtsdatum
            ? toBackendLocalDate(formValue.piaGeburtsdatum)
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

    // Handle start-end filter form control changes seperately
    effect(
      () => {
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
        const defaultFilter = this.defaultFilterSig();
        if (!query) {
          return;
        }
        this.router.navigate(['.'], {
          queryParams: {
            show: query === defaultFilter ? undefined : query,
          },
          queryParamsHandling: 'merge',
          replaceUrl: true,
        });
      },
      { allowSignalWrites: true },
    );

    // When the route param inputs change, load the gesuche
    effect(
      () => {
        const {
          query,
          filter,
          startEndFilter,
          sortColumn,
          sortOrder,
          page,
          pageSize,
        } = this.getInputs();

        this.gesuchStore.loadGesuche$({
          getGesucheSBQueryType: query,
          ...filter,
          ...startEndFilter,
          sortColumn,
          sortOrder,
          page: page ?? 0,
          pageSize: pageSize ?? DEFAULT_PAGE_SIZE,
        });
      },
      { allowSignalWrites: true },
    );
  }

  /**
   * Bundle all route param inputs into an object
   */
  private getInputs() {
    const query = this.showViewSig();
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
    const sortColumn = this.sortColumn();
    const sortOrder = this.sortOrder();
    const page = this.page();
    const pageSize = this.pageSize();

    return {
      query,
      filter,
      startEndFilter,
      sortColumn,
      sortOrder,
      page,
      pageSize,
    };
  }

  sortData(event: Sort) {
    this.router.navigate(['.'], {
      queryParams: createQuery({
        // display column names are set to the enum values, so this is safe to cast
        sortColumn: event.active as SbDashboardColumn,
        sortOrder: sortMap[event.direction],
      }),
      queryParamsHandling: 'merge',
      replaceUrl: true,
    });
  }

  resetStatus() {
    this.filterForm.controls.status.reset();
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
    const { query, filter, startEndFilter, sortColumn, sortOrder } =
      this.getInputs();

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
