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
import { TranslocoPipe } from '@jsverse/transloco';
import { MaskitoDirective } from '@maskito/angular';
import { Store } from '@ngrx/store';

import { SharedEventGesuchFormElternSteuerdaten } from '@dv/shared/event/gesuch-form-eltern-steuererklaerung';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import {
  DokumentTyp,
  SharedModelGesuchFormularUpdate,
  SteuerdatenTyp,
  SteuererklaerungUpdate,
} from '@dv/shared/model/gesuch';
import { ELTERN_STEUERERKLAERUNG_STEPS } from '@dv/shared/model/gesuch-form';
import {
  SharedPatternDocumentUploadComponent,
  createUploadOptionsFactory,
} from '@dv/shared/pattern/document-upload';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiFormReadonlyDirective,
  SharedUiFormZuvorHintComponent,
  SharedUiZuvorHintDirective,
} from '@dv/shared/ui/form';
import { SharedUiInfoDialogDirective } from '@dv/shared/ui/info-dialog';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import { SharedUiTranslateChangePipe } from '@dv/shared/ui/translate-change';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
} from '@dv/shared/util/form';
import {
  fromFormatedNumber,
  maskitoNumber,
} from '@dv/shared/util/maskito-util';

import { selectSharedFeatureGesuchFormSteuererklaerungView } from './shared-feature-gesuch-form-eltern-steuererklaerung.selector';

