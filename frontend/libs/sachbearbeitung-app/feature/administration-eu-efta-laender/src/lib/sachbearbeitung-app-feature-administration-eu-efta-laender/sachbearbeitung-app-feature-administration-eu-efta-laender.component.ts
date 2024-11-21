import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
  untracked,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { debounceTime, distinctUntilChanged, map, startWith } from 'rxjs';

import { EuEftaLaenderStore } from '@dv/sachbearbeitung-app/data-access/eu-efta-laender';
import { Land } from '@dv/shared/model/gesuch';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import { SharedUiRdIsPendingPipe } from '@dv/shared/ui/remote-data-pipe';
import { provideMaterialDefaultOptions } from '@dv/shared/util/form';

const sharedCountryKeyPrefix = 'shared.country.';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-administration-eu-efta-laender',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TranslatePipe,
    MatFormFieldModule,
    MatInputModule,
    MatListModule,
    SharedUiLoadingComponent,
    SharedUiRdIsPendingPipe,
    SharedUiMaxLengthDirective,
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
  laenderStore = inject(EuEftaLaenderStore);
  countryFilter = new FormControl<string | null>(null);
  countryList = new FormControl<string[]>([]);

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

      this.countryList.patchValue(
        laender.filter((l) => l.isEuEfta).map((l) => l.land),
        { emitEvent: false },
      );
    });

    effect(
      () => {
        const selectedCountries = this.countryListChangedSig();

        if (!selectedCountries) {
          return;
        }

        const remainingCountries =
          untracked(this.laenderStore.euEftaLaenderListViewSig)?.filter(
            (l) => !selectedCountries.includes(l.land),
          ) ?? [];

        this.laenderStore.saveLaender$([
          ...selectedCountries.map((land) => ({
            land: land as Land,
            isEuEfta: true,
          })),
          ...remainingCountries.map(({ land }) => ({
            land,
            isEuEfta: false,
          })),
        ]);
      },
      { allowSignalWrites: true },
    );
  }
}
