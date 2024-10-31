import { DatePipe } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  OnInit,
  Signal,
  computed,
  effect,
  inject,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import {
  FormControl,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MaskitoDirective } from '@maskito/angular';
import { Store } from '@ngrx/store';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { addYears } from 'date-fns';
import { Subject, startWith } from 'rxjs';

import { AusbildungsstaetteStore } from '@dv/shared/data-access/ausbildungsstaette';
import { selectLanguage } from '@dv/shared/data-access/language';
import { SharedEventGesuchFormEducation } from '@dv/shared/event/gesuch-form-education';
import {
  AusbildungsPensum,
  Ausbildungsgang,
  Ausbildungsstaette,
} from '@dv/shared/model/gesuch';
import { AUSBILDUNG } from '@dv/shared/model/gesuch-form';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiFormReadonlyDirective,
  SharedUiFormZuvorHintComponent,
  SharedUiZuvorHintDirective,
} from '@dv/shared/ui/form';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiRdIsPendingPipe } from '@dv/shared/ui/remote-data-pipe';
import { SharedUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import { SharedUiTranslateChangePipe } from '@dv/shared/ui/translate-change';
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

import { selectSharedFeatureGesuchFormEducationView } from './shared-feature-gesuch-form-education.selector';

@Component({
  selector: 'dv-shared-feature-gesuch-form-education',
  standalone: true,
  imports: [
    DatePipe,
    TranslateModule,
    ReactiveFormsModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    MatFormFieldModule,
    MatButtonModule,
    MatInputModule,
    MatSelectModule,
    MatCheckboxModule,
    MatAutocompleteModule,
    MaskitoDirective,
    SharedUiRdIsPendingPipe,
    SharedUiStepFormButtonsComponent,
    SharedUiLoadingComponent,
    SharedUiFormReadonlyDirective,
    SharedUiZuvorHintDirective,
    SharedUiFormZuvorHintComponent,
    SharedUiTranslateChangePipe,
    TranslatedPropertyPipe,
  ],
  templateUrl: './shared-feature-gesuch-form-education.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormEducationComponent implements OnInit {
  private elementRef = inject(ElementRef);
  private store = inject(Store);
  private formBuilder = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);

  readonly ausbildungspensumValues = Object.values(AusbildungsPensum);

  translate = inject(TranslateService);
  ausbildungsstatteStore = inject(AusbildungsstaetteStore);
  languageSig = this.store.selectSignal(selectLanguage);

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

  viewSig = this.store.selectSignal(selectSharedFeatureGesuchFormEducationView);
  gotReenabled$ = new Subject<object>();

  ausbildungsstaetteSig = toSignal(
    this.form.controls.ausbildungsstaette.valueChanges,
  );
  ausbildungsstaettOptionsSig: Signal<
    (Ausbildungsstaette & { translatedName?: string })[]
  > = computed(() => {
    const currentAusbildungsstaette = this.ausbildungsstaetteSig();
    const ausbildungsstaettes =
      this.ausbildungsstatteStore.ausbildungsstaetteViewSig();
    const toReturn = currentAusbildungsstaette
      ? ausbildungsstaettes.filter((ausbildungsstaette) => {
          return this.getTranslatedAusbildungstaetteName(ausbildungsstaette)
            ?.toLowerCase()
            .includes(currentAusbildungsstaette.toLowerCase());
        })
      : ausbildungsstaettes;

    return toReturn.map((ausbildungsstaette) => {
      return {
        ...ausbildungsstaette,
        translatedName:
          this.getTranslatedAusbildungstaetteName(ausbildungsstaette),
      };
    });
  });
  ausbildungNichtGefundenChangedSig = toSignal(
    this.form.controls.ausbildungNichtGefunden.valueChanges,
  );
  startChangedSig = toSignal(this.form.controls.ausbildungBegin.valueChanges);
  endChangedSig = toSignal(this.form.controls.ausbildungEnd.valueChanges);

  ausbildungsgangOptionsSig = computed(() => {
    const ausbildungsstaettes =
      this.ausbildungsstatteStore.ausbildungsstaetteViewSig();

    return (
      ausbildungsstaettes
        .find(
          (ausbildungsstaette) =>
            this.getTranslatedAusbildungstaetteName(ausbildungsstaette) ===
            this.ausbildungsstaetteSig(),
        )
        ?.ausbildungsgaenge?.map((ausbildungsgang) => {
          return {
            ...ausbildungsgang,
            translatedName:
              this.getTranslatedAusbildungsgangName(ausbildungsgang),
          };
        }) ?? []
    );
  });
  previousAusbildungsstaetteSig = computed(() => {
    const { formChanges } = this.viewSig();
    const ausbildungsstaettes =
      this.ausbildungsstatteStore.ausbildungsstaetteViewSig();
    const changedAusbildungsgang = formChanges?.ausbildungsgang;

    if (changedAusbildungsgang && ausbildungsstaettes) {
      return ausbildungsstaettes.find((ausbildungsstaette) =>
        ausbildungsstaette.ausbildungsgaenge?.find(
          (ausbildungsgang) => changedAusbildungsgang.id === ausbildungsgang.id,
        ),
      );
    }
    return undefined;
  });

  private gotReenabledSig = toSignal(this.gotReenabled$);

  constructor() {
    this.formUtils.registerFormForUnsavedCheck(this);

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
    effect(() => {
      const { minAusbildungBeginDate } = this.viewSig();
      const validators: ValidatorFn[] = [
        Validators.required,
        parseableDateValidatorForLocale(this.languageSig(), 'monthYear'),
        maxDateValidatorForLocale(
          this.languageSig(),
          addYears(new Date(), 100),
          'monthYear',
        ),
      ];

      if (minAusbildungBeginDate) {
        validators.push(
          minDateValidatorForLocale(
            this.languageSig(),
            minAusbildungBeginDate,
            'monthYear',
            {
              errorType: 'minDateLebenslauf',
            },
          ),
        );
      }
      this.form.controls.ausbildungBegin.clearValidators();
      this.form.controls.ausbildungBegin.addValidators(validators);
    });
    effect(() => {
      const { minEndDatum } = this.viewSig();
      const validators: ValidatorFn[] = [
        Validators.required,
        parseableDateValidatorForLocale(this.languageSig(), 'monthYear'),
        maxDateValidatorForLocale(
          this.languageSig(),
          addYears(new Date(), 100),
          'monthYear',
        ),
        minDateValidatorForLocale(this.languageSig(), minEndDatum, 'monthYear'),
        createDateDependencyValidator(
          'after',
          this.form.controls.ausbildungBegin,
          true,
          new Date(),
          this.languageSig(),
          'monthYear',
        ),
      ];

      this.form.controls.ausbildungEnd.clearValidators();
      this.form.controls.ausbildungEnd.addValidators(validators);
    });
    effect(
      () => {
        this.startChangedSig();
        this.form.controls.ausbildungEnd.updateValueAndValidity();
      },
      { allowSignalWrites: true },
    );
    effect(
      () => {
        this.endChangedSig();
        this.form.controls.ausbildungBegin.updateValueAndValidity();
      },
      { allowSignalWrites: true },
    );

    // fill form
    effect(
      () => {
        const { ausbildung } = this.viewSig();
        const ausbildungsstaettes =
          this.ausbildungsstatteStore.ausbildungsstaetteViewSig();

        if (ausbildung && ausbildungsstaettes) {
          this.form.patchValue({
            ...ausbildung,
            ausbildungsgang: undefined,
          });
          const currentAusbildungsgang = ausbildung.ausbildungsgang;
          if (currentAusbildungsgang) {
            const ausbildungsstaette = ausbildungsstaettes.find(
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
                this.getTranslatedAusbildungstaetteName(ausbildungsstaette),
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
        this.gotReenabledSig();
        const isAusbildungAusland = !!isAusbildungAuslandSig();

        if (isAusbildungAusland) {
          this.form.controls.ausbildungsort.reset();
        }

        this.formUtils.setDisabledState(
          this.form.controls.ausbildungsort,
          this.viewSig().readonly || isAusbildungAusland,
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
        this.gotReenabledSig();
        const staette = staetteSig();
        this.formUtils.setDisabledState(
          this.form.controls.ausbildungsgang,
          this.viewSig().readonly || !staette,
          !this.viewSig().readonly,
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
        this.gotReenabledSig();
        this.formUtils.setDisabledState(
          this.form.controls.fachrichtung,
          this.viewSig().readonly ||
            (!ausbildungNichtGefundenSig() && !ausbildungsgangSig()),
          !this.viewSig().readonly,
        );
      },
      { allowSignalWrites: true },
    );
  }

  ngOnInit() {
    this.store.dispatch(SharedEventGesuchFormEducation.init());
  }

  trackByIndex(index: number) {
    return index;
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

  handleSave() {
    this.form.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);
    const { gesuchId, trancheId, gesuchFormular } =
      this.buildUpdatedGesuchFromForm();
    if (this.form.valid && gesuchId && trancheId) {
      this.store.dispatch(
        SharedEventGesuchFormEducation.saveTriggered({
          origin: AUSBILDUNG,
          gesuchId,
          trancheId,
          gesuchFormular,
        }),
      );
      this.form.markAsPristine();
    }
  }

  handleContinue() {
    const { gesuch } = this.viewSig();
    if (gesuch?.id) {
      this.store.dispatch(
        SharedEventGesuchFormEducation.nextTriggered({
          id: gesuch.id,
          trancheId: gesuch.gesuchTrancheToWorkWith.id,
          origin: AUSBILDUNG,
        }),
      );
    }
  }

  // eg extract to util service (for every form step)
  private buildUpdatedGesuchFromForm() {
    this.onDateBlur(this.form.controls.ausbildungBegin);
    this.onDateBlur(this.form.controls.ausbildungEnd);
    const { gesuch, gesuchFormular } = this.viewSig();
    const { ausbildungsgang, ...formValue } = convertTempFormToRealValues(
      this.form,
      ['fachrichtung', 'pensum'],
    );
    const ret = {
      gesuchId: gesuch?.id,
      trancheId: gesuch?.gesuchTrancheToWorkWith.id,
      gesuchFormular: {
        ...gesuchFormular,
        ausbildung: {
          ...formValue,
          ausbildungsgangId: ausbildungsgang ?? undefined,
          ausbildungsstaette: undefined,
        },
      },
    };
    return ret;
  }

  getTranslatedAusbildungstaetteName(
    staette: Ausbildungsstaette | undefined,
  ): string | undefined {
    if (staette === undefined) {
      return undefined;
    }
    return this.languageSig() === 'fr' ? staette.nameFr : staette.nameDe;
  }

  onDateBlur(ctrl: FormControl) {
    return onMonthYearInputBlur(ctrl, new Date(), this.languageSig());
  }

  private getTranslatedAusbildungsgangName(
    ausbildungsgang: Ausbildungsgang,
  ): string | undefined {
    return this.languageSig() === 'fr'
      ? ausbildungsgang.bezeichnungFr
      : ausbildungsgang.bezeichnungDe;
  }
}
