import { A11yModule } from '@angular/cdk/a11y';
import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  QueryList,
  ViewChild,
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
  MatPaginatorIntl,
  MatPaginatorModule,
} from '@angular/material/paginator';
import { MatRadioModule } from '@angular/material/radio';
import { MatSelectModule } from '@angular/material/select';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { Router, RouterModule } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';
import {
  differenceInCalendarMonths,
  differenceInCalendarYears,
  differenceInDays,
  format,
  isSameDay,
  isWithinInterval,
  startOfDay,
} from 'date-fns';

import { SachbearbeitungAppPatternOverviewLayoutComponent } from '@dv/sachbearbeitung-app/pattern/overview-layout';
import { SharedDataAccessGesuchEvents } from '@dv/shared/data-access/gesuch';
import {
  GesuchFilter,
  GesuchTrancheTyp,
  Gesuchstatus,
  SharedModelGesuch,
} from '@dv/shared/model/gesuch';
import { SharedUiClearButtonComponent } from '@dv/shared/ui/clear-button';
import {
  SharedUiFocusableListDirective,
  SharedUiFocusableListItemDirective,
} from '@dv/shared/ui/focusable-list';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiTableHeaderFilterComponent } from '@dv/shared/ui/table-header-filter';
import {
  TypeSafeMatCellDefDirective,
  TypeSafeMatRowDefDirective,
} from '@dv/shared/ui/table-helper';
import { SharedUiVersionTextComponent } from '@dv/shared/ui/version-text';
import { provideDvDateAdapter } from '@dv/shared/util/date-adapter';
import { SharedUtilPaginatorTranslation } from '@dv/shared/util/paginator-translation';
import { isDefined } from '@dv/shared/util-fn/type-guards';

import { selectSachbearbeitungAppFeatureCockpitView } from './sachbearbeitung-app-feature-cockpit.selector';

const DEFAULT_FILTER: GesuchFilter = 'ALLE_BEARBEITBAR_MEINE';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-cockpit',
  standalone: true,
  imports: [
    A11yModule,
    CommonModule,
    TranslateModule,
    MatTableModule,
    MatSortModule,
    MatSlideToggleModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatSelectModule,
    MatRadioModule,
    ReactiveFormsModule,
    RouterModule,
    MatPaginatorModule,
    SharedUiIconChipComponent,
    SharedUiFocusableListItemDirective,
    SharedUiFocusableListDirective,
    SharedUiLoadingComponent,
    SharedUiVersionTextComponent,
    TypeSafeMatCellDefDirective,
    TypeSafeMatRowDefDirective,
    RouterModule,
    A11yModule,
    SharedUiIconChipComponent,
    MatPaginatorModule,
    SharedUiTableHeaderFilterComponent,
    SharedUiClearButtonComponent,
    SachbearbeitungAppPatternOverviewLayoutComponent,
  ],
  templateUrl: './sachbearbeitung-app-feature-cockpit.component.html',
  styleUrls: ['./sachbearbeitung-app-feature-cockpit.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [
    provideDvDateAdapter(),
    { provide: MatPaginatorIntl, useClass: SharedUtilPaginatorTranslation },
  ],
})
export class SachbearbeitungAppFeatureCockpitComponent implements OnInit {
  private store = inject(Store);
  private router = inject(Router);
  private formBuilder = inject(NonNullableFormBuilder);
  showSig = input<GesuchFilter | undefined>(undefined, {
    alias: 'show',
  });

  @ViewChildren(SharedUiFocusableListItemDirective)
  items?: QueryList<SharedUiFocusableListItemDirective>;
  @ViewChild('gesuchePaginator', { static: true }) paginator!: MatPaginator;
  displayedColumns = [
    'fall',
    'typ',
    'nachname',
    'vorname',
    'geburtsdatum',
    'status',
    'bearbeiter',
    'letzteAktivitaet',
  ];

  filterForm = this.formBuilder.group({
    fall: [<string | undefined>undefined],
    typ: [''],
    nachname: [<string | undefined>undefined],
    vorname: [<string | undefined>undefined],
    geburtsdatum: [<Date | undefined>undefined],
    status: [''],
    bearbeiter: [<string | undefined>undefined],
    letzteAktivitaetStart: [<Date | undefined>undefined],
    letzteAktivitaetEnd: [<Date | undefined>undefined],
  });

  quickFilterForm = this.formBuilder.group({
    query: [<GesuchFilter | undefined>undefined],
  });
  dataSoruce = new MatTableDataSource<SharedModelGesuch>([]);

  cockpitViewSig = this.store.selectSignal(
    selectSachbearbeitungAppFeatureCockpitView,
  );
  quickFilters: { typ: GesuchFilter; icon: string }[] = [
    {
      typ: 'ALLE_BEARBEITBAR_MEINE',
      icon: 'person',
    },
    {
      typ: 'ALLE_BEARBEITBAR',
      icon: 'people',
    },
    {
      typ: 'ALLE',
      icon: 'all_inclusive',
    },
  ];
  showViewSig = computed<GesuchFilter>(() => {
    const show = this.showSig();
    return show ?? DEFAULT_FILTER;
  });

