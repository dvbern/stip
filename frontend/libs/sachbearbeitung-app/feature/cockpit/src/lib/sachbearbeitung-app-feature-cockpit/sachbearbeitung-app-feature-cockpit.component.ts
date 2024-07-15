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
import {
  FormControl,
  NonNullableFormBuilder,
  ReactiveFormsModule,
} from '@angular/forms';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {
  MatPaginator,
  MatPaginatorIntl,
  MatPaginatorModule,
} from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { Router, RouterModule } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';
import { isSameDay, isWithinInterval, startOfDay } from 'date-fns';

import { SachbearbeitungAppPatternOverviewLayoutComponent } from '@dv/sachbearbeitung-app/pattern/overview-layout';
import { SharedDataAccessGesuchEvents } from '@dv/shared/data-access/gesuch';
import { Gesuchstatus, SharedModelGesuch } from '@dv/shared/model/gesuch';
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

@Component({
  selector: 'dv-sachbearbeitung-app-feature-cockpit',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    MatTableModule,
    MatSortModule,
    MatSlideToggleModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatSelectModule,
    ReactiveFormsModule,
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
  showAll = input<true | undefined>(undefined, { alias: 'show-all' });

  @ViewChildren(SharedUiFocusableListItemDirective)
  items?: QueryList<SharedUiFocusableListItemDirective>;
  @ViewChild('gesuchePaginator', { static: true }) paginator!: MatPaginator;
  displayedColumns = [
    'fall',
    'svNummer',
    'nachname',
    'vorname',
    'geburtsdatum',
    'ort',
    'status',
    'bearbeiter',
    'letzteAktivitaet',
  ];

  filterForm = this.formBuilder.group({
    fall: [<string | undefined>undefined],
    svNummer: [<string | undefined>undefined],
    nachname: [<string | undefined>undefined],
    vorname: [<string | undefined>undefined],
    geburtsdatum: [<Date | undefined>undefined],
    ort: [<string | undefined>undefined],
    status: [''],
    bearbeiter: [<string | undefined>undefined],
    letzteAktivitaetStart: [<Date | undefined>undefined],
    letzteAktivitaetEnd: [<Date | undefined>undefined],
  });

  // FormControl is necessary instead of (change) event binding due to an potential issue
  // with the Angular Material SlideToggle, see https://github.com/angular/components/pull/28745
  showAllControl = new FormControl<boolean | undefined>(this.showAll());
  dataSoruce = new MatTableDataSource<SharedModelGesuch>([]);

  cockpitViewSig = this.store.selectSignal(
    selectSachbearbeitungAppFeatureCockpitView,
  );

  private filterFormChangedSig = toSignal(this.filterForm.valueChanges);
  availableStatusSig = computed(() => {
    return this.cockpitViewSig().gesuche.reduce<Gesuchstatus[]>(
      (acc, gesuch) =>
        acc.includes(gesuch.gesuchStatus) ? acc : [...acc, gesuch.gesuchStatus],
      [],
    );
  });
  gesucheDataSourceSig = computed(() => {
    const sort = this.sortSig();
    const gesuche = this.cockpitViewSig().gesuche.map((gesuch) => ({
      id: gesuch.id,
      fall: gesuch.fall.fallNummer,
      svNummer:
        gesuch.gesuchTrancheToWorkWith?.gesuchFormular?.personInAusbildung
          ?.sozialversicherungsnummer,
      nachname:
        gesuch.gesuchTrancheToWorkWith?.gesuchFormular?.personInAusbildung
          ?.nachname,
      vorname:
        gesuch.gesuchTrancheToWorkWith?.gesuchFormular?.personInAusbildung
          ?.vorname,
      geburtsdatum:
        gesuch.gesuchTrancheToWorkWith?.gesuchFormular?.personInAusbildung
          ?.geburtsdatum,
      ort: gesuch.gesuchTrancheToWorkWith?.gesuchFormular?.personInAusbildung
        ?.adresse.ort,
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
          checkFilter(data.svNummer, filterForm.svNummer),
          checkFilter(data.nachname, filterForm.nachname),
          checkFilter(data.vorname, filterForm.vorname),
          checkFilter(data.geburtsdatum, filterForm.geburtsdatum),
          checkFilter(data.ort, filterForm.ort),
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
        const showAll = this.showAll();
        if (isFirstChange) {
          isFirstChange = false;
          return;
        }
        this.store.dispatch(
          SharedDataAccessGesuchEvents.loadAllDebounced({
            filter: { showAll: showAll },
          }),
        );
      },
      { allowSignalWrites: true },
    );

    const showAllChanged = toSignal(this.showAllControl.valueChanges);
    effect(
      () => {
        const showAll = showAllChanged();
        this.router.navigate(['.'], {
          queryParams: showAll ? { ['show-all']: showAll } : undefined,
          replaceUrl: true,
        });
      },
      { allowSignalWrites: true },
    );
  }

  ngOnInit() {
    this.showAllControl.setValue(this.showAll());
    this.store.dispatch(
      SharedDataAccessGesuchEvents.loadAll({
        filter: { showAll: this.showAll() },
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
