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
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { debounceTime, distinctUntilChanged, map, startWith } from 'rxjs';

import { EuEftaLaenderStore } from '@dv/sachbearbeitung-app/data-access/eu-efta-laender';
import { SachbearbeitungAppDialogEuEftaLaenderEditComponent } from '@dv/sachbearbeitung-app/dialog/eu-efta-laender-edit';
import { Land } from '@dv/shared/model/gesuch';
import { SharedUiClearButtonComponent } from '@dv/shared/ui/clear-button';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { provideMaterialDefaultOptions } from '@dv/shared/util/form';

const sharedCountryKeyPrefix = 'shared.country.';

const INPUT_DELAY = 600;

@Component({
  selector: 'dv-sachbearbeitung-app-feature-administration-eu-efta-laender',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TranslatePipe,
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
  countryList = new FormControl<string[]>([]);

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
    eintragGueltig: [<string | null>null],
    isEuEfta: [<string | null>null],
  });

  private filterFormChangedSig = toSignal(
    this.filterForm.valueChanges.pipe(
      debounceTime(INPUT_DELAY),
      map(() => this.filterForm.getRawValue()),
    ),
  );

  private countryFilterChangedSig = toSignal(
    this.countryFilter.valueChanges.pipe(
      debounceTime(300),
      map((value) => ((value?.length ?? 0) < 3 ? null : value)),
      distinctUntilChanged(),
    ),
  );
  private countryListChangedSig = toSignal(this.countryList.valueChanges);
  private countryTranslationsSig = toSignal(
    this.translate.onLangChange.pipe(
      startWith({
        translations:
          this.translate.translations[
            this.translate.currentLang ?? this.translate.defaultLang
          ],
      }),
      map(({ translations }) =>
        Object.entries<string>(translations).filter(([key]) =>
          key.startsWith(sharedCountryKeyPrefix),
        ),
      ),
    ),
  );
  private countryListSig = computed(() => {
    const countryTranslations = this.countryTranslationsSig();
    return countryTranslations?.map(([key, value]) => ({
      key: key.replace(sharedCountryKeyPrefix, ''),
      value,
    }));
  });

  countryDataSourceSig = computed(() => {
    const allCountries = this.laenderStore.euEftaLaenderListViewSig() ?? [];
    const selected = this.countryList.value ?? [];
    const paginator = this.paginatorSig();
    const sort = this.sortSig();
    const filter = this.filterChangedSig();

    // todo:correct
    const filteredCountries =
      selected.length > 0
        ? allCountries.filter((c) => selected.includes(c.iso3code ?? ''))
        : allCountries;

    const datasource = new MatTableDataSource(filteredCountries);

    datasource.filterPredicate = (data, filter: string) => {
      const { iso3, deKurzform, frKurzform } = JSON.parse(filter);
      if (iso3 && !data.iso3code?.toLowerCase().includes(iso3.toLowerCase())) {
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

  hiddenCountriesSig = computed(() => {
    const filter = this.countryFilterChangedSig();
    const countryListSig = this.countryListSig();

    const hiddenCountries = filter
      ? countryListSig
          ?.filter(
            ({ value }) => !value.toLowerCase().includes(filter.toLowerCase()),
          )
          .map(({ key }) => key)
      : undefined;

    return (
      hiddenCountries?.reduce(
        (acc, key) => ({ ...acc, [key]: true }),
        {} as Record<string, boolean>,
      ) ?? {}
    );
  });

  constructor() {
    this.laenderStore.loadLaender$();

    effect(() => {
      const laender = this.laenderStore.euEftaLaenderListViewSig();

      if (!laender) {
        return;
      }

      // todo:correct
      this.countryList.patchValue(
        laender.filter((l) => l.isEuEfta).map((l) => l.iso3code ?? ''),
        { emitEvent: false },
      );
    });

    effect(
      () => {
        const filterValues = this.filterFormChangedSig();
        this.countryDataSourceSig().filter = JSON.stringify(filterValues);
        const selectedCountries = this.countryListChangedSig();

        if (!selectedCountries) {
          return;
        }
        //
        // const remainingCountries =
        //   untracked(this.laenderStore.euEftaLaenderListViewSig)?.filter(
        //     (l) => !selectedCountries.includes(l.land),
        //   ) ?? [];

        // this.laenderStore.saveLaender$([
        //   ...selectedCountries.map((land, iso3code) => ({
        //     land: land as Land,
        //     isEuEfta: true,
        //     iso3Code: iso3code as Iso3,
        //     deKurzform: deKurzform as DeKurzformObject,
        //     frKurzform: frKurzform as FrKurzformObject,
        //     eintragGueltig: true,
        //   })),
        //   ...remainingCountries.map(
        //     ({ land, iso3code, deKurzform, frKurzform }) => ({
        //       land,
        //       isEuEfta: false,
        //       iso3code,
        //       deKurzform,
        //       frKurzform,
        //       eintragGueltig: false,
        //     }),
        //   ),
        // ]);
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
        }

        this.laenderStore.createLand$({ land });
      });
  }
}
