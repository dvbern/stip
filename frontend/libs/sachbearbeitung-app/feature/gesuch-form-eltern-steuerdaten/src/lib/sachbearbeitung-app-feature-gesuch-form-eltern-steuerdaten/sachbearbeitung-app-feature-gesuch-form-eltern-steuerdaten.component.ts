import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  ElementRef,
  computed,
  effect,
  inject,
  input,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { MaskitoDirective } from '@maskito/angular';
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';
import { Subject } from 'rxjs';

import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { SharedEventGesuchFormElternSteuerdaten } from '@dv/shared/event/gesuch-form-eltern-steuererklaerung';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import { Steuerdaten, SteuerdatenTyp } from '@dv/shared/model/gesuch';
import { ELTERN_STEUERDATEN_STEPS } from '@dv/shared/model/gesuch-form';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiFormReadonlyDirective,
} from '@dv/shared/ui/form';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
} from '@dv/shared/util/form';
import { maskitoNumber } from '@dv/shared/util/maskito-util';
import { sharedUtilValidatorRange } from '@dv/shared/util/validator-range';
import { prepareSteuerjahrValidation } from '@dv/shared/util/validator-steuerdaten';
import { SteuerdatenStore } from '@dv/shared-data-access-steuerdaten';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-gesuch-form-eltern-steuerdaten',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MaskitoDirective,
    TranslatePipe,
    MatFormFieldModule,
    MatInputModule,
    MatRadioModule,
    SharedUiLoadingComponent,
    SharedUiFormReadonlyDirective,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiStepFormButtonsComponent,
  ],
  templateUrl:
    './sachbearbeitung-app-feature-gesuch-form-eltern-steuerdaten.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureGesuchFormElternSteuerdatenComponent {
  private store = inject(Store);
  private formBuilder = inject(NonNullableFormBuilder);
  private steuerdatenStore = inject(SteuerdatenStore);
  config = inject(SharedModelCompileTimeConfig);
  destroyRef = inject(DestroyRef);
  stepSig = input.required<{ type: SteuerdatenTyp }>({ alias: 'step' });
  formUtils = inject(SharedUtilFormService);
  elementRef = inject(ElementRef);
  gotReenabled$ = new Subject<object>();
  viewSig = this.store.selectSignal(selectSharedDataAccessGesuchsView);
  maskitoNumber = maskitoNumber;

  form = this.formBuilder.group({
    totalEinkuenfte: [<string | null>null, [Validators.required]],
    eigenmietwert: [<string | null>null, [Validators.required]],
    arbeitsverhaeltnis: [<boolean | null>null, [Validators.required]],
    saeule3a: [<string | null>null, [Validators.required]],
    saeule2: [<string | null>null, [Validators.required]],
    kinderalimente: [<string | null>null, [Validators.required]],
    vermoegen: [<string | null>null, [Validators.required]],
    steuernKantonGemeinde: [<string | null>null, [Validators.required]],
    steuernBund: [<string | null>null, [Validators.required]],
    fahrkosten: [<string | null>null, [Validators.required]],
    fahrkostenPartner: [<string | null>null, [Validators.required]],
    verpflegung: [<string | null>null, [Validators.required]],
    verpflegungPartner: [<string | null>null, [Validators.required]],
    steuerjahr: [
      <number | null>null,
      [
        /** @see // this.steuerjahrValidation */
      ],
    ],
    veranlagungsCode: [
      <number | null>null,
      [Validators.required, sharedUtilValidatorRange(0, 99)],
    ],
  });

  private gotReenabledSig = toSignal(this.gotReenabled$);
  private arbeitsverhaeltnisChangedSig = toSignal(
    this.form.controls.arbeitsverhaeltnis.valueChanges,
  );

  hiddenFieldSet = this.formUtils.createHiddenFieldSet();

  originalSteuerdatenSig = computed(() => {
    return this.steuerdatenStore
      .cachedSteuerdatenListViewSig()
      ?.find((s) => s.steuerdatenTyp === this.stepSig().type);
  });

  private numberConverter = this.formUtils.createNumberConverter(this.form, [
    'totalEinkuenfte',
    'eigenmietwert',
    'saeule3a',
    'saeule2',
    'kinderalimente',
    'vermoegen',
    'steuernKantonGemeinde',
    'steuernBund',
    'fahrkosten',
    'fahrkostenPartner',
    'verpflegung',
    'verpflegungPartner',
  ]);

  steuerjahrValidation = prepareSteuerjahrValidation(
    this.form.controls.steuerjahr,
    this.viewSig,
  );

  constructor() {
    this.createSteuerDatenSBEffects();
    this.steuerjahrValidation.createEffect();
    this.store.dispatch(SharedEventGesuchFormElternSteuerdaten.init());
  }

  private createSteuerDatenSBEffects() {
    const isSachbearbeitungApp = this.config.isSachbearbeitungApp;

    if (isSachbearbeitungApp) {
      effect(
        () => {
          const { trancheId: gesuchTrancheId } = this.viewSig();
          if (!gesuchTrancheId) return;
          this.steuerdatenStore.getSteuerdaten$({ gesuchTrancheId });
        },
        { allowSignalWrites: true },
      );
      effect(() => {
        const steuerdaten = this.originalSteuerdatenSig();

        if (steuerdaten) {
          this.form.patchValue({
            ...steuerdaten,
            arbeitsverhaeltnis: steuerdaten.isArbeitsverhaeltnisSelbstaendig,
            ...this.numberConverter.toString(steuerdaten),
          });
        }
      });
      effect(
        () => {
          this.gotReenabledSig();
          const arbeitsverhaeltnis = this.arbeitsverhaeltnisChangedSig();
          this.hiddenFieldSet.setFieldVisibility(
            this.form.controls.saeule3a,
            arbeitsverhaeltnis ?? false,
          );
          this.hiddenFieldSet.setFieldVisibility(
            this.form.controls.saeule2,
            arbeitsverhaeltnis ?? false,
          );
        },
        { allowSignalWrites: true },
      );
    }
  }

  handleSave(): void {
    this.form.markAllAsTouched();

    this.formUtils.focusFirstInvalid(this.elementRef);
    const { gesuchId, trancheId, gesuchFormular, steuerdaten } =
      this.buildUpdateSteuerdatenForm();

    if (this.form.valid && gesuchId && trancheId && gesuchFormular) {
      this.form.markAsPristine();

      this.steuerdatenStore.updateSteuerdaten$({
        gesuchTrancheId: trancheId,
        steuerdaten,
        onSuccess: () => {
          this.store.dispatch(
            SharedEventGesuchFormElternSteuerdaten.nextTriggered({
              id: gesuchId,
              origin: ELTERN_STEUERDATEN_STEPS[this.stepSig().type],
            }),
          );
        },
      });
    }
  }

  handleContinue() {
    const { gesuch } = this.viewSig();
    if (gesuch?.id) {
      this.store.dispatch(
        SharedEventGesuchFormElternSteuerdaten.nextTriggered({
          id: gesuch.id,
          origin: ELTERN_STEUERDATEN_STEPS[this.stepSig().type],
        }),
      );
    }
  }

  private buildUpdateSteuerdatenForm() {
    const { gesuch, gesuchFormular } = this.viewSig();
    const formValues = convertTempFormToRealValues(this.form, [
      'totalEinkuenfte',
      'eigenmietwert',
      'arbeitsverhaeltnis',
      'saeule3a',
      'saeule2',
      'kinderalimente',
      'vermoegen',
      'steuernKantonGemeinde',
      'steuernBund',
      'fahrkosten',
      'fahrkostenPartner',
      'verpflegung',
      'verpflegungPartner',
      'steuerjahr',
      'veranlagungsCode',
    ]);
    const originalSteuerdaten = this.originalSteuerdatenSig();
    const steuerdaten = {
      id: originalSteuerdaten?.id,
      ...formValues,
      steuerdatenTyp: this.stepSig().type,
      arbeitsverhaeltnis: undefined,
      isArbeitsverhaeltnisSelbstaendig: formValues.arbeitsverhaeltnis,
      ...this.numberConverter.toNumber(formValues),
    };
    return {
      gesuchId: gesuch?.id,
      gesuchFormular,
      trancheId: gesuch?.gesuchTrancheToWorkWith.id,
      steuerdaten: upsertSteuerdaten(
        steuerdaten,
        this.steuerdatenStore.cachedSteuerdatenListViewSig(),
      ),
    };
  }
}

const upsertSteuerdaten = (
  steuerdaten: Steuerdaten,
  originalSteuerdaten?: Steuerdaten[],
) => {
  const uniqueSteuerdaten = new Set<string>();
  const result = [];

  const combinedSteuerdaten = [...(originalSteuerdaten ?? []), steuerdaten];

  for (const s of combinedSteuerdaten) {
    if (!uniqueSteuerdaten.has(s.steuerdatenTyp)) {
      uniqueSteuerdaten.add(s.steuerdatenTyp);
      result.push(
        s.steuerdatenTyp === steuerdaten.steuerdatenTyp
          ? { ...s, ...steuerdaten }
          : s,
      );
    }
  }

  return result;
};
