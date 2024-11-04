import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  Signal,
  computed,
  effect,
  inject,
  input,
  output,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import {
  FormControl,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';
import { addYears, format } from 'date-fns';
import { startWith } from 'rxjs';

import { AusbildungStore } from '@dv/shared/data-access/ausbildung';
import { AusbildungsstaetteStore } from '@dv/shared/data-access/ausbildungsstaette';
import {
  SharedDataAccessGesuchEvents,
  selectSharedDataAccessGesuchCacheView,
} from '@dv/shared/data-access/gesuch';
import { GlobalNotificationStore } from '@dv/shared/data-access/global-notification';
import { selectLanguage } from '@dv/shared/data-access/language';
import { AusbildungsPensum, Ausbildungsstaette } from '@dv/shared/model/gesuch';
import { getTranslatableProp, isDefined } from '@dv/shared/model/type-util';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiRdIsPendingWithoutCachePipe } from '@dv/shared/ui/remote-data-pipe';
import { TranslatedPropertyPipe } from '@dv/shared/ui/translated-property-pipe';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
} from '@dv/shared/util/form';
import {
  createDateDependencyValidator,
  maxDateValidatorForLocale,
  minDateValidatorForLocale,
  onMonthYearInputBlur,
  parseableDateValidatorForLocale,
} from '@dv/shared/util/validator-date';
import { capitalized } from '@dv/shared/util-fn/string-helper';

