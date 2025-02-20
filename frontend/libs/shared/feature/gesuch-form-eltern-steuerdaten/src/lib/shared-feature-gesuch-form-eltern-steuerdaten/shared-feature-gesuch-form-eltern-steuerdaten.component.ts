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
import { TranslatePipe } from '@ngx-translate/core';
import { Subject } from 'rxjs';

import { SteuerdatenStore } from '@dv/shared/data-access/steuerdaten';
import { SharedEventGesuchFormElternSteuerdaten } from '@dv/shared/event/gesuch-form-eltern-steuerdaten';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import {
  SharedModelGesuchFormularUpdate,
  SteuerdatenTyp,
  SteuerdatenUpdate,
  SteuererklaerungUpdate,
} from '@dv/shared/model/gesuch';
import { ELTERN_STEUER_STEPS } from '@dv/shared/model/gesuch-form';
import { SharedPatternDocumentUploadComponent } from '@dv/shared/pattern/document-upload';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiFormReadonlyDirective,
  SharedUiFormZuvorHintComponent,
  SharedUiZuvorHintDirective,
} from '@dv/shared/ui/form';
import {
  SharedUiIfGesuchstellerDirective,
  SharedUiIfSachbearbeiterDirective,
} from '@dv/shared/ui/if-app-type';
import { SharedUiInfoDialogDirective } from '@dv/shared/ui/info-dialog';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import { SharedUiTranslateChangePipe } from '@dv/shared/ui/translate-change';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
} from '@dv/shared/util/form';
import { maskitoNumber } from '@dv/shared/util/maskito-util';
import { sharedUtilValidatorRange } from '@dv/shared/util/validator-range';
import { prepareSteuerjahrValidation } from '@dv/shared/util/validator-steuerdaten';

import { selectSharedFeatureGesuchFormSteuerdatenView } from './shared-feature-gesuch-form-eltern-steuerdaten.selector';

