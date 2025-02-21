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
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
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
import { Subject, merge } from 'rxjs';

import { SteuerdatenStore } from '@dv/shared/data-access/steuerdaten';
import { SharedEventGesuchFormElternSteuerdaten } from '@dv/shared/event/gesuch-form-eltern-steuerdaten';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import {
  DokumentTyp,
  SharedModelGesuchFormularUpdate,
  Steuerdaten,
  SteuerdatenTyp,
  SteuererklaerungUpdate,
} from '@dv/shared/model/gesuch';
import { ELTERN_STEUER_STEPS } from '@dv/shared/model/gesuch-form';
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
  destroyRef = inject(DestroyRef);
  stepSig = input.required<{ type: SteuerdatenTyp }>({ alias: 'step' });
  formUtils = inject(SharedUtilFormService);
  elementRef = inject(ElementRef);
  viewSig = this.store.selectSignal(
    selectSharedFeatureGesuchFormSteuerdatenView,
  );
  gotReenabled$ = new Subject<object>();
  maskitoNumber = maskitoNumber;

  hasUnsavedChanges = false;

  private createUploadOptionsSig = createUploadOptionsFactory(this.viewSig);

  steuererklaerungForm = this.formBuilder.group({
    steuererklaerungInBern: [<boolean | null>null, [Validators.required]],
  });

  steuererklaerungInBernChangedSig = toSignal(
    this.steuererklaerungForm.controls.steuererklaerungInBern.valueChanges,
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
    return this.steuerdatenStore
      .cachedSteuerdatenListViewSig()
      ?.find((s) => s.steuerdatenTyp === this.stepSig().type);
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
          this.steuerdatenForm.patchValue({
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
            this.steuerdatenForm.controls.saeule3a,
            arbeitsverhaeltnis ?? false,
          );
          this.hiddenFieldSet.setFieldVisibility(
            this.steuerdatenForm.controls.saeule2,
            arbeitsverhaeltnis ?? false,
          );
        },
        { allowSignalWrites: true },
      );
    }
  }

  constructor() {
    this.formUtils.registerFormForUnsavedCheck(this);
    merge(
      this.steuererklaerungForm.valueChanges,
      this.steuerdatenForm.valueChanges,
    )
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => {
        this.hasUnsavedChanges =
          this.steuerdatenForm.dirty || this.steuererklaerungForm.dirty;
      });

    this.createSteuerDatenSBEffects();
    this.steuerjahrValidation.createEffect();
    this.store.dispatch(SharedEventGesuchFormElternSteuerdaten.init());

    effect(() => {
      const steuererklaerung = this.originalSteuererklaerungSig();
      if (steuererklaerung) {
        this.steuererklaerungForm.patchValue({
          steuererklaerungInBern: steuererklaerung.steuererklaerungInBern,
        });
      }
    });
  }

  handleSave(): void {
    this.steuererklaerungForm.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);
    const { gesuchId, trancheId, gesuchFormular } =
      this.buildUpdatedGesuchFromSteuererklaerungForm();

    if (this.steuererklaerungForm.valid && gesuchId && trancheId) {
      this.steuererklaerungForm.markAsPristine();
      this.hasUnsavedChanges = false;
      this.store.dispatch(
        SharedEventGesuchFormElternSteuerdaten.saveTriggered({
          gesuchId,
          trancheId,
          gesuchFormular,
          origin: ELTERN_STEUER_STEPS[this.stepSig().type],
        }),
      );
    }
  }

  handleSaveSachbearbeiter(): void {
    this.steuererklaerungForm.markAllAsTouched();
    this.steuerdatenForm.markAllAsTouched();
    const { gesuchId, trancheId, gesuchFormular } =
      this.buildUpdatedGesuchFromSteuererklaerungForm();

    this.formUtils.focusFirstInvalid(this.elementRef);
    const steuerdaten = this.buildUpdateSteuerdatenForm();

    if (
      this.steuererklaerungForm.valid &&
      this.steuerdatenForm.valid &&
      gesuchId &&
      trancheId
    ) {
      this.hasUnsavedChanges = false;
      this.steuerdatenForm.markAsPristine();
      this.steuererklaerungForm.markAsPristine();

      this.steuerdatenStore.updateSteuerdaten$({
        gesuchTrancheId: trancheId,
        steuerdaten,
        onSuccess: () => {
          this.store.dispatch(
            SharedEventGesuchFormElternSteuerdaten.saveTriggered({
              gesuchId,
              trancheId,
              gesuchFormular,
              origin: ELTERN_STEUER_STEPS[this.stepSig().type],
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
          origin: ELTERN_STEUER_STEPS[this.stepSig().type],
        }),
      );
    }
  }

  private buildUpdatedGesuchFromSteuererklaerungForm() {
    const { gesuch, gesuchFormular } = this.viewSig();
    const formValues = convertTempFormToRealValues(this.steuererklaerungForm, [
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

  private buildUpdateSteuerdatenForm(): Steuerdaten[] {
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
    return upsertSteuerdaten(
      steuerdaten,
      this.steuerdatenStore.cachedSteuerdatenListViewSig(),
    );
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
