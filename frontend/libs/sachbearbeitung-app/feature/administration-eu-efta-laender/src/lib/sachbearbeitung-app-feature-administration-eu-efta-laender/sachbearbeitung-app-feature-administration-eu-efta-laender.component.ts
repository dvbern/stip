import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  computed,
  effect,
  inject,
  signal,
  viewChild,
} from '@angular/core';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import {
  FormControl,
  NonNullableFormBuilder,
  ReactiveFormsModule,
} from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDialog } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatPaginator } from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { TranslatePipe } from '@ngx-translate/core';
import { debounceTime, map } from 'rxjs';

import { SachbearbeitungAppDialogEuEftaLaenderEditComponent } from '@dv/sachbearbeitung-app/dialog/eu-efta-laender-edit';
import { LandStore } from '@dv/shared/data-access/land';
import { GlobalNotificationStore } from '@dv/shared/global/notification';
import { Land } from '@dv/shared/model/gesuch';
import { INPUT_DELAY } from '@dv/shared/model/ui-constants';
import { SharedUiClearButtonComponent } from '@dv/shared/ui/clear-button';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { provideMaterialDefaultOptions } from '@dv/shared/util/form';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-administration-eu-efta-laender',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TranslatePipe,
    MatSelectModule,
    MatFormFieldModule,
    MatInputModule,
    SharedUiMaxLengthDirective,
    MatInputModule,
    MatCheckboxModule,
    SharedUiClearButtonComponent,
    MatButtonModule,
    MatSortModule,
    MatTooltipModule,
    MatTableModule,
    MatIconModule,
    TypeSafeMatCellDefDirective,
    MatPaginator,
  ],
  providers: [
    provideMaterialDefaultOptions({
      subscriptSizing: 'dynamic',
    }),
  ],
  templateUrl:
    './sachbearbeitung-app-feature-administration-eu-efta-laender.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureAdministrationEuEftaLaenderComponent {
  private formBuilder = inject(NonNullableFormBuilder);
  private dialog = inject(MatDialog);
  private sortSig = viewChild(MatSort);
  private destroyRef = inject(DestroyRef);
  private notificationStore = inject(GlobalNotificationStore);
  paginatorSig = viewChild(MatPaginator);

  filterChangedSig = signal<string | null>(null);

  laenderStore = inject(LandStore);
  countryFilter = new FormControl<string | null>(null);

  displayedColumns: string[] = [
    'iso3code',
    'deKurzform',
    'frKurzform',
    'eintragGueltig',
    'isEuEfta',
    'actions',
  ];

  filterForm = this.formBuilder.group({
    iso3code: [<string | null>null],
    deKurzform: [<string | null>null],
    frKurzform: [<string | null>null],
    eintragGueltig: [<boolean | undefined>undefined],
    isEuEfta: [<boolean | undefined>undefined],
  });

  boolFilterValues = [
    {
      value: true,
      key: 'TRUE',
    },
    {
      value: false,
      key: 'FALSE',
    },
  ];

  private filterFormChangedSig = toSignal(
    this.filterForm.valueChanges.pipe(
      debounceTime(INPUT_DELAY),
      map(() => this.filterForm.getRawValue()),
    ),
  );

  countryDataSourceSig = computed(() => {
    const allCountries = this.laenderStore.landListViewSig() ?? [];
    const datasource = new MatTableDataSource(allCountries);

    this.configureDatasource(datasource);
    this.applyFilterToDataSource(datasource);

    return datasource;
  });

  constructor() {
    this.laenderStore.loadLaender$();

    effect(() => {
      const filterValues = this.filterFormChangedSig();
      this.countryDataSourceSig().filter = JSON.stringify(filterValues);
    });

    // Set default sorting
    effect(() => {
      const sort = this.sortSig();
      if (sort && !sort.active) {
        sort.sort({
          id: 'deKurzform',
          start: 'asc',
          disableClear: false,
        });
      }
    });
  }

  openDialog(land?: Land) {
    SachbearbeitungAppDialogEuEftaLaenderEditComponent.open(this.dialog, {
      land: land,
      laender: this.laenderStore.landListViewSig() ?? [],
    })
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((land) => {
        if (!land) {
          return;
        }

        if (land.id) {
          this.laenderStore.updateLand$({
            land,
            landId: land.id,
            onSuccess: () => {
              this.notificationStore.createSuccessNotification({
                messageKey:
                  'sachbearbeitung-app.admin.euEftaLaender.edit.success',
              });
            },
          });
        } else {
          this.laenderStore.createLand$({
            land,
            onSuccess: () => {
              this.notificationStore.createSuccessNotification({
                messageKey:
                  'sachbearbeitung-app.admin.euEftaLaender.create.success',
              });
            },
          });
        }
      });
  }

  private configureDatasource(datasource: MatTableDataSource<Land>): void {
    datasource.sortingDataAccessor = this.createSortingDataAccessor();
    datasource.filterPredicate = this.createFilterPredicate();

    const paginator = this.paginatorSig();
    const sort = this.sortSig();

    if (paginator) {
      datasource.paginator = paginator;
    }

    if (sort) {
      datasource.sort = sort;
    }
  }

  private createSortingDataAccessor() {
    return (data: Land, sortHeaderId: string) => {
      if (sortHeaderId === 'iso3code') {
        return data.iso3code ?? 'zzz';
      }

      const value = data[sortHeaderId as keyof Land];

      if (typeof value === 'boolean') {
        return value ? 1 : 0;
      }

      return value !== undefined && value !== null
        ? (value as string | number)
        : '';
    };
  }

  private createFilterPredicate() {
    return (data: Land, filter: string) => {
      const filterCriteria = JSON.parse(filter);
      return this.matchesAllFilterCriteria(data, filterCriteria);
    };
  }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  private matchesAllFilterCriteria(data: Land, criteria: any): boolean {
    return (
      this.matchesTextFilter(data.iso3code, criteria.iso3code) &&
      this.matchesTextFilter(data.deKurzform, criteria.deKurzform) &&
      this.matchesTextFilter(data.frKurzform, criteria.frKurzform) &&
      this.matchesBooleanFilter(data.eintragGueltig, criteria.eintragGueltig) &&
      this.matchesBooleanFilter(data.isEuEfta, criteria.isEuEfta)
    );
  }

  private matchesTextFilter(
    dataValue: string | null | undefined,
    filterValue: string | null,
  ): boolean {
    if (!filterValue) {
      return true;
    }
    return (
      dataValue?.toLowerCase().includes(filterValue.toLowerCase()) ?? false
    );
  }

  private matchesBooleanFilter(
    dataValue: boolean,
    filterValue: boolean | undefined,
  ): boolean {
    if (typeof filterValue !== 'boolean') {
      return true;
    }
    return dataValue === filterValue;
  }

  private applyFilterToDataSource(datasource: MatTableDataSource<Land>): void {
    const filter = this.filterChangedSig();
    if (filter) {
      datasource.filter = filter.trim().toLowerCase();
    }
  }
}