@Component({
  selector: 'dv-shared-feature-ausbildung',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TranslateModule,
    MatFormFieldModule,
    MatButtonModule,
    MatInputModule,
    MatCheckboxModule,
    MatAutocompleteModule,
    MatSelectModule,
    TranslatedPropertyPipe,
    SharedUiRdIsPendingWithoutCachePipe,
    SharedUiLoadingComponent,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
  ],
  templateUrl: './shared-feature-ausbildung.component.html',
  providers: [AusbildungStore, AusbildungsstaetteStore],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureAusbildungComponent implements OnInit {
  private store = inject(Store);
  private formBuilder = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);
  private globalNotificationStore = inject(GlobalNotificationStore);
  readonly ausbildungspensumValues = Object.values(AusbildungsPensum);

  fallIdSig = input.required<string | null>();
  ausbildungSaved = output<void>();
  ausbildungStore = inject(AusbildungStore);
  ausbildungsstatteStore = inject(AusbildungsstaetteStore);
  form = this.formBuilder.group({
    ausbildungsort: [<string | undefined>undefined, [Validators.required]],
    isAusbildungAusland: [false, []],
    ausbildungsstaette: [<string | undefined>undefined, [Validators.required]],
    ausbildungsgang: [<string | undefined>undefined, [Validators.required]],
    fachrichtung: [<string | null>null, [Validators.required]],
    ausbildungNichtGefunden: [false, []],
    alternativeAusbildungsgang: [<string | undefined>undefined],
    alternativeAusbildungsstaette: [<string | undefined>undefined],
    ausbildungBegin: ['', []],
    ausbildungEnd: ['', []],
    pensum: this.formBuilder.control<AusbildungsPensum | null>(null, {
      validators: Validators.required,
    }),
  });

  private ausbildungsstaetteSig = toSignal(
    this.form.controls.ausbildungsstaette.valueChanges,
  );
  private ausbildungNichtGefundenChangedSig = toSignal(
    this.form.controls.ausbildungNichtGefunden.valueChanges,
  );
  private beginChangedSig = toSignal(
    this.form.controls.ausbildungBegin.valueChanges,
  );
  private endChangedSig = toSignal(
    this.form.controls.ausbildungEnd.valueChanges,
  );
  private isWritableSig = computed(() => {
    const {
      cache: { gesuch },
    } = this.gesuchViewSig();
    return (
      !isDefined(gesuch) ||
      this.ausbildungStore.ausbildungViewSig().writeableWhen ===
        gesuch.gesuchStatus
    );
  });

  languageSig = this.store.selectSignal(selectLanguage);
  gesuchViewSig = this.store.selectSignal(
    selectSharedDataAccessGesuchCacheView,
  );

  ausbildungsgangOptionsSig = computed(() => {
    const ausbildungsstaettes =
      this.ausbildungsstatteStore.ausbildungsstaetteViewSig();
    const language = this.languageSig();

    return (
      ausbildungsstaettes
        .find(
          (ausbildungsstaette) =>
            getTranslatableProp(ausbildungsstaette, 'name', language) ===
            this.ausbildungsstaetteSig(),
        )
        ?.ausbildungsgaenge?.map((ausbildungsgang) => {
          return {
            ...ausbildungsgang,
            translatedName: getTranslatableProp(
              ausbildungsgang,
              'bezeichnung',
              language,
            ),
          };
        }) ?? []
    );
  });
  ausbildungsstaettOptionsSig: Signal<
    (Ausbildungsstaette & { translatedName?: string })[]
  > = computed(() => {
    const currentAusbildungsstaette = this.ausbildungsstaetteSig();
    const ausbildungstaetten =
      this.ausbildungsstatteStore.ausbildungsstaetteViewSig() ?? [];
    const language = this.languageSig();
    const toReturn = currentAusbildungsstaette
      ? ausbildungstaetten.filter((ausbildungsstaette) => {
          return getTranslatableProp(ausbildungsstaette, 'name', language)
            ?.toLowerCase()
            .includes(currentAusbildungsstaette.toLowerCase());
        })
      : ausbildungstaetten;

    return toReturn.map((ausbildungsstaette) => {
      return {
        ...ausbildungsstaette,
        translatedName:
          getTranslatableProp(ausbildungsstaette, 'name', language) ??
          undefined,
      };
    });
  });

  constructor() {
    this.ausbildungsstatteStore.loadAusbildungsstaetten$();

    this.formUtils.registerFormForUnsavedCheck(this);
    const controls = this.form.controls;

    // abhaengige Validierung zuruecksetzen on valueChanges
    effect(
      () => {
        const value = this.ausbildungNichtGefundenChangedSig();
        const {
          alternativeAusbildungsgang,
          alternativeAusbildungsstaette,
          ausbildungsgang,
          ausbildungsstaette,
        } = this.form.controls;
        this.formUtils.setRequired(ausbildungsgang, !value);
        this.formUtils.setRequired(ausbildungsstaette, !value);
        this.formUtils.setRequired(alternativeAusbildungsgang, !!value);
        this.formUtils.setRequired(alternativeAusbildungsstaette, !!value);
      },
      { allowSignalWrites: true },
    );

    [
      { control: controls.ausbildungBegin, getAdditionalValidators: () => [] },
      {
        control: controls.ausbildungEnd,
        getAdditionalValidators: () => [
          minDateValidatorForLocale(
            this.languageSig(),
            this.ausbildungStore.ausbildungViewSig().minEndDatum,
            'monthYear',
          ),
          createDateDependencyValidator(
            'after',
            controls.ausbildungBegin,
            true,
            new Date(),
            this.languageSig(),
            'monthYear',
          ),
        ],
      },
    ].forEach(({ control, getAdditionalValidators }) =>
      effect(() => {
        control.clearValidators();
        control.addValidators([
          Validators.required,
          parseableDateValidatorForLocale(this.languageSig(), 'monthYear'),
          maxDateValidatorForLocale(
            this.languageSig(),
            addYears(new Date(), 100),
            'monthYear',
          ),
          ...getAdditionalValidators(),
        ]);
      }),
    );

    (['begin', 'end'] as const).forEach((type) =>
      effect(
        () => {
          this[`${type}ChangedSig`]();
          controls[`ausbildung${capitalized(type)}`].updateValueAndValidity();
        },
        { allowSignalWrites: true },
      ),
    );

    // fill form
    effect(
      () => {
        const ausbildung = {
          ...this.gesuchViewSig().cache.gesuch?.gesuchTrancheToWorkWith
            .gesuchFormular?.ausbildung,
        };
        const ausbildungstaetten =
          this.ausbildungsstatteStore.ausbildungsstaetteViewSig();

        if (ausbildung && ausbildungstaetten) {
          if (ausbildung.ausbildungBegin && ausbildung.ausbildungEnd) {
            ausbildung.ausbildungBegin = format(
              new Date(ausbildung.ausbildungBegin),
              'MM.yyyy',
            );
            ausbildung.ausbildungEnd = format(
              new Date(ausbildung.ausbildungEnd),
              'MM.yyyy',
            );
          }
          this.form.patchValue({
            ...ausbildung,
            ausbildungsgang: undefined,
          });
          const currentAusbildungsgang = ausbildung.ausbildungsgang;
          if (currentAusbildungsgang) {
            const ausbildungsstaette = ausbildungstaetten.find(
              (ausbildungsstaette) =>
                ausbildungsstaette.ausbildungsgaenge?.find(
                  (ausbildungsgang) =>
                    ausbildungsgang.id === currentAusbildungsgang.id,
                ),
            );
            const ausbildungsgang = ausbildungsstaette?.ausbildungsgaenge?.find(
              (ausbildungsgang) =>
                ausbildungsgang.id === currentAusbildungsgang.id,
            );
            this.form.patchValue({
              ausbildungsstaette:
                getTranslatableProp(
                  ausbildungsstaette,
                  'name',
                  this.languageSig(),
                ) ?? undefined,
              ausbildungsgang: ausbildungsgang?.id,
            });
          }
        } else {
          this.form.reset();
        }
      },
      { allowSignalWrites: true },
    );

    const isAusbildungAuslandSig = toSignal(
      this.form.controls.isAusbildungAusland.valueChanges.pipe(
        startWith(this.form.value.isAusbildungAusland),
      ),
    );
    effect(
      () => {
        const isAusbildungAusland = !!isAusbildungAuslandSig();

        if (isAusbildungAusland) {
          this.form.controls.ausbildungsort.reset();
        }

        this.formUtils.setDisabledState(
          this.form.controls.ausbildungsort,
          !this.isWritableSig() || isAusbildungAusland,
          false,
        );
      },
      { allowSignalWrites: true },
    );

    // When Staette null, disable gang
    const staetteSig = toSignal(
      this.form.controls.ausbildungsstaette.valueChanges.pipe(
        startWith(this.form.value.ausbildungsstaette),
      ),
    );
    effect(
      () => {
        const isWritable = this.isWritableSig();
        const staette = staetteSig();
        this.formUtils.setDisabledState(
          this.form.controls.ausbildungsgang,
          !isWritable || !staette,
          isWritable,
        );

        if (!staette) {
          this.form.controls.ausbildungsgang.reset();
          if (!this.form.controls.ausbildungNichtGefunden) {
            this.form.controls.fachrichtung.reset();
            this.form.controls.ausbildungsort.reset();
          }
        }
      },
      { allowSignalWrites: true },
    );

    // When Ausbildungsgang is null, disable fachrichtung. But only if ausbildungNichtGefunden is false
    const ausbildungsgangSig = toSignal(
      this.form.controls.ausbildungsgang.valueChanges.pipe(
        startWith(this.form.value.ausbildungsgang),
      ),
    );
    const ausbildungNichtGefundenSig = toSignal(
      this.form.controls.ausbildungNichtGefunden.valueChanges.pipe(
        startWith(this.form.value.ausbildungNichtGefunden),
      ),
    );
    effect(
      () => {
        const isWritable = this.isWritableSig();
        this.formUtils.setDisabledState(
          this.form.controls.fachrichtung,
          !isWritable ||
            (!ausbildungNichtGefundenSig() && !ausbildungsgangSig()),
          isWritable,
        );
      },
      { allowSignalWrites: true },
    );
  }

  ngOnInit() {
    if (!this.fallIdSig()) {
      this.store.dispatch(SharedDataAccessGesuchEvents.loadGesuch());
    }
  }

  handleGangChangedByUser() {
    this.form.controls.fachrichtung.reset();
    this.form.controls.ausbildungsort.reset();
  }

  handleManuellChangedByUser() {
    this.form.controls.ausbildungsstaette.reset();
    this.form.controls.alternativeAusbildungsstaette.reset();
    this.form.controls.ausbildungsgang.reset();
    this.form.controls.alternativeAusbildungsgang.reset();
    this.form.controls.fachrichtung.reset();
    this.form.controls.ausbildungsort.reset();
  }

  markDatesAsTouched() {
    [
      this.form.controls.ausbildungBegin,
      this.form.controls.ausbildungEnd,
    ].forEach((ctrl) => {
      ctrl.markAsTouched();
      ctrl.updateValueAndValidity();
    });
  }

  onDateBlur(ctrl: FormControl) {
    return onMonthYearInputBlur(ctrl, new Date(), this.languageSig());
  }

  handleSave() {
    this.form.markAllAsTouched();

    if (this.form.invalid) {
      return;
    }

    this.onDateBlur(this.form.controls.ausbildungBegin);
    this.onDateBlur(this.form.controls.ausbildungEnd);

    const { ausbildungsgang: ausbildungsgangId, ...formValues } =
      convertTempFormToRealValues(this.form, ['fachrichtung', 'pensum']);
    delete formValues.ausbildungsstaette;

    const ausbildungId =
      this.gesuchViewSig().cache.gesuch?.gesuchTrancheToWorkWith.gesuchFormular
        ?.ausbildung.id;
    const fallId = this.fallIdSig();
    const gesuchFallId = this.gesuchViewSig().cache.gesuch?.fallId;
    if (fallId) {
      this.ausbildungStore.createAusbildung$({
        ausbildung: {
          fallId,
          ...formValues,
          ausbildungsgangId,
        },
        onSuccess: () => {
          this.ausbildungSaved.emit();
        },
      });
    } else if (ausbildungId && gesuchFallId) {
      this.ausbildungStore.saveAusbildung$({
        ausbildungId,
        ausbildungUpdate: {
          ...formValues,
          id: ausbildungId,
          ausbildungsgangId,
          fallId: gesuchFallId,
        },
        onSuccess: () => {
          this.globalNotificationStore.createSuccessNotification({
            messageKey: 'shared.ausbildung.saved.success',
          });
        },
      });
    }

    this.form.markAsPristine();
  }
}
