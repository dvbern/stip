import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
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
import { TranslateModule } from '@ngx-translate/core';
import { Subject } from 'rxjs';

import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { SharedEventGesuchFormElternSteuerdaten } from '@dv/shared/event/gesuch-form-eltern-steuerdaten';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import {
  SharedModelGesuchFormularUpdate,
  SteuerdatenTyp,
  SteuerdatenUpdate,
} from '@dv/shared/model/gesuch';
import { ELTERN_STEUER_STEPS } from '@dv/shared/model/gesuch-form';
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

@Component({
  selector: 'lib-shared-feature-gesuch-form-eltern-steuerdaten',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MaskitoDirective,
    TranslateModule,
    MatFormFieldModule,
    MatInputModule,
    MatRadioModule,
    SharedUiLoadingComponent,
    SharedUiFormReadonlyDirective,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiStepFormButtonsComponent,
  ],
  templateUrl: './shared-feature-gesuch-form-eltern-steuerdaten.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormElternSteuerdatenComponent {
  private store = inject(Store);
  private formBuilder = inject(NonNullableFormBuilder);
  private config = inject(SharedModelCompileTimeConfig);
  stepSig = input.required<{ type: SteuerdatenTyp }>({ alias: 'step' });
  formUtils = inject(SharedUtilFormService);
  elementRef = inject(ElementRef);
  viewSig = this.store.selectSignal(selectSharedDataAccessGesuchsView);
  gotReenabled$ = new Subject<object>();
  private gotReenabledSig = toSignal(this.gotReenabled$);
  maskitoNumber = maskitoNumber;
  form = this.formBuilder.group({
    totalEinkuenfte: [<string | null>null, [Validators.required]],
    eigenmietwert: [<string | null>null, [Validators.required]],
    arbeitsverhaeltnis: [<boolean | null>null, [Validators.required]],
    saeule3a: [<string | null>null, [Validators.required]],
    saeule2: [<string | null>null, [Validators.required]],
    kinderalimente: [<string | null>null, [Validators.required]],
    ergaenzungsleistungen: [<string | null>null, [Validators.required]],
    vermoegen: [<string | null>null, [Validators.required]],
    steuernStaat: [<string | null>null, [Validators.required]],
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
  hiddenFieldSet = this.formUtils.createHiddenFieldSet();
  arbeitsverhaeltnisChangedSig = toSignal(
    this.form.controls.arbeitsverhaeltnis.valueChanges,
  );
  originalSteuerdatenSig = computed(() => {
    return this.viewSig().gesuchFormular?.steuerdaten?.find(
      (s) => s.steuerdatenTyp === this.stepSig().type,
    );
  });
  numberConverter = this.formUtils.createNumberConverter(this.form, [
    'totalEinkuenfte',
    'eigenmietwert',
    'saeule3a',
    'saeule2',
    'kinderalimente',
    'ergaenzungsleistungen',
    'vermoegen',
    'steuernStaat',
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
    this.store.dispatch(SharedEventGesuchFormElternSteuerdaten.init());
    this.steuerjahrValidation.createEffect();
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
        this.hiddenFieldSet.setFieldVisibility(
          this.form.controls.veranlagungsCode,
          this.config.isSachbearbeitungApp,
        );
        this.hiddenFieldSet.setFieldVisibility(
          this.form.controls.steuerjahr,
          this.config.isSachbearbeitungApp,
        );
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
  }

  handleSave(): void {
    this.form.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);
    const { gesuchId, trancheId, gesuchFormular } =
      this.buildUpdatedGesuchFromForm();

    if (this.form.valid && gesuchId && trancheId) {
      this.store.dispatch(
        SharedEventGesuchFormElternSteuerdaten.saveTriggered({
          gesuchId,
          trancheId,
          gesuchFormular,
          origin: ELTERN_STEUER_STEPS[this.stepSig().type],
        }),
      );
      this.form.markAsPristine();
    }
  }

  handleContinue() {
    const { gesuch } = this.viewSig();
    if (gesuch?.id) {
      this.store.dispatch(
        SharedEventGesuchFormElternSteuerdaten.nextTriggered({
          id: gesuch.id,
          origin: ELTERN_STEUER_STEPS[this.stepSig().type],
        }),
      );
    }
  }

  private buildUpdatedGesuchFromForm() {
    const { gesuch, gesuchFormular } = this.viewSig();
    const formValues = convertTempFormToRealValues(this.form, [
      'totalEinkuenfte',
      'eigenmietwert',
      'arbeitsverhaeltnis',
      'saeule3a',
      'saeule2',
      'kinderalimente',
      'ergaenzungsleistungen',
      'vermoegen',
      'steuernStaat',
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
      ...this.numberConverter.toNumber(),
    };
    return {
      gesuchId: gesuch?.id,
      trancheId: gesuch?.gesuchTrancheToWorkWith.id,
      gesuchFormular: {
        ...gesuchFormular,
        steuerdaten: upsertSteuerdaten(steuerdaten, gesuchFormular),
      },
    };
  }
}

const upsertSteuerdaten = (
  steuerdaten: SteuerdatenUpdate,
  gesuchFormular?: SharedModelGesuchFormularUpdate | null,
) => {
  const uniqueSteuerdaten = new Set<string>();
  const result = [];

  const combinedSteuerdaten = [
    ...(gesuchFormular?.steuerdaten ?? []),
    steuerdaten,
  ];

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
