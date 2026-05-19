import { DatePipe } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
  viewChild,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { NonNullableFormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDialog } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatPaginator } from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MaskitoDirective } from '@maskito/angular';
import {
  addDays,
  differenceInCalendarMonths,
  differenceInCalendarYears,
  differenceInDays,
  endOfDay,
  format,
  startOfDay,
} from 'date-fns';
import { debounceTime } from 'rxjs';

import { BfsStatistikStore } from '@dv/sachbearbeitung-app/data-access/bfs-statistik';
import { SachbearbeitungAppDialogCreateBfsStatistikComponent } from '@dv/sachbearbeitung-app/dialog/create-bfs-statistik';
import { SachbearbeitungAppUiAdvTranslocoDirective } from '@dv/sachbearbeitung-app/ui/adv-transloco-directive';
import { Statistik } from '@dv/shared/model/gesuch';
import { isDefined } from '@dv/shared/model/type-util';
import {
  DEFAULT_PAGE_SIZE,
  INPUT_DELAY,
  PAGE_SIZES,
} from '@dv/shared/model/ui-constants';
import { SharedUiClearButtonComponent } from '@dv/shared/ui/clear-button';
import { SharedUiDownloadButtonDirective } from '@dv/shared/ui/download-button';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { provideDvDateAdapter } from '@dv/shared/util/date-adapter';
import { maskitoYear } from '@dv/shared/util/maskito-util';
import { getDiffFormat } from '@dv/shared/util/validator-date';

type FilterTypes =
  | keyof Statistik
  | `${keyof Pick<Statistik, 'timestampErstellt'>}${'Von' | 'Bis'}`;

@Component({
  selector: 'dv-sachbearbeitung-app-feature-administration-bfs-statistik',
  imports: [
    DatePipe,
    ReactiveFormsModule,
    MatSelectModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatButtonModule,
    MatSortModule,
    MatTooltipModule,
    MatTableModule,
    MatIconModule,
    MatPaginator,
    MaskitoDirective,
    SachbearbeitungAppUiAdvTranslocoDirective,
    SharedUiMaxLengthDirective,
    SharedUiClearButtonComponent,
    SharedUiIconChipComponent,
    SharedUiDownloadButtonDirective,
    TypeSafeMatCellDefDirective,
  ],
  providers: [provideDvDateAdapter()],
  templateUrl:
    './sachbearbeitung-app-feature-administration-bfs-statistik.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureAdministrationBfsStatistikComponent {
  private formBuilder = inject(NonNullableFormBuilder);
  private bfsStatistikStore = inject(BfsStatistikStore);
  private sortSig = viewChild(MatSort);
  private paginatorSig = viewChild(MatPaginator);
  private dialog = inject(MatDialog);
  displayedColumns: string[] = [
    'year',
    'timestampErstellt',
    'userTriggeredCreation',
    'file',
  ];
  pageSizes = PAGE_SIZES;
  defaultPageSize = DEFAULT_PAGE_SIZE;
  maskitoYear = maskitoYear({ max: new Date().getFullYear() });

  filterForm = this.formBuilder.group({
    year: [<string | null>null],
    timestampErstelltVon: [<Date | undefined>undefined],
    timestampErstelltBis: [<Date | undefined>undefined],
    userTriggeredCreation: [<string | null>null],
  } satisfies Partial<Record<FilterTypes, unknown>>);
  filterChangedSig = toSignal(
    this.filterForm.valueChanges.pipe(debounceTime(INPUT_DELAY)),
  );

  timestampErstellVonChangedSig = toSignal(
    this.filterForm.controls.timestampErstelltVon.valueChanges,
  );
  timestampErstellBisChangedSig = toSignal(
    this.filterForm.controls.timestampErstelltBis.valueChanges,
  );

  timestampErstelltRangeSig = computed(() => {
    const von = this.timestampErstellVonChangedSig();
    const bis = this.timestampErstellBisChangedSig();

    if (!von || !bis) {
      return '';
    }
    const difference = {
      days: differenceInDays(bis, von),
      months: differenceInCalendarMonths(bis, von),
      years: differenceInCalendarYears(bis, von),
    };
    return difference.days
      ? [
          `${getDiffFormat(von, difference)}`,
          `${format(bis, 'dd.MM.yy')}`,
        ].join(' - ')
      : format(von, 'dd.MM.yyyy');
  });

  bfsStatistikDataSourceSig = computed(() => {
    const allCountries = this.bfsStatistikStore.bfsStatistikListViewSig() ?? [];
    const datasource = new MatTableDataSource(allCountries);
    const paginator = this.paginatorSig();
    const sort = this.sortSig();

    datasource.filterPredicate = filterData;

    if (paginator) {
      datasource.paginator = paginator;
    }

    if (sort) {
      datasource.sort = sort;
    }

    return datasource;
  });

  createStatistik() {
    SachbearbeitungAppDialogCreateBfsStatistikComponent.open(this.dialog)
      .afterClosed()
      .subscribe((year) => {
        if (year) {
          this.bfsStatistikStore.createBfsStatistik$({
            year,
            onSuccess: () => {
              this.bfsStatistikStore.loadAllBfsStatistik$();
            },
          });
        }
      });
  }

  constructor() {
    this.bfsStatistikStore.loadAllBfsStatistik$();

    effect(() => {
      const filter = this.filterChangedSig();
      this.bfsStatistikDataSourceSig().filter = JSON.stringify(filter);
    });
  }
}

const filterData = (data: Statistik, filter: string) => {
  const filterCriteria = JSON.parse(filter);
  console.log('filtering', { data, filter });

  return Object.entries(filterCriteria).every(([key, value]) =>
    filterFn(key as FilterTypes, value, data),
  );
};

const filterFn = (key: FilterTypes, value: unknown, data: Statistik) => {
  if (!isDefined(value)) {
    return true;
  }

  switch (key) {
    case 'timestampErstelltVon':
      return data.timestampErstellt > startOfDay(value as string).toISOString();
    case 'timestampErstelltBis':
      return (
        data.timestampErstellt <
        addDays(endOfDay(value as string), 1).toISOString()
      );
  }

  return data[key].toString().includes(value as string);
};
