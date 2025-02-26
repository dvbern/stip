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

import { SteuerdatenStore } from '@dv/shared/data-access/steuerdaten';
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

import { selectSharedFeatureGesuchFormSteuererklaerungView } from './shared-feature-gesuch-form-eltern-steuererklaerung.selector';

@Component({
  selector: 'lib-shared-feature-gesuch-form-eltern-steuererklaerung',
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
    SharedUiFormZuvorHintComponent,
    SharedUiTranslateChangePipe,
    SharedPatternDocumentUploadComponent,
    SharedUiInfoDialogDirective,
  ],
  templateUrl:
    './shared-feature-gesuch-form-eltern-steuererklaerung.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormElternSteuererklaerungComponent {
  private store = inject(Store);
  private formBuilder = inject(NonNullableFormBuilder);
  private steuerdatenStore = inject(SteuerdatenStore);
  config = inject(SharedModelCompileTimeConfig);
  destroyRef = inject(DestroyRef);
  stepSig = input.required<{ type: SteuerdatenTyp }>({ alias: 'step' });
  formUtils = inject(SharedUtilFormService);
  elementRef = inject(ElementRef);
  viewSig = this.store.selectSignal(
    selectSharedFeatureGesuchFormSteuererklaerungView,
  );
  gotReenabled$ = new Subject<object>();
  maskitoNumber = maskitoNumber;

  hasUnsavedChanges = false;

  private createUploadOptionsSig = createUploadOptionsFactory(this.viewSig);

  form = this.formBuilder.group({
    steuererklaerungInBern: [<boolean | null>null, [Validators.required]],
  });

  steuererklaerungInBernChangedSig = toSignal(
    this.form.controls.steuererklaerungInBern.valueChanges,
  );

  steuererklaerungInBernDocumentSig = this.createUploadOptionsSig(() => {
    const steuererklaerungInBern = this.steuererklaerungInBernChangedSig();
    const type = this.stepSig().type;

    // if in bern, no document is needed, explicit, so no document as long as
    // no value is set!
    return steuererklaerungInBern === false
      ? DokumentTyp[`STEUERERKLAERUNG_AUSBILDUNGSBEITRAEGE_${type}`]
      : null;
  });

  private gotReenabledSig = toSignal(this.gotReenabled$);

  hiddenFieldSet = this.formUtils.createHiddenFieldSet();

  originalSteuerdatenSig = computed(() => {
    return this.steuerdatenStore
      .cachedSteuerdatenListViewSig()
      ?.find((s) => s.steuerdatenTyp === this.stepSig().type);
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
    ]);

    const steuererklaerung = {
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