@Component({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MaskitoDirective,
    SharedUiInfoDialogDirective,
    TranslocoPipe,
    MatFormFieldModule,
    SharedUiZuvorHintDirective,
    MatInputModule,
    MatRadioModule,
    SharedUiLoadingComponent,
    SharedUiFormReadonlyDirective,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiStepFormButtonsComponent,
    SharedUiFormZuvorHintComponent,
    SharedUiTranslateChangePipe,
    SharedPatternDocumentUploadComponent,
  ],
  templateUrl:
    './shared-feature-gesuch-form-eltern-steuererklaerung.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormElternSteuererklaerungComponent {
  private store = inject(Store);
  private formBuilder = inject(NonNullableFormBuilder);
  config = inject(SharedModelCompileTimeConfig);
  destroyRef = inject(DestroyRef);
  // eslint-disable-next-line @angular-eslint/no-input-rename
  stepSig = input.required<{ type: SteuerdatenTyp }>({ alias: 'step' });
  formUtils = inject(SharedUtilFormService);
  elementRef = inject(ElementRef);
  viewSig = this.store.selectSignal(
    selectSharedFeatureGesuchFormSteuererklaerungView,
  );

  hasUnsavedChanges = false;

  maskitoNumber = maskitoNumber;

  private createUploadOptionsSig = createUploadOptionsFactory(this.viewSig);

  form = this.formBuilder.group({
    steuererklaerungInBern: [<boolean | null>null, [Validators.required]],
    unterhaltsbeitraege: [<string | null>null, [Validators.required]],
    renten: [<string | null>null, [Validators.required]],
    ergaenzungsleistungen: [<string | undefined>undefined],
    einnahmenBGSA: [<string | undefined>undefined],
  });

  steuererklaerungInBernChangedSig = toSignal(
    this.form.controls.steuererklaerungInBern.valueChanges,
  );

  private numberConverter = this.formUtils.createNumberConverter(this.form, [
    'ergaenzungsleistungen',
    'unterhaltsbeitraege',
    'renten',
    'einnahmenBGSA',
  ]);

  private ergaenzungsleistungChangedSig = toSignal(
    this.form.controls.ergaenzungsleistungen.valueChanges,
  );
  ergaenzungsleistungenDocumentSig = this.createUploadOptionsSig(() => {
    const steuerdatenTyp = this.stepSig().type;

    const ergaenzungsleistung = fromFormatedNumber(
      this.ergaenzungsleistungChangedSig() ?? '0',
    );

    return ergaenzungsleistung > 0
      ? `STEUERERKLAERUNG_ERGAENZUNGSLEISTUNGEN_${steuerdatenTyp}`
      : null;
  });

  steuererklaerungInBernDocumentSig = this.createUploadOptionsSig(() => {
    const steuererklaerungInBern = this.steuererklaerungInBernChangedSig();
    const steuerdatenTyp = this.stepSig().type;

    // if in bern, no document is needed, explicit, so no document as long as
    // no value is set!
    return steuererklaerungInBern === false
      ? DokumentTyp[`STEUERERKLAERUNG_AUSBILDUNGSBEITRAEGE_${steuerdatenTyp}`]
      : null;
  });

  unterhaltsbeitraegeChangeSig = toSignal(
    this.form.controls.unterhaltsbeitraege.valueChanges,
  );
  unterhaltsbeitraegeDocumentSig = this.createUploadOptionsSig(() => {
    const unterhaltsbeitraege = fromFormatedNumber(
      this.unterhaltsbeitraegeChangeSig() ?? '0',
    );
    const steuerdatenTyp = this.stepSig().type;

    return unterhaltsbeitraege > 0
      ? DokumentTyp[`STEUERERKLAERUNG_UNTERHALTSBEITRAEGE_${steuerdatenTyp}`]
      : null;
  });

  rentenSig = toSignal(this.form.controls.renten.valueChanges);
  rentenDocumentSig = this.createUploadOptionsSig(() => {
    const renten = fromFormatedNumber(this.rentenSig() ?? '0');
    const steuerdatenTyp = this.stepSig().type;

    return renten > 0
      ? DokumentTyp[`STEUERERKLAERUNG_RENTEN_${steuerdatenTyp}`]
      : null;
  });

  einnahmenBGSASig = toSignal(this.form.controls.einnahmenBGSA.valueChanges);
  einnahmenBGSADocumentSig = this.createUploadOptionsSig(() => {
    const einnahmenBGSA = fromFormatedNumber(this.einnahmenBGSASig() ?? '0');
    const steuerdatenTyp = this.stepSig().type;

    return einnahmenBGSA > 0
      ? DokumentTyp[`STEUERERKLAERUNG_EINNAHMEN_BGSA_${steuerdatenTyp}`]
      : null;
  });

  originalSteuererklaerungSig = computed(() => {
    return this.viewSig().gesuchFormular?.steuererklaerung?.find(
      (s) => s.steuerdatenTyp === this.stepSig().type,
    );
  });

  constructor() {
    this.formUtils.registerFormForUnsavedCheck(this);
    this.store.dispatch(SharedEventGesuchFormElternSteuerdaten.init());

    effect(() => {
      const steuererklaerung = this.originalSteuererklaerungSig();
      if (steuererklaerung) {
        this.form.patchValue({
          ...steuererklaerung,
          ...this.numberConverter.toString(steuererklaerung),
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
      this.form.markAsPristine();
      this.hasUnsavedChanges = false;
      this.store.dispatch(
        SharedEventGesuchFormElternSteuerdaten.saveTriggered({
          gesuchId,
          trancheId,
          gesuchFormular,
          origin: ELTERN_STEUERERKLAERUNG_STEPS[this.stepSig().type],
        }),
      );
    }
  }

  handleContinue() {
    const { gesuch } = this.viewSig();
    if (gesuch?.id) {
      this.store.dispatch(
        SharedEventGesuchFormElternSteuerdaten.nextTriggered({
          id: gesuch.id,
          origin: ELTERN_STEUERERKLAERUNG_STEPS[this.stepSig().type],
        }),
      );
    }
  }

  private buildUpdatedGesuchFromSteuererklaerungForm() {
    const { gesuch, gesuchFormular } = this.viewSig();
    const formValues = convertTempFormToRealValues(this.form, [
      'steuererklaerungInBern',
      'unterhaltsbeitraege',
      'renten',
    ]);

    const steuererklaerung = {
      ...formValues,
      steuerdatenTyp: this.stepSig().type,
      ...this.numberConverter.toNumber(formValues),
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