  private filterFormChangedSig = toSignal(this.filterForm.valueChanges);
  availableStatusSig = computed(() => {
    return this.cockpitViewSig().gesuche.reduce<Gesuchstatus[]>(
      (acc, gesuch) =>
        acc.includes(gesuch.gesuchStatus) ? acc : [...acc, gesuch.gesuchStatus],
      [],
    );
  });
  availableStatus = Object.values(GesuchTrancheTyp);
  private letzteAktivitaetStartChangedSig = toSignal(
    this.filterForm.controls.letzteAktivitaetStart.valueChanges,
  );
  private letzteAktivitaetEndChangedSig = toSignal(
    this.filterForm.controls.letzteAktivitaetEnd.valueChanges,
  );
  letzteAktivitaetRangeSig = computed(() => {
    const start = this.letzteAktivitaetStartChangedSig();
    const end = this.letzteAktivitaetEndChangedSig();

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

  gesucheDataSourceSig = computed(() => {
    const sort = this.sortSig();
    const gesuche = this.cockpitViewSig().gesuche.map((gesuch) => ({
      id: gesuch.id,
      trancheId: gesuch.gesuchTrancheToWorkWith?.id,
      fall: gesuch.fall.fallNummer,
      typ: gesuch.gesuchTrancheToWorkWith?.typ,
      nachname:
        gesuch.gesuchTrancheToWorkWith?.gesuchFormular?.personInAusbildung
          ?.nachname,
      vorname:
        gesuch.gesuchTrancheToWorkWith?.gesuchFormular?.personInAusbildung
          ?.vorname,
      geburtsdatum:
        gesuch.gesuchTrancheToWorkWith?.gesuchFormular?.personInAusbildung
          ?.geburtsdatum,
      status: gesuch.gesuchStatus,
      bearbeiter: gesuch.bearbeiter,
      letzteAktivitaet: gesuch.aenderungsdatum,
    }));
    const filterForm = this.filterFormChangedSig();

    const dataSource = new MatTableDataSource(gesuche);

    dataSource.paginator = this.paginator;
    if (sort) {
      dataSource.sort = sort;
    }

    if (hasFilterValues(filterForm)) {
      dataSource.filterPredicate = (data) =>
        [
          checkFilter(data.fall, filterForm.fall),
          checkFilter(data.typ, filterForm.typ),
          checkFilter(data.nachname, filterForm.nachname),
          checkFilter(data.vorname, filterForm.vorname),
          checkFilter(data.geburtsdatum, filterForm.geburtsdatum),
          checkFilter(data.status, filterForm.status),
          checkFilter(data.bearbeiter, filterForm.bearbeiter),
          checkFilter(data.letzteAktivitaet, [
            filterForm.letzteAktivitaetStart,
            filterForm.letzteAktivitaetEnd,
          ]),
        ].every((result) => result);
      dataSource.filter = JSON.stringify(filterForm);
    } else {
      dataSource.filter = '';
    }

    return dataSource;
  });
  sortSig = viewChild(MatSort);

  constructor() {
    let isFirstChange = true;
    effect(
      () => {
        const query = this.showViewSig();
        if (isFirstChange) {
          isFirstChange = false;
          return;
        }
        this.store.dispatch(
          SharedDataAccessGesuchEvents.loadAllDebounced({
            query,
          }),
        );
      },
      { allowSignalWrites: true },
    );

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
          replaceUrl: true,
        });
      },
      { allowSignalWrites: true },
    );
  }

  ngOnInit() {
    const query = this.showViewSig();
    this.quickFilterForm.reset({ query });
    this.store.dispatch(
      SharedDataAccessGesuchEvents.loadAll({
        query,
      }),
    );
  }
}

const hasFilterValues = <T extends Record<string, unknown>>(
  values?: T,
): values is NonNullable<T> => {
  if (!values) {
    return false;
  }
  return Object.values(values).some(
    (value) => isDefined(value) && value !== '',
  );
};

const checkFilter = <T>(
  value: T,
  filter:
    | string
    | number
    | Date
    | [Date | undefined, Date | undefined]
    | undefined
    | null,
) => {
  if (!isDefined(value) || !isDefined(filter)) {
    return true;
  }
  if (typeof value === 'number' || typeof filter === 'number') {
    return value.toString() === filter.toString();
  }
  if (typeof value === 'string') {
    if (!value) {
      return true;
    }
    if (filter instanceof Date) {
      return isSameDay(new Date(value), filter);
    }
    if (Array.isArray(filter)) {
      return isInterval(filter)
        ? isWithinInterval(startOfDay(new Date(value)), {
            start: startOfDay(filter[0]),
            end: startOfDay(filter[1]),
          })
        : true;
    }
    return value.toLocaleLowerCase().includes(filter.toLocaleLowerCase());
  }
  throw new Error('Unsupported type');
};

const isInterval = (value: unknown[]): value is [Date, Date] => {
  return value.length === 2 && value.every((v) => v instanceof Date);
};

const getDiffFormat = (
  date: Date,
  difference: { months: number; years: number },
) => {
  let value = format(date, 'dd.');
  if (difference.months > 0) {
    value += format(date, 'MM.');
  }
  if (difference.years > 0) {
    value += format(date, 'yy');
  }
  return value;
};
