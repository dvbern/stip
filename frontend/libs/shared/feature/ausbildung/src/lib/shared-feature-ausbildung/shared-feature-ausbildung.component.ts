import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  computed,
  effect,
  inject,
  input,
  output,
  signal,
  untracked,
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
import { provideDateFnsAdapter } from '@angular/material-date-fns-adapter';
import { RouterLink } from '@angular/router';
import { TranslocoPipe } from '@jsverse/transloco';
import { Store } from '@ngrx/store';
import { addYears } from 'date-fns';
import { diff } from 'json-diff-ts';
import { startWith } from 'rxjs';

import { AusbildungStore } from '@dv/shared/data-access/ausbildung';
import { AusbildungsstaetteStore } from '@dv/shared/data-access/ausbildungsstaette';
import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import {
  SharedDataAccessGesuchEvents,
  selectSharedDataAccessGesuchCacheView,
  selectSharedDataAccessGesuchsView,
} from '@dv/shared/data-access/gesuch';
import { selectLanguage } from '@dv/shared/data-access/language';
import { GlobalNotificationStore } from '@dv/shared/global/notification';
import {
  AusbildungsPensum,
  GesuchsperiodeSelectErrorType,
  Plz,
} from '@dv/shared/model/gesuch';
import {
  capitalized,
  compareById,
  getTranslatableProp,
} from '@dv/shared/model/type-util';
import {
  SharedPatternDocumentUploadComponent,
  createUploadOptionsFactory,
} from '@dv/shared/pattern/document-upload';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiFormReadonlyDirective,
} from '@dv/shared/ui/form';
import { SharedUiInfoDialogDirective } from '@dv/shared/ui/info-dialog';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import { SharedUiPlzOrtAutocompleteDirective } from '@dv/shared/ui/plz-ort-autocomplete';
import {
  SharedUiRdIsPendingPipe,
  SharedUiRdIsPendingWithoutCachePipe,
} from '@dv/shared/ui/remote-data-pipe';
import { SharedUiSelectSearchComponent } from '@dv/shared/ui/select-search';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
  provideMaterialDefaultOptions,
  updateVisbilityAndDisbledState,
} from '@dv/shared/util/form';
import { sortListByText } from '@dv/shared/util/table';
import {
  createDateDependencyValidator,
  maxDateValidatorForLocale,
  minDateValidatorForLocale,
  onMonthYearInputBlur,
  parseableDateValidatorForLocale,
} from '@dv/shared/util/validator-date';

const AusbildungRangeControls = ['ausbildungBegin', 'ausbildungEnd'] as const;
type AusbildungRangeControls = (typeof AusbildungRangeControls)[number];

const gesuchsPeriodenSelectErrorMap: Record<
  GesuchsperiodeSelectErrorType,
  { [key: string]: true }
> = {
  INAKTIVE_PERIODE_GEFUNDEN: { inactivePeriod: true },
  KEINE_AKTIVE_PERIODE_GEFUNDEN: { periodeNotFound: true },
  PERIODE_IN_ENTWURF_GEFUNDEN: { periodeIsDraft: true },
};

@Component({
  selector: 'dv-shared-feature-ausbildung',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink,
    TranslocoPipe,
    MatFormFieldModule,
    MatButtonModule,
    MatInputModule,
    MatAutocompleteModule,
    MatCheckboxModule,
    MatSelectModule,
    SharedUiInfoDialogDirective,
    SharedUiFormReadonlyDirective,
    SharedUiRdIsPendingPipe,
    SharedUiRdIsPendingWithoutCachePipe,
    SharedUiLoadingComponent,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiMaxLengthDirective,
    SharedPatternDocumentUploadComponent,
    SharedUiSelectSearchComponent,
    SharedUiPlzOrtAutocompleteDirective,
  ],
  templateUrl: './shared-feature-ausbildung.component.html',
  providers: [
    AusbildungStore,
    AusbildungsstaetteStore,
    provideDateFnsAdapter(),
    provideMaterialDefaultOptions({
      subscriptSizing: 'dynamic',
    }),
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureAusbildungComponent implements OnInit {
  private store = inject(Store);
  private formBuilder = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);
  private einreichenStore = inject(EinreichenStore);
  private globalNotificationStore = inject(GlobalNotificationStore);
  private gesuchViewSig = this.store.selectSignal(
    selectSharedDataAccessGesuchsView,
  );
  private cachedGesuchViewSig = this.store.selectSignal(
    selectSharedDataAccessGesuchCacheView,
  );
  readonly ausbildungspensumValues = Object.values(AusbildungsPensum);

  fallIdSig = input.required<string | null>();
  ausbildungSaved = output<void>();
  languageSig = this.store.selectSignal(selectLanguage);

  ausbildungStore = inject(AusbildungStore);
  ausbildungsstatteStore = inject(AusbildungsstaetteStore);

  form = this.formBuilder.group({
    ausbildungsortPlzOrt: this.formBuilder.group({
      plz: [<string | undefined>undefined, [Validators.required]],
      ort: [<string | undefined>undefined, [Validators.required]],
    }),
    isAusbildungAusland: [false, []],
    ausbildungsstaetteId: [
      <string | undefined>undefined,
      [Validators.required],
    ],
    ausbildungsgangId: [<string | undefined>undefined, [Validators.required]],
    besuchtBMS: [false, []],
    fachrichtungBerufsbezeichnung: [
      <string | undefined>undefined,
      [Validators.required],
    ],
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
  private ausbildungsstaetteIdSig = toSignal(
    this.form.controls.ausbildungsstaetteId.valueChanges,
  );
  private ausbildungsgangIdSig = toSignal(
    this.form.controls.ausbildungsgangId.valueChanges,
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

  plzValues?: Plz[];
  isEditableSig = computed(() => {
    const { type } = this.usageTypeSig();
    const {
      cache: { gesuchFormular },
    } = this.cachedGesuchViewSig();

    return type === 'dialog' || gesuchFormular?.ausbildung?.editable;
  });

  private currentAusbildungsstaetteSig = computed(() => {
    const ausbildungsstaetteId = this.ausbildungsstaetteIdSig();
    const ausbildungsstaettes =
      this.ausbildungsstatteStore.ausbildungsstaettenWithAusbildungsgaengeViewSig();

    return ausbildungsstaettes.find(
      (ausbildungsstaette) => ausbildungsstaette.id === ausbildungsstaetteId,
    );
  });

  private currentAusbildungsgangSig = computed(() => {
    const ausbildungsgangId = this.ausbildungsgangIdSig();
    return this.currentAusbildungsstaetteSig()?.ausbildungsgaenge.find(
      (ausbildungsgang) => ausbildungsgang.id === ausbildungsgangId,
    );
  });

  ausbildungsstaettenOptionsSig = computed(
    () => {
      const currentAusbildungsstaette = this.currentAusbildungsstaetteSig();
      const isNew = this.fallIdSig();
      const ausbildungsstaetten =
        this.ausbildungsstatteStore.ausbildungsstaettenWithAusbildungsgaengeViewSig();

      if (!ausbildungsstaetten || (!currentAusbildungsstaette && !isNew)) {
        return [];
      }

      return ausbildungsstaetten
        .map((ausbildungsstaette) => ({
          ...ausbildungsstaette,
          invalid:
            !ausbildungsstaette.aktiv &&
            ausbildungsstaette.id !== currentAusbildungsstaette?.id,
          disabled: !ausbildungsstaette.aktiv,
        }))
        .filter((ausbildungsstaette) =>
          ausbildungsstaette.ausbildungsgaenge.some((gang) => gang.aktiv),
        );
    },
    {
      equal: areObjectsEqual,
    },
  );

  ausbildungsgangOptionsSig = computed(
    () => {
      const currentAusbildungsstaette = this.currentAusbildungsstaetteSig();
      const currentAusbildungsgang = this.currentAusbildungsgangSig();
      const ausbildung = {
        ...this.cachedGesuchViewSig().cache.gesuch?.gesuchTrancheToWorkWith
          .gesuchFormular?.ausbildung,
      };
      const language = this.languageSig();

      const ausbildungsgaenge =
        currentAusbildungsstaette?.ausbildungsgaenge
          ?.filter(
            (ausbildungsgang) =>
              ausbildungsgang.aktiv ||
              ausbildungsgang.id === ausbildung.ausbildungsgang?.id,
          )
          .map((ausbildungsgang) => {
            return {
              ...ausbildungsgang,
              translatedName: getTranslatableProp(
                ausbildungsgang,
                'bezeichnung',
                language,
              ),
              testId: ausbildungsgang.bezeichnungDe,
              invalid:
                !ausbildungsgang.aktiv &&
                ausbildungsgang.id !== currentAusbildungsgang?.id,
              disabled: !ausbildungsgang.aktiv,
              displayValueDe: ausbildungsgang.bezeichnungDe,
              displayValueFr: ausbildungsgang.bezeichnungFr,
            };
          }) ?? [];
      return sortListByText(ausbildungsgaenge, (item) => item.translatedName);
    },
    {
      equal: areObjectsEqual,
    },
  );

  zusatzfrageSig = computed(() => {
    const newAusbildungsgang = this.currentAusbildungsgangSig();
    if (!newAusbildungsgang) return undefined;

    return newAusbildungsgang.zusatzfrage;
  });

  showBesuchtBMS = computed(() => {
    const newAusbildungsgang = this.currentAusbildungsgangSig();
    if (!newAusbildungsgang) return false;

    const ausbildungsgange = untracked(this.ausbildungsgangOptionsSig);
    const gang = ausbildungsgange.find(
      (ausbildungsgang) => ausbildungsgang.id === newAusbildungsgang.id,
    );

    return gang?.askForBerufsmaturitaet;
  });

  ausbildungsstaettDocumentSig = this.createUploadOptionsSig(
    () => 'AUSBILDUNG_BESTAETIGUNG_AUSBILDUNGSSTAETTE',
  );

  hiddenFieldsSetSig = signal(new Set<FormControl>());
  compareById = compareById;

  constructor() {
    this.ausbildungsstatteStore.loadAusbildungsstaetten$();

    this.formUtils.registerFormForUnsavedCheck(this);
    this.formUtils.observeInvalidFieldsAndMarkControls(
      this.einreichenStore.invalidFormularControlsSig,
      this.form,
    );
    const controls = this.form.controls;

    // abhaengige Validierung zuruecksetzen on valueChanges
    effect(() => {
      const value = this.ausbildungNichtGefundenChangedSig();
      const {
        alternativeAusbildungsgang,
        alternativeAusbildungsstaette,
        ausbildungsgangId,
        ausbildungsstaetteId,
      } = this.form.controls;
      this.formUtils.setRequired(ausbildungsgangId, !value);
      this.formUtils.setRequired(ausbildungsstaetteId, !value);
      this.formUtils.setRequired(alternativeAusbildungsgang, !!value);
      this.formUtils.setRequired(alternativeAusbildungsstaette, !!value);
    });

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
      effect(() => {
        this[`${type}ChangedSig`]();
        controls[`ausbildung${capitalized(type)}`].updateValueAndValidity();
      }),
    );

    effect(() => {
      this.beginChangedSig();

      this.ausbildungStore.resetAusbildungErrors();

      setTimeout(() => {
        this.withAusbildungRange((ctrl) => ctrl.updateValueAndValidity());
      });
    });

    effect(() => {
      const error = this.ausbildungStore.ausbildungCreateErrorResponseViewSig();

      if (error) {
        this.withAusbildungRange((ctrl) =>
          ctrl.setErrors(gesuchsPeriodenSelectErrorMap[error.type]),
        );
      }
    });

    // fill form
    effect(() => {
      const ausbildung = {
        ...this.cachedGesuchViewSig().cache.gesuch?.gesuchTrancheToWorkWith
          .gesuchFormular?.ausbildung,
      };
      const ausbildungstaetten =
        this.ausbildungsstatteStore.ausbildungsstaetteViewSig();

      if (ausbildung && ausbildungstaetten) {
        this.form.patchValue({
          ...ausbildung,
          ausbildungsortPlzOrt: {
            plz: ausbildung.ausbildungsortPLZ,
            ort: ausbildung.ausbildungsort,
          },
        });
        const currentAusbildungsgang = ausbildung.ausbildungsgang;
        if (currentAusbildungsgang) {
          const ausbildungsstaette = ausbildungstaetten.find(
            (ausbildungsstaette) =>
              ausbildungsstaette.id ===
              ausbildung.ausbildungsgang?.ausbildungsstaette?.id,
          );
          const ausbildungsgang = ausbildungsstaette?.ausbildungsgaenge?.find(
            (ausbildungsgang) =>
              ausbildungsgang.id === currentAusbildungsgang.id,
          );
          this.form.patchValue({
            ausbildungsstaetteId:
              ausbildung.ausbildungsgang?.ausbildungsstaette?.id,
            ausbildungsgangId: ausbildungsgang?.id,
          });
        }
      }
    });

    effect(() => {
      const { readonly } = this.cachedGesuchViewSig();
      const { invalidFormularProps } = this.einreichenStore.validationViewSig();
      const nichtGefunden = this.ausbildungNichtGefundenChangedSig();

      if (!readonly && nichtGefunden) {
        const { errors } = this.form.controls.ausbildungNichtGefunden;
        if (errors) {
          delete errors['requiredOff'];
          this.form.controls.ausbildungNichtGefunden.setErrors(errors);
        }
        this.formUtils.invalidateControlIfValidationFails(
          this.form,
          ['ausbildungNichtGefunden', 'ausbildungsstaetteId'],
          {
            specialValidationErrors:
              invalidFormularProps.specialValidationErrors,
            beforeInvalidate: () => {
              untracked(() => {
                this.form.controls.ausbildungNichtGefunden.setErrors({
                  requiredOff: true,
                });
              });
            },
          },
        );
      }
    });

    const isAusbildungAuslandSig = toSignal(
      this.form.controls.isAusbildungAusland.valueChanges.pipe(
        startWith(this.form.value.isAusbildungAusland),
      ),
    );
    effect(() => {
      const isAusbildungAusland = !!isAusbildungAuslandSig();

      if (isAusbildungAusland) {
        this.form.controls.ausbildungsortPlzOrt.controls.plz.reset();
        this.form.controls.ausbildungsortPlzOrt.controls.ort.reset();
      }

      this.formUtils.setDisabledState(
        this.form.controls.ausbildungsortPlzOrt.controls.plz,
        !this.isEditableSig() || isAusbildungAusland,
        false,
      );

      this.formUtils.setDisabledState(
        this.form.controls.ausbildungsortPlzOrt.controls.ort,
        !this.isEditableSig() || isAusbildungAusland,
        false,
      );
    });

    // When Staette null, disable gang
    const staetteSig = toSignal(
      this.form.controls.ausbildungsstaetteId.valueChanges.pipe(
        startWith(this.form.value.ausbildungsstaetteId),
      ),
    );
    effect(() => {
      const isWritable = this.isEditableSig();
      const staette = staetteSig();
      this.formUtils.setDisabledState(
        this.form.controls.ausbildungsgangId,
        !isWritable || !staette,
        isWritable,
      );

      if (!staette) {
        this.form.controls.ausbildungsgangId.reset();
        this.form.controls.besuchtBMS.reset();
        if (!this.form.controls.ausbildungNichtGefunden) {
          this.form.controls.fachrichtungBerufsbezeichnung.reset();
          this.form.controls.ausbildungsortPlzOrt.controls.plz.reset();
          this.form.controls.ausbildungsortPlzOrt.controls.ort.reset();
        }
      }
    });

    // When Ausbildungsgang is null, disable fachrichtung. But only if ausbildungNichtGefunden is false
    const ausbildungNichtGefundenSig = toSignal(
      this.form.controls.ausbildungNichtGefunden.valueChanges.pipe(
        startWith(this.form.value.ausbildungNichtGefunden),
      ),
    );
    effect(() => {
      const isWritable = this.isEditableSig();
      this.formUtils.setDisabledState(
        this.form.controls.fachrichtungBerufsbezeichnung,
        !isWritable ||
          (!ausbildungNichtGefundenSig() && !this.currentAusbildungsgangSig()),
        isWritable,
      );
    });

    effect(() => {
      const { readonly } = this.cachedGesuchViewSig();
      const ausbildungsgang = this.currentAusbildungsgangSig();
      updateVisbilityAndDisbledState({
        hiddenFieldsSetSig: this.hiddenFieldsSetSig,
        formControl: this.form.controls.fachrichtungBerufsbezeichnung,
        visible: !!ausbildungsgang?.zusatzfrage,
        disabled: readonly,
      });
      this.form.controls.fachrichtungBerufsbezeichnung.updateValueAndValidity();
    });
  }

  ngOnInit() {
    if (!this.fallIdSig()) {
      this.store.dispatch(SharedDataAccessGesuchEvents.loadGesuch());
    }
  }

  handleGangChangedByUser() {
    this.form.controls.fachrichtungBerufsbezeichnung.reset();
    this.form.controls.ausbildungsortPlzOrt.controls.plz.reset();
    this.form.controls.ausbildungsortPlzOrt.controls.ort.reset();
    this.form.controls.besuchtBMS.reset();
  }

  handleManuellChangedByUser() {
    this.form.controls.ausbildungsstaetteId.reset();
    this.form.controls.ausbildungsgangId.reset();
    this.form.controls.alternativeAusbildungsstaette.reset();
    this.form.controls.alternativeAusbildungsgang.reset();
    this.form.controls.fachrichtungBerufsbezeichnung.reset();
    this.form.controls.ausbildungsortPlzOrt.controls.plz.reset();
    this.form.controls.ausbildungsortPlzOrt.controls.ort.reset();
    this.form.controls.besuchtBMS.reset();
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

    const {
      ausbildungsgangId,
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      ausbildungsstaetteId: _,
      ausbildungsortPlzOrt,
      ...formValues
    } = convertTempFormToRealValues(this.form, ['pensum']);

    const ausbildungId =
      this.cachedGesuchViewSig().cache.gesuch?.gesuchTrancheToWorkWith
        .gesuchFormular?.ausbildung.id;
    const { type, fallId } = this.usageTypeSig();

    switch (type) {
      case 'dialog': {
        this.ausbildungStore.createAusbildung$({
          ausbildung: {
            fallId: fallId,
            ...formValues,
            ausbildungsgangId,
            ausbildungsort: ausbildungsortPlzOrt.ort,
            ausbildungsortPLZ: ausbildungsortPlzOrt.plz,
          },
          onSuccess: (response) => {
            if (response.ausbildung) {
              this.ausbildungSaved.emit();
            }
          },
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
            ausbildungsgangId,
            ausbildungsort: ausbildungsortPlzOrt.ort,
            ausbildungsortPLZ: ausbildungsortPlzOrt.plz,
          },
          onSuccess: () => {
            this.globalNotificationStore.createSuccessNotification({
              messageKey: 'shared.ausbildung.saved.success',
            });
            this.store.dispatch(SharedDataAccessGesuchEvents.loadGesuch());
          },
        });
        break;
      }
    }

    this.form.markAsPristine();
  }

  isActive(obj: { aktiv: boolean }) {
    return obj.aktiv;
  }

  private withAusbildungRange = (
    fn: (control: FormControl, type: AusbildungRangeControls) => void,
  ) => {
    AusbildungRangeControls.forEach((type) =>
      fn(this.form.controls[type], type),
    );
  };
}

const areObjectsEqual = <T>(a: T, b: T): boolean => {
  return diff(a, b).length === 0;
};
