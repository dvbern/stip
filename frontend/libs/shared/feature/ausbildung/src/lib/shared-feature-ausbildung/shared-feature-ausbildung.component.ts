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
import { TranslatePipe } from '@ngx-translate/core';
import { addYears } from 'date-fns';
import { startWith } from 'rxjs';

import { AusbildungStore } from '@dv/shared/data-access/ausbildung';
import { AusbildungsstaetteStore } from '@dv/shared/data-access/ausbildungsstaette';
import {
  SharedDataAccessGesuchEvents,
  selectSharedDataAccessGesuchCacheView,
  selectSharedDataAccessGesuchsView,
} from '@dv/shared/data-access/gesuch';
import { selectLanguage } from '@dv/shared/data-access/language';
import { GlobalNotificationStore } from '@dv/shared/global/notification';
import { SharedModelError } from '@dv/shared/model/error';
import { AusbildungsPensum, Ausbildungsstaette } from '@dv/shared/model/gesuch';
import { capitalized, getTranslatableProp } from '@dv/shared/model/type-util';
import {
  SharedPatternDocumentUploadComponent,
  createUploadOptionsFactory,
} from '@dv/shared/pattern/document-upload';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiFormReadonlyDirective,
} from '@dv/shared/ui/form';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
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
import { sharedUtilFnErrorTransformer } from '@dv/shared/util-fn/error-transformer';

const AusbildungRangeControls = ['ausbildungBegin', 'ausbildungEnd'] as const;
type AusbildungRangeControls = (typeof AusbildungRangeControls)[number];
type KnownErrors = Record<'periodeNotFound', true | null>;

const KnownErrorKeys = {
  'jakarta.validation.constraints.gesuch.create.gesuchsperiode.notfound.message':
    {
      periodeNotFound: true,
    },
} satisfies Record<string, KnownErrors>;

@Component({
  selector: 'dv-shared-feature-ausbildung',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TranslatePipe,
    MatFormFieldModule,
    MatButtonModule,
    MatInputModule,
    MatCheckboxModule,
    MatAutocompleteModule,
    MatSelectModule,
    TranslatedPropertyPipe,
    SharedUiFormReadonlyDirective,
    SharedUiRdIsPendingWithoutCachePipe,
    SharedUiLoadingComponent,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiMaxLengthDirective,
    SharedPatternDocumentUploadComponent,
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
  private languageSig = this.store.selectSignal(selectLanguage);
  private gesuchViewSig = this.store.selectSignal(
    selectSharedDataAccessGesuchsView,
  );
  private cachedGesuchViewSig = this.store.selectSignal(
    selectSharedDataAccessGesuchCacheView,
  );
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

  private createUploadOptionsSig = createUploadOptionsFactory(
    this.gesuchViewSig,
  );
  private usageTypeSig = computed(() => {
    const fallId = this.fallIdSig();
    const gesuchFallId = this.cachedGesuchViewSig().cache.gesuch?.fallId;

    return fallId
      ? { type: 'dialog' as const, fallId }
      : { type: 'gesuch-form' as const, fallId: gesuchFallId };
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
  isEditableSig = computed(() => {
    const { type } = this.usageTypeSig();
    const {
      cache: { gesuchFormular },
    } = this.cachedGesuchViewSig();

    return type === 'dialog' || gesuchFormular?.ausbildung?.editable;
  });

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
  ausbildungsstaettDocumentSig = this.createUploadOptionsSig(
    () => 'AUSBILDUNG_BESTAETIGUNG_AUSBILDUNGSSTAETTE',
  );
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

    effect(
      () => {
        this.beginChangedSig();

        this.ausbildungStore.resetAusbildungErrors();

        setTimeout(() => {
          this.withAusbildungRange((ctrl) => ctrl.updateValueAndValidity());
        });
      },
      { allowSignalWrites: true },
    );

    // fill form
    effect(
      () => {
        const ausbildung = {
          ...this.cachedGesuchViewSig().cache.gesuch?.gesuchTrancheToWorkWith
            .gesuchFormular?.ausbildung,
        };
        const ausbildungstaetten =
          this.ausbildungsstatteStore.ausbildungsstaetteViewSig();

        if (ausbildung && ausbildungstaetten) {
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
          !this.isEditableSig() || isAusbildungAusland,
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
        const isWritable = this.isEditableSig();
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
        const isWritable = this.isEditableSig();
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
    this.withAusbildungRange((ctrl) => {
      ctrl.markAsTouched();
      ctrl.updateValueAndValidity();
    });
  }

  onDateBlur(ctrl: FormControl) {
    onMonthYearInputBlur(ctrl, new Date(), this.languageSig());
  }

  handleSave() {
    this.form.markAllAsTouched();

    if (this.form.invalid) {
      return;
    }

    this.withAusbildungRange((ctrl) => this.onDateBlur(ctrl));

    const { ausbildungsgang: ausbildungsgangId, ...formValues } =
      convertTempFormToRealValues(this.form, ['fachrichtung', 'pensum']);
    delete formValues.ausbildungsstaette;

    const ausbildungId =
      this.cachedGesuchViewSig().cache.gesuch?.gesuchTrancheToWorkWith
        .gesuchFormular?.ausbildung.id;
    const ausbildungsgang = ausbildungsgangId ? { ausbildungsgangId } : {};
    const { type, fallId } = this.usageTypeSig();

    const onFailure = (error: unknown) => {
      const { parsedError, knownError } = parseKnownError(error);

      if (!knownError) {
        this.globalNotificationStore.handleHttpRequestFailed([parsedError]);
        return;
      }

      this.withAusbildungRange((ctrl) => ctrl.setErrors(knownError));
    };

    switch (type) {
      case 'dialog': {
        this.ausbildungStore.createAusbildung$({
          ausbildung: {
            fallId: fallId,
            ...formValues,
            ...ausbildungsgang,
          },
          onSuccess: () => {
            this.ausbildungSaved.emit();
          },
          onFailure,
        });
        break;
      }
      case 'gesuch-form': {
        if (!ausbildungId || !fallId) {
          return;
        }
        this.ausbildungStore.saveAusbildung$({
          ausbildungId,
          ausbildungUpdate: {
            ...formValues,
            id: ausbildungId,
            fallId: fallId,
            ...ausbildungsgang,
          },
          onSuccess: () => {
            this.globalNotificationStore.createSuccessNotification({
              messageKey: 'shared.ausbildung.saved.success',
            });
          },
          onFailure,
        });
        break;
      }
    }

    this.form.markAsPristine();
  }

  private withAusbildungRange = (
    fn: (control: FormControl, type: AusbildungRangeControls) => void,
  ) => {
    AusbildungRangeControls.forEach((type) =>
      fn(this.form.controls[type], type),
    );
  };
}

const isKnownErrorKey = (key: string): key is keyof typeof KnownErrorKeys =>
  key in KnownErrorKeys;

const parseKnownError = (
  error: unknown,
): { knownError?: KnownErrors; parsedError: SharedModelError } => {
  const parsedError = sharedUtilFnErrorTransformer(error);
  if (parsedError.type === 'validationError') {
    const key = parsedError.messageKey;
    if (isKnownErrorKey(key)) {
      return { knownError: KnownErrorKeys[key], parsedError };
    }
  }

  return { parsedError };
};