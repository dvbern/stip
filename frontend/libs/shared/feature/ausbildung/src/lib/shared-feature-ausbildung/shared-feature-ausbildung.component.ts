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
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';
import { addYears } from 'date-fns';
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
  Ausbildungsstaette,
  GesuchsperiodeSelectErrorType,
} from '@dv/shared/model/gesuch';
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
import { SharedUiInfoDialogDirective } from '@dv/shared/ui/info-dialog';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import {
  SharedUiRdIsPendingPipe,
  SharedUiRdIsPendingWithoutCachePipe,
} from '@dv/shared/ui/remote-data-pipe';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
  provideMaterialDefaultOptions,
} from '@dv/shared/util/form';
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
    TranslatePipe,
    MatFormFieldModule,
    MatButtonModule,
    MatInputModule,
    MatCheckboxModule,
    MatAutocompleteModule,
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
    besuchtBMS: [false, []],
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
  private ausbildungsgangChangedSig = toSignal(
    this.form.controls.ausbildungsgang.valueChanges,
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

  showBesuchtBMS = computed(() => {
    const ausbildungsgangId = this.ausbildungsgangChangedSig();
    if (!ausbildungsgangId) return false;

    const ausbildungsgange = untracked(this.ausbildungsgangOptionsSig);
    const gang = ausbildungsgange.find(
      (ausbildungsgang) => ausbildungsgang.id === ausbildungsgangId,
    );
    if (!gang) return false;

    const bfs = gang.bildungskategorie.bfs;

    return bfs === 4 || bfs === 5;
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
        ausbildungsgang,
        ausbildungsstaette,
      } = this.form.controls;
      this.formUtils.setRequired(ausbildungsgang, !value);
      this.formUtils.setRequired(ausbildungsstaette, !value);
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
          ['ausbildungNichtGefunden', 'ausbildungsstaette'],
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
        this.form.controls.ausbildungsort.reset();
      }

      this.formUtils.setDisabledState(
        this.form.controls.ausbildungsort,
        !this.isEditableSig() || isAusbildungAusland,
        false,
      );
    });

    // When Staette null, disable gang
    const staetteSig = toSignal(
      this.form.controls.ausbildungsstaette.valueChanges.pipe(
        startWith(this.form.value.ausbildungsstaette),
      ),
    );
    effect(() => {
      const isWritable = this.isEditableSig();
      const staette = staetteSig();
      this.formUtils.setDisabledState(
        this.form.controls.ausbildungsgang,
        !isWritable || !staette,
        isWritable,
      );

      if (!staette) {
        this.form.controls.ausbildungsgang.reset();
        this.form.controls.besuchtBMS.reset();
        if (!this.form.controls.ausbildungNichtGefunden) {
          this.form.controls.fachrichtung.reset();
          this.form.controls.ausbildungsort.reset();
        }
      }
    });

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
    effect(() => {
      const isWritable = this.isEditableSig();
      this.formUtils.setDisabledState(
        this.form.controls.fachrichtung,
        !isWritable || (!ausbildungNichtGefundenSig() && !ausbildungsgangSig()),
        isWritable,
      );
    });
  }

  ngOnInit() {
    if (!this.fallIdSig()) {
      this.store.dispatch(SharedDataAccessGesuchEvents.loadGesuch());
    }
  }

  handleGangChangedByUser() {
    this.form.controls.fachrichtung.reset();
    this.form.controls.ausbildungsort.reset();
    this.form.controls.besuchtBMS.reset();
  }

  handleManuellChangedByUser() {
    this.form.controls.ausbildungsstaette.reset();
    this.form.controls.alternativeAusbildungsstaette.reset();
    this.form.controls.ausbildungsgang.reset();
    this.form.controls.alternativeAusbildungsgang.reset();
    this.form.controls.fachrichtung.reset();
    this.form.controls.ausbildungsort.reset();
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

    const { ausbildungsgang: ausbildungsgangId, ...formValues } =
      convertTempFormToRealValues(this.form, ['fachrichtung', 'pensum']);
    delete formValues.ausbildungsstaette;

    const ausbildungId =
      this.cachedGesuchViewSig().cache.gesuch?.gesuchTrancheToWorkWith
        .gesuchFormular?.ausbildung.id;
    const ausbildungsgang = ausbildungsgangId ? { ausbildungsgangId } : {};
    const { type, fallId } = this.usageTypeSig();

    switch (type) {
      case 'dialog': {
        this.ausbildungStore.createAusbildung$({
          ausbildung: {
            fallId: fallId,
            ...formValues,
            ...ausbildungsgang,
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
            ...ausbildungsgang,
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

  private withAusbildungRange = (
    fn: (control: FormControl, type: AusbildungRangeControls) => void,
  ) => {
    AusbildungRangeControls.forEach((type) =>
      fn(this.form.controls[type], type),
    );
  };
}
