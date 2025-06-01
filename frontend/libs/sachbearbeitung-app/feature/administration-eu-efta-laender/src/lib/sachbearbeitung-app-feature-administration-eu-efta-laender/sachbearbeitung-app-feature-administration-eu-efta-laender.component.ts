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
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { debounceTime, map } from 'rxjs';

import { EuEftaLaenderStore } from '@dv/sachbearbeitung-app/data-access/eu-efta-laender';
import { SachbearbeitungAppDialogEuEftaLaenderEditComponent } from '@dv/sachbearbeitung-app/dialog/eu-efta-laender-edit';
import { Land } from '@dv/shared/model/gesuch';
import { INPUT_DELAY } from '@dv/shared/model/ui-constants';
import { SharedUiClearButtonComponent } from '@dv/shared/ui/clear-button';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { provideMaterialDefaultOptions } from '@dv/shared/util/form';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-administration-eu-efta-laender',
  standalone: true,
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
  private translate = inject(TranslateService);
  private formBuilder = inject(NonNullableFormBuilder);
  private dialog = inject(MatDialog);
  private sortSig = viewChild(MatSort);
  private destroyRef = inject(DestroyRef);
  paginatorSig = viewChild(MatPaginator);

  filterChangedSig = signal<string | null>(null);

  laenderStore = inject(EuEftaLaenderStore);
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
    const allCountries = this.laenderStore.euEftaLaenderListViewSig() ?? [];
    const paginator = this.paginatorSig();
    const sort = this.sortSig();
    const filter = this.filterChangedSig();

    const datasource = new MatTableDataSource(allCountries);

    datasource.filterPredicate = (data, filter: string) => {
      const { iso3code, deKurzform, frKurzform, eintragGueltig, isEuEfta } =
        JSON.parse(filter);
      if (
        iso3code &&
        !data.iso3code?.toLowerCase().includes(iso3code.toLowerCase())
      ) {
        return false;
      }
      if (
        deKurzform &&
        !data.deKurzform.toLowerCase().includes(deKurzform.toLowerCase())
      ) {
        return false;
      }
      if (
        frKurzform &&
        !data.frKurzform.toLowerCase().includes(frKurzform.toLowerCase())
      ) {
        return false;
      }
      if (
        typeof eintragGueltig === 'boolean' &&
        data.eintragGueltig !== eintragGueltig
      ) {
        return false;
      }
      if (typeof isEuEfta === 'boolean' && data.isEuEfta !== isEuEfta) {
        return false;
      }
      return true;
    };

    datasource.sort = this.sortSig() ?? null;

    if (paginator) {
      datasource.paginator = paginator;
    }

    if (sort) {
      datasource.sort = sort;
    }

    if (filter) {
      datasource.filter = filter.trim().toLowerCase();
    }
    return datasource;
  });

  constructor() {
    this.laenderStore.loadLaender$();

    effect(
      () => {
        const filterValues = this.filterFormChangedSig();
        this.countryDataSourceSig().filter = JSON.stringify(filterValues);
      },
      { allowSignalWrites: true },
    );
  }

  openDialog(land?: Land) {
    SachbearbeitungAppDialogEuEftaLaenderEditComponent.open(this.dialog, {
      land: land,
      laender: this.laenderStore.euEftaLaenderListViewSig() ?? [],
    })
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((land) => {
        if (!land) {
          return;
        }

        if (land.id) {
          this.laenderStore.updateLand$({ land, landId: land.id });
        } else {
          this.laenderStore.createLand$({ land });
        }
      });
  }
}
