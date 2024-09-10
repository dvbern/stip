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

import { SharedEventGesuchFormElternSteuerdaten } from '@dv/shared/event/gesuch-form-eltern-steuerdaten';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import {
  SharedModelGesuchFormularUpdate,
  SteuerdatenTyp,
  SteuerdatenUpdate,
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
  SharedUiZuvorHintDirective,
} from '@dv/shared/ui/form';
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
    TranslateModule,
    MatFormFieldModule,
    MatInputModule,
    MatRadioModule,
    SharedUiLoadingComponent,
    SharedUiFormReadonlyDirective,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiStepFormButtonsComponent,
    SharedUiZuvorHintDirective,
    SharedUiFormZuvorHintComponent,
    SharedUiTranslateChangePipe,
    SharedPatternDocumentUploadComponent,
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
  viewSig = this.store.selectSignal(
    selectSharedFeatureGesuchFormSteuerdatenView,
  );
  gotReenabled$ = new Subject<object>();
  maskitoNumber = maskitoNumber;
  form = this.formBuilder.group({
    totalEinkuenfte: [<string | null>null, [Validators.required]],
    eigenmietwert: [<string | null>null, []],
    arbeitsverhaeltnis: [<boolean | null>null, [Validators.required]],
    saeule3a: [<string | null>null, [Validators.required]],
    saeule2: [<string | null>null, [Validators.required]],
    kinderalimente: [<string | null>null, [Validators.required]],
    vermoegen: [<string | null>null, [Validators.required]],
    wohnkosten: [<string | null>null, [Validators.required]],
    steuernKantonGemeinde: [<string | null>null, [Validators.required]],
    ergaenzungsleistungen: [<string | null>null, [Validators.required]],
    ergaenzungsleistungenPartner: [<string | null>null, [Validators.required]],
    sozialhilfebeitraege: [<string | null>null, [Validators.required]],
    sozialhilfebeitraegePartner: [<string | null>null, [Validators.required]],
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
  private createUploadOptionsSig = createUploadOptionsFactory(this.viewSig);
  private arbeitsverhaeltnisChangedSig = toSignal(
    this.form.controls.arbeitsverhaeltnis.valueChanges,
  );

  hiddenFieldSet = this.formUtils.createHiddenFieldSet();
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
    'ergaenzungsleistungenPartner',
    'sozialhilfebeitraege',
    'sozialhilfebeitraegePartner',
    'vermoegen',
    'wohnkosten',
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

  private wohnkostenChangedSig = toSignal(
    this.form.controls.wohnkosten.valueChanges,
  );
  wohnkostenDocumentSig = this.createUploadOptionsSig(() => {
    const steuerdatenTyp = this.stepSig().type;
    const wohnkosten =
      fromFormatedNumber(this.wohnkostenChangedSig() ?? undefined) ?? 0;

    return wohnkosten > 0
      ? `STEUERDATEN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_${steuerdatenTyp}`
      : null;
  });

  private ergaenzungsleistungChangedSig = toSignal(
    this.form.controls.ergaenzungsleistungen.valueChanges,
  );
  private ergaenzungsleistungPartnerChangedSig = toSignal(
    this.form.controls.ergaenzungsleistungenPartner.valueChanges,
  );
  ergaenzungsleistungenDocumentSig = this.createUploadOptionsSig(() => {
    const steuerdatenTyp = this.stepSig().type;
    const ergaenzungsleistung =
      fromFormatedNumber(this.ergaenzungsleistungChangedSig() ?? undefined) ??
      0;

    return ergaenzungsleistung > 0
      ? `STEUERDATEN_ERGAENZUNGSLEISTUNGEN_${defaultVater(steuerdatenTyp)}`
      : null;
  });
  ergaenzungsleistungenPartnerDocumentSig = this.createUploadOptionsSig(() => {
    const ergaenzungsleistung =
      fromFormatedNumber(
        this.ergaenzungsleistungPartnerChangedSig() ?? undefined,
      ) ?? 0;

    return ergaenzungsleistung
      ? 'STEUERDATEN_ERGAENZUNGSLEISTUNGEN_MUTTER'
      : null;
  });

  private sozialhilfeChangedSig = toSignal(
    this.form.controls.sozialhilfebeitraege.valueChanges,
  );
  private sozialhilfePartnerChangedSig = toSignal(
    this.form.controls.sozialhilfebeitraegePartner.valueChanges,
  );
  sozialhilfeDocumentSig = this.createUploadOptionsSig(() => {
    const steuerdatenTyp = this.stepSig().type;
    const sozialhilfe =
      fromFormatedNumber(this.sozialhilfeChangedSig() ?? undefined) ?? 0;

    return sozialhilfe > 0
      ? `STEUERDATEN_SOZIALHILFEBUDGET_${defaultVater(steuerdatenTyp)}`
      : null;
  });
  sozialhilfePartnerDocumentSig = this.createUploadOptionsSig(() => {
    const sozialhilfe =
      fromFormatedNumber(this.sozialhilfePartnerChangedSig() ?? undefined) ?? 0;

    return sozialhilfe ? 'STEUERDATEN_SOZIALHILFEBUDGET_MUTTER' : null;
  });

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

    effect(
      () => {
        const steuerdatenTyp = this.stepSig().type;
        const partnerRequired = steuerdatenTyp === 'FAMILIE';

        this.hiddenFieldSet.setFieldVisibility(
          this.form.controls.ergaenzungsleistungenPartner,
          partnerRequired,
        );
        this.hiddenFieldSet.setFieldVisibility(
          this.form.controls.sozialhilfebeitraegePartner,
          partnerRequired,
        );
      },
      { allowSignalWrites: true },
    );
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
      'arbeitsverhaeltnis',
      'saeule3a',
      'saeule2',
      'kinderalimente',
      'ergaenzungsleistungen',
      'ergaenzungsleistungenPartner',
      'sozialhilfebeitraege',
      'ergaenzungsleistungenPartner',
      'vermoegen',
      'wohnkosten',
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

const defaultVater = (steuerdatenTyp: SteuerdatenTyp) => {
  if (steuerdatenTyp !== 'FAMILIE') {
    return steuerdatenTyp;
  }
  return 'VATER';
};