@Component({
  selector: 'lib-shared-feature-gesuch-form-eltern-steuerdaten',
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
    SharedUiIfSachbearbeiterDirective,
    SharedUiIfGesuchstellerDirective,
    SharedUiZuvorHintDirective,
    SharedUiFormZuvorHintComponent,
    SharedUiTranslateChangePipe,
    SharedPatternDocumentUploadComponent,
    SharedUiInfoDialogDirective,
  ],
  templateUrl: './shared-feature-gesuch-form-eltern-steuerdaten.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormElternSteuerdatenComponent {
  private store = inject(Store);
  private formBuilder = inject(NonNullableFormBuilder);
  private config = inject(SharedModelCompileTimeConfig);
  private steuerdatenStore = inject(SteuerdatenStore);

  stepSig = input.required<{ type: SteuerdatenTyp }>({ alias: 'step' });
  formUtils = inject(SharedUtilFormService);
  elementRef = inject(ElementRef);
  viewSig = this.store.selectSignal(
    selectSharedFeatureGesuchFormSteuerdatenView,
  );
  gotReenabled$ = new Subject<object>();
  maskitoNumber = maskitoNumber;

  form = this.formBuilder.group({
    steuererklaerungInBern: [<boolean | null>null, [Validators.required]],
  });

  steuerdatenForm = this.formBuilder.group({
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
    this.steuerdatenForm.controls.arbeitsverhaeltnis.valueChanges,
  );

  hiddenFieldSet = this.formUtils.createHiddenFieldSet();

  originalSteuerdatenSig = computed(() => {
    // todo: return the steuerdaten from new store
    return this.viewSig().gesuchFormular?.steuererklaerung?.find(
      (s) => s.steuerdatenTyp === this.stepSig().type,
    );
  });

  originalSteuererklaerungSig = computed(() => {
    return this.viewSig().gesuchFormular?.steuererklaerung?.find(
      (s) => s.steuerdatenTyp === this.stepSig().type,
    );
  });

  private numberConverter = this.formUtils.createNumberConverter(
    this.steuerdatenForm,
    [
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
    ],
  );

  steuerjahrValidation = prepareSteuerjahrValidation(
    this.steuerdatenForm.controls.steuerjahr,
    this.viewSig,
  );

  private createSteuerDatenEffect() {
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

        // if (steuerdaten) {
        //   this.form.patchValue({
        //     ...steuerdaten,
        //     arbeitsverhaeltnis: steuerdaten.isArbeitsverhaeltnisSelbstaendig,
        //     ...this.numberConverter.toString(steuerdaten),
        //   });
        // }
      });
    }
  }

  constructor() {
    this.store.dispatch(SharedEventGesuchFormElternSteuerdaten.init());

    this.createSteuerDatenEffect();
    this.steuerjahrValidation.createEffect();

    // betrifft sachbearbeiter
    effect(
      () => {
        this.gotReenabledSig();
        const arbeitsverhaeltnis = this.arbeitsverhaeltnisChangedSig();
        this.hiddenFieldSet.setFieldVisibility(
          this.steuerdatenForm.controls.saeule3a,
          arbeitsverhaeltnis ?? false,
        );
        this.hiddenFieldSet.setFieldVisibility(
          this.steuerdatenForm.controls.saeule2,
          arbeitsverhaeltnis ?? false,
        );
        this.hiddenFieldSet.setFieldVisibility(
          this.steuerdatenForm.controls.veranlagungsCode,
          this.config.isSachbearbeitungApp,
        );
        this.hiddenFieldSet.setFieldVisibility(
          this.steuerdatenForm.controls.steuerjahr,
          this.config.isSachbearbeitungApp,
        );
      },
      { allowSignalWrites: true },
    );

    effect(() => {
      const steuererklaerung = this.originalSteuererklaerungSig();
      if (steuererklaerung) {
        this.form.patchValue({
          steuererklaerungInBern: steuererklaerung.steuererklaerungInBern,
        });
      }
    });
  }

  handleSave(): void {
    this.form.markAllAsTouched();

    this.formUtils.focusFirstInvalid(this.elementRef);

    const { gesuchId, trancheId, gesuchFormular } =
      this.buildUpdatedGesuchFromSteuererklaerungForm();

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

  handleSaveSachbearbeiter(): void {
    this.form.markAllAsTouched();
    this.steuerdatenForm.markAllAsTouched();

    this.formUtils.focusFirstInvalid(this.elementRef);
    const { gesuchId, trancheId, gesuchFormular } =
      this.buildUpdatedGesuchFromSteuerdatenForm();

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

  private buildUpdatedGesuchFromSteuererklaerungForm() {
    const { gesuch, gesuchFormular } = this.viewSig();
    const formValues = convertTempFormToRealValues(this.form, [
      'steuererklaerungInBern',
    ]);

    const originalSteuererklaerung = this.originalSteuererklaerungSig();
    const steuererklaerung = {
      id: originalSteuererklaerung?.id,
      ...formValues,
      steuerdatenTyp: this.stepSig().type,
    };
    return {
      gesuchId: gesuch?.id,
      trancheId: gesuch?.gesuchTrancheToWorkWith.id,
      gesuchFormular: {
        ...gesuchFormular,
        steuererklaerung: upsertSteuererklaerung(
          steuererklaerung,
          gesuchFormular,
        ),
      },
    };
  }

  private buildUpdatedGesuchFromSteuerdatenForm() {
    const { gesuch, gesuchFormular } = this.viewSig();
    const formValues = convertTempFormToRealValues(this.steuerdatenForm, [
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
      trancheId: gesuch?.gesuchTrancheToWorkWith.id,
      gesuchFormular: {
        ...gesuchFormular,
        steuerdaten: upsertSteuerdaten(steuerdaten, gesuchFormular),
      },
    };
  }
}

const upsertSteuererklaerung = (
  steuererklaerung: SteuererklaerungUpdate,
  gesuchFormular?: SharedModelGesuchFormularUpdate | null,
) => {
  const uniqueSteuererklaerung = new Set<string>();
  const result = [];

  const combinedSteuererklaerung = [
    ...(gesuchFormular?.steuererklaerung ?? []),
    steuererklaerung,
  ];

  for (const s of combinedSteuererklaerung) {
    if (!uniqueSteuererklaerung.has(s.steuerdatenTyp)) {
      uniqueSteuererklaerung.add(s.steuerdatenTyp);
      result.push(
        s.steuerdatenTyp === steuererklaerung.steuerdatenTyp
          ? { ...s, ...steuererklaerung }
          : s,
      );
    }
  }

  return result;
};

// Todo: will only be used for SB and will be different
const upsertSteuerdaten = (
  steuerdaten: SteuerdatenUpdate,
  gesuchFormular?: SharedModelGesuchFormularUpdate | null,
) => {
  const uniqueSteuerdaten = new Set<string>();
  const result = [];

  const combinedSteuerdaten = [
    // ...(gesuchFormular?.steuerdaten ?? []),
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
