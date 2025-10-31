import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  OnInit,
  computed,
  effect,
  inject,
  input,
  signal,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import {
  FormControl,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { MaskitoDirective } from '@maskito/angular';
import { Store } from '@ngrx/store';
import { Observable, Subject } from 'rxjs';

import { AusbildungsstaetteStore } from '@dv/shared/data-access/ausbildungsstaette';
import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import { selectLanguage } from '@dv/shared/data-access/language';
import { SharedEventGesuchFormEinnahmenkosten } from '@dv/shared/event/gesuch-form-einnahmenkosten';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import { Bildungskategorie, DokumentTyp } from '@dv/shared/model/gesuch';
import {
  AUSBILDUNG,
  EINNAHMEN_KOSTEN,
  EINNAHMEN_KOSTEN_PARTNER,
  FAMILIENSITUATION,
  PERSON,
} from '@dv/shared/model/gesuch-form';
import { isDefined } from '@dv/shared/model/type-util';
import { MAX_EINKOMMEN } from '@dv/shared/model/ui-constants';
import {
  SharedPatternDocumentUploadComponent,
  createUploadOptionsFactory,
} from '@dv/shared/pattern/document-upload';
import { SharedUiAdvTranslocoDirective } from '@dv/shared/ui/adv-transloco-directive';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiFormReadonlyDirective,
  SharedUiFormZuvorHintComponent,
  SharedUiZuvorHintDirective,
} from '@dv/shared/ui/form';
import { SharedUiInfoContainerComponent } from '@dv/shared/ui/info-container';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import { SharedUiRdIsPendingPipe } from '@dv/shared/ui/remote-data-pipe';
import { SharedUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import { SharedUiTranslateChangePipe } from '@dv/shared/ui/translate-change';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
  percentStringToNumber,
} from '@dv/shared/util/form';
import {
  fromFormatedNumber,
  maskitoMaxNumber,
  maskitoNumber,
  maskitoPercent,
  toFormatedNumber,
} from '@dv/shared/util/maskito-util';
import {
  getDateDifference,
  getLastDayOfYear,
  parseBackendLocalDateAndPrint,
} from '@dv/shared/util/validator-date';
import { sharedUtilValidatorRange } from '@dv/shared/util/validator-range';
import { prepareSteuerjahrValidation } from '@dv/shared/util/validator-steuerdaten';

import { selectSharedFeatureGesuchFormEinnahmenkostenView } from './shared-feature-gesuch-form-einnahmenkosten.selector';

enum EinkommenTyp {
  PIA = 'PIA',
  PARTNER = 'PARTNER',
}

const bildungskategorieMap = {
  SEKUNDARSTUFE_I: 'SEKUNDAR_2',
  SEKUNDARSTUFE_II: 'SEKUNDAR_2',
  TERTIAERSTUFE_A: 'TERTIAER',
  TERTIAERSTUFE_B: 'TERTIAER',
} satisfies Record<Bildungskategorie, 'SEKUNDAR_2' | 'TERTIAER'>;
type MainBildungskategorie =
  (typeof bildungskategorieMap)[keyof typeof bildungskategorieMap];
const MIN_WG_ANZAHL_PERSONEN = 2;

@Component({
  standalone: true,
  selector: 'dv-shared-feature-gesuch-form-einnahmenkosten',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatRadioModule,
    MaskitoDirective,
    SharedUiRdIsPendingPipe,
    SharedUiInfoContainerComponent,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiStepFormButtonsComponent,
    SharedUiLoadingComponent,
    SharedUiZuvorHintDirective,
    SharedUiFormZuvorHintComponent,
    SharedUiTranslateChangePipe,
    SharedPatternDocumentUploadComponent,
    SharedUiFormReadonlyDirective,
    SharedUiMaxLengthDirective,
    SharedUiAdvTranslocoDirective,
  ],
  templateUrl: './shared-feature-gesuch-form-einnahmenkosten.component.html',
  styleUrl: './shared-feature-gesuch-form-einnahmenkosten.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormEinnahmenkostenComponent implements OnInit {
  private store = inject(Store);
  private formBuilder = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);
  private elementRef = inject(ElementRef);
  private config = inject(SharedModelCompileTimeConfig);
  private einreichenStore = inject(EinreichenStore);
  einkommenTyp = input(undefined, {
    transform: (v: string | undefined): EinkommenTyp | undefined => {
      if (v && v in EinkommenTyp) {
        return v as EinkommenTyp;
      }
      return undefined;
    },
  });

  form = this.formBuilder.group({
    nettoerwerbseinkommen: [<string | null>null, [Validators.required]],
    arbeitspensumProzent: [
      <string | undefined>undefined,
      [Validators.required],
    ],
    zulagen: [<string | null>null, [Validators.required]],
    renten: [<string | undefined>undefined],
    eoLeistungen: [<string | undefined>undefined],
    ergaenzungsleistungen: [<string | undefined>undefined],
    beitraege: [<string | undefined>undefined],
    ausbildungskosten: [<string | null>null, [Validators.required]],
    fahrkosten: [<string | null>null, [Validators.required]],
    wohnkosten: [<string | null>null, [Validators.required]],
    betreuungskostenKinder: [<string | null>null, [Validators.required]],
    unterhaltsbeitraege: [<string | undefined>undefined],
    einnahmenBGSA: [<string | undefined>undefined],
    taggelderAHVIV: [<string | undefined>undefined],
    andereEinnahmen: [<string | undefined>undefined],
    verpflegungskosten: [<string | undefined>undefined, [Validators.required]],
    auswaertigeMittagessenProWoche: [
      <number | null>null,
      [Validators.required, sharedUtilValidatorRange(0, 5)],
    ],
    wgWohnend: [<boolean | null>null, [Validators.required]],
    wgAnzahlPersonen: [
      <number | undefined>undefined,
      [Validators.required, Validators.min(MIN_WG_ANZAHL_PERSONEN)],
    ],
    alternativeWohnformWohnend: [
      <boolean | undefined>undefined,
      [Validators.required],
    ],
    vermoegen: [
      <string | undefined>undefined,
      [
        Validators.required,
        /* See `vermoegenValidator` bellow */
      ],
    ],
    steuern: [<string | null>null, []],
    veranlagungsStatus: [<string | null>null, [Validators.required]],
    steuerjahr: [
      <number | null>null,
      [
        /** @see // this.steuerjahrValidation */
      ],
    ],
  });

  ausbildungsstaetteStore = inject(AusbildungsstaetteStore);
  viewSig = this.store.selectSignal(
    selectSharedFeatureGesuchFormEinnahmenkostenView,
  );
  gotReenabled$ = new Subject<object>();
  languageSig = this.store.selectSignal(selectLanguage);

  formChangesSig = computed(() => {
    const isEKPartner = this.einkommenTyp() === 'PARTNER';

    return isEKPartner
      ? this.viewSig().formChangesPartner
      : this.viewSig().formChanges;
  });

  private gotReenabledSig = toSignal(this.gotReenabled$);

  private ausbildungskostenChangedSig = toSignal(
    this.form.controls.ausbildungskosten.valueChanges,
  );

  istAusbildungskostenMoreThanLimitSig = computed(() => {
    const value = this.ausbildungskostenChangedSig();
    const limit = this.formStateSig().ausbildungsKostenLimit;

    return checkLimit(value, limit);
  });

  maskitoNumber = maskitoNumber;
  maskitoMaxNumber = maskitoMaxNumber(MAX_EINKOMMEN);
  maskitoPercent = maskitoPercent;
  hiddenFieldsSetSig = signal(new Set());
  MIN_WG_ANZAHL_PERSONEN = MIN_WG_ANZAHL_PERSONEN;

  private createUploadOptionsSig = createUploadOptionsFactory(this.viewSig);

  steuerjahrValidation = prepareSteuerjahrValidation(
    this.form.controls.steuerjahr,
    this.viewSig,
  );

  formStateSig = computed(() => {
    const { gesuchFormular, gesuch } = this.viewSig();
    const ausbildung = gesuchFormular?.ausbildung;
    const ausbildungsstaettes =
      this.ausbildungsstaetteStore.ausbildungsstaetteViewSig();

    if (!gesuchFormular) {
      return {
        hasData: false as const,
        schritte: [
          PERSON.translationKey,
          AUSBILDUNG.translationKey,
          FAMILIENSITUATION.translationKey,
        ],
      };
    }
    const { personInAusbildung, familiensituation, kinds, partner } =
      gesuchFormular;

    const schritte = [
      ...(!personInAusbildung ? [PERSON.translationKey] : []),
      ...(!ausbildung ? [AUSBILDUNG.translationKey] : []),
      ...(!familiensituation ? [FAMILIENSITUATION.translationKey] : []),
    ];

    if (!personInAusbildung || !familiensituation || !ausbildung) {
      return { hasData: false, schritte } as const;
    }
    const hatElternteilVerloren =
      familiensituation.vaterUnbekanntVerstorben === 'VERSTORBEN' ||
      familiensituation.mutterUnbekanntVerstorben === 'VERSTORBEN';
    const hatKinder = kinds ? kinds.length > 0 : false;
    const geburtsdatum = parseBackendLocalDateAndPrint(
      this.einkommenTyp() === 'PARTNER'
        ? partner?.geburtsdatum
        : personInAusbildung.geburtsdatum,
      this.languageSig(),
    );
    const ausbildungsgang = ausbildungsstaettes
      .find((a) =>
        a.ausbildungsgaenge?.some(
          (g) => g.id === ausbildung.ausbildungsgang?.id,
        ),
      )
      ?.ausbildungsgaenge?.find((a) => a.id === ausbildung.ausbildungsgang?.id);

    const ausbiludungsStufe = ausbildungsgang?.bildungskategorie
      ? bildungskategorieMap[ausbildungsgang.bildungskategorie]
      : undefined;
    const aubildungsKostenMap = {
      SEKUNDAR_2: gesuch?.gesuchsperiode.ausbKosten_SekII,
      TERTIAER: gesuch?.gesuchsperiode.ausbKosten_Tertiaer,
    } satisfies Record<MainBildungskategorie, unknown>;
    const ausbildungsKostenLimit =
      ausbiludungsStufe && aubildungsKostenMap[ausbiludungsStufe];

    // return true if the person was 18 or older by the end of the year before the gesuchsjahr
    const gesuchsjahr: number | undefined =
      gesuch?.gesuchsperiode.gesuchsjahr.technischesJahr;
    const warErwachsenSteuerJahr =
      !geburtsdatum || !gesuchsjahr
        ? false
        : (getDateDifference(geburtsdatum, getLastDayOfYear(gesuchsjahr - 1))
            ?.years ?? 0) >= 18;

    return {
      hasData: true,
      hatElternteilVerloren,
      hatKinder,
      ausbildungsKostenLimit,
      ausbiludungsStufe,
      warErwachsenSteuerJahr,
    } as const;
  });

  private createFieldDocumentSig<T extends keyof typeof this.form.controls>(
    controlName: T,
    dokumentTypBase: keyof typeof DokumentTyp,
    dokumentTypPartner: keyof typeof DokumentTyp,
  ) {
    const valueSig = toSignal(
      this.form.controls[controlName].valueChanges as Observable<
        string | null | undefined
      >,
      { initialValue: undefined },
    );
    return this.createUploadOptionsSig(() => {
      const value = fromFormatedNumber((valueSig() ?? '0') as string);
      return value > 0
        ? DokumentTyp[
            this.einkommenTyp() === 'PARTNER'
              ? dokumentTypPartner
              : dokumentTypBase
          ]
        : null;
    });
  }

  nettoerwerbseinkommenDocumentSig = this.createFieldDocumentSig(
    'nettoerwerbseinkommen',
    'EK_LOHNABRECHNUNG',
    'EK_PARTNER_LOHNABRECHNUNG',
  );

  unterhaltsbeitraegeDocumentSig = this.createFieldDocumentSig(
    'unterhaltsbeitraege',
    'EK_BELEG_UNTERHALTSBEITRAEGE',
    'EK_PARTNER_BELEG_UNTERHALTSBEITRAEGE',
  );

  zulagenDocumentSig = this.createFieldDocumentSig(
    'zulagen',
    'EK_BELEG_KINDERZULAGEN',
    'EK_PARTNER_BELEG_KINDERZULAGEN',
  );

  rentenDocumentSig = this.createFieldDocumentSig(
    'renten',
    'EK_BELEG_BEZAHLTE_RENTEN',
    'EK_PARTNER_BELEG_BEZAHLTE_RENTEN',
  );

  eoLeistungenDocumentSig = this.createFieldDocumentSig(
    'eoLeistungen',
    'EK_ENTSCHEID_ERGAENZUNGSLEISTUNGEN_EO',
    'EK_PARTNER_ENTSCHEID_ERGAENZUNGSLEISTUNGEN_EO',
  );

  ergaenzungsleistungenDocumentSig = this.createFieldDocumentSig(
    'ergaenzungsleistungen',
    'EK_VERFUEGUNG_ERGAENZUNGSLEISTUNGEN',
    'EK_PARTNER_VERFUEGUNG_ERGAENZUNGSLEISTUNGEN',
  );

  beitraegeDocumentSig = this.createFieldDocumentSig(
    'beitraege',
    'EK_VERFUEGUNG_GEMEINDE_INSTITUTION',
    'EK_PARTNER_VERFUEGUNG_GEMEINDE_INSTITUTION',
  );

  fahrkostenDocumentSig = this.createFieldDocumentSig(
    'fahrkosten',
    'EK_BELEG_OV_ABONNEMENT',
    'EK_PARTNER_BELEG_OV_ABONNEMENT',
  );

  wohnkostenDocumentSig = this.createFieldDocumentSig(
    'wohnkosten',
    'EK_MIETVERTRAG',
    'EK_PARTNER_MIETVERTRAG',
  );

  wgWohnendSig = toSignal(this.form.controls.wgWohnend.valueChanges);

  betreuungskostenKinderDocumentSig = this.createFieldDocumentSig(
    'betreuungskostenKinder',
    'EK_BELEG_BETREUUNGSKOSTEN_KINDER',
    'EK_PARTNER_BELEG_BETREUUNGSKOSTEN_KINDER',
  );

  vermoegenDocumentSig = this.createFieldDocumentSig(
    'vermoegen',
    'EK_VERMOEGEN',
    'EK_PARTNER_VERMOEGEN',
  );

  einnahmenBGSADocumentSig = this.createFieldDocumentSig(
    'einnahmenBGSA',
    'EK_BELEG_EINNAHMEN_BGSA',
    'EK_PARTNER_BELEG_EINNAHMEN_BGSA',
  );

  taggelderAHVIVDocumentSig = this.createFieldDocumentSig(
    'taggelderAHVIV',
    'EK_BELEG_TAGGELDER_AHV_IV',
    'EK_PARTNER_BELEG_TAGGELDER_AHV_IV',
  );

  andereEinnahmenDocumentSig = this.createFieldDocumentSig(
    'andereEinnahmen',
    'EK_BELEG_ANDERE_EINNAHMEN',
    'EK_PARTNER_BELEG_ANDERE_EINNAHMEN',
  );

  constructor() {
    this.formUtils.registerFormForUnsavedCheck(this);
    this.formUtils.observeInvalidFieldsAndMarkControls(
      this.einreichenStore.invalidFormularControlsSig,
      this.form,
    );
    this.form.controls.steuern.disable();
    effect(() => {
      this.gotReenabledSig();
      const { hasData, hatKinder, warErwachsenSteuerJahr } =
        this.formStateSig();

      const isEKPartner = this.einkommenTyp() === 'PARTNER';

      const wgWohnend = this.wgWohnendSig();
      const { wohnsitzNotEigenerHaushalt } = this.viewSig();

      if (!hasData) {
        return;
      }

      this.formUtils.setRequired(this.form.controls.zulagen, hatKinder);

      this.setDisabledStateAndHide(
        this.form.controls.verpflegungskosten,
        !isEKPartner,
      );

      this.setDisabledStateAndHide(
        this.form.controls.auswaertigeMittagessenProWoche,
        !wohnsitzNotEigenerHaushalt,
      );
      this.setDisabledStateAndHide(
        this.form.controls.wohnkosten,
        isEKPartner || wohnsitzNotEigenerHaushalt,
      );
      this.setDisabledStateAndHide(
        this.form.controls.wgWohnend,
        isEKPartner || wohnsitzNotEigenerHaushalt,
      );
      this.setDisabledStateAndHide(
        this.form.controls.wgAnzahlPersonen,
        wgWohnend !== true,
      );
      this.setDisabledStateAndHide(
        this.form.controls.alternativeWohnformWohnend,
        wgWohnend !== false,
      );
      this.setDisabledStateAndHide(
        this.form.controls.betreuungskostenKinder,
        !hatKinder,
      );
      this.setDisabledStateAndHide(
        this.form.controls.vermoegen,
        !warErwachsenSteuerJahr,
      );
      this.setDisabledStateAndHide(
        this.form.controls.veranlagungsStatus,
        this.config.isGesuchApp,
      );
      this.setDisabledStateAndHide(
        this.form.controls.steuerjahr,
        this.config.isGesuchApp,
      );
      this.setDisabledStateAndHide(
        this.form.controls.ausbildungskosten,
        isEKPartner,
      );
    });

    // hide and show arbeitsPensum
    const nettoerwerbseinkommenChangedSig = toSignal(
      this.form.controls.nettoerwerbseinkommen.valueChanges,
    );
    effect(() => {
      const nettoerwerbseinkommen = nettoerwerbseinkommenChangedSig();
      const hatNettoerwerbseinkommen =
        fromFormatedNumber((nettoerwerbseinkommen ?? '0') as string) > 0;

      this.setDisabledStateAndHide(
        this.form.controls.arbeitspensumProzent,
        !hatNettoerwerbseinkommen,
      );
    });

    this.steuerjahrValidation.createEffect();

    // fill form
    effect(() => {
      const view = this.viewSig();
      const isEKPartner = this.einkommenTyp() === 'PARTNER';

      const einnahmenKosten = isEKPartner
        ? view.einnahmenKostenPartner
        : view.einnahmenKosten;

      if (einnahmenKosten) {
        this.form.patchValue({
          ...einnahmenKosten,
          nettoerwerbseinkommen:
            einnahmenKosten.nettoerwerbseinkommen.toString(),
          arbeitspensumProzent:
            einnahmenKosten.arbeitspensumProzent?.toString(),
          einnahmenBGSA: einnahmenKosten.einnahmenBGSA?.toString(),
          taggelderAHVIV: einnahmenKosten.taggelderAHVIV?.toString(),
          andereEinnahmen: einnahmenKosten.andereEinnahmen?.toString(),
          unterhaltsbeitraege: einnahmenKosten.unterhaltsbeitraege?.toString(),
          zulagen: einnahmenKosten.zulagen?.toString(),
          renten: einnahmenKosten.renten?.toString(),
          eoLeistungen: einnahmenKosten.eoLeistungen?.toString(),
          ergaenzungsleistungen:
            einnahmenKosten.ergaenzungsleistungen?.toString(),
          beitraege: einnahmenKosten.beitraege?.toString(),
          ausbildungskosten: einnahmenKosten.ausbildungskosten?.toString(),
          fahrkosten: einnahmenKosten.fahrkosten.toString(),
          wohnkosten: einnahmenKosten.wohnkosten?.toString(),
          verpflegungskosten: einnahmenKosten.verpflegungskosten?.toString(),
          betreuungskostenKinder:
            einnahmenKosten.betreuungskostenKinder?.toString(),
          vermoegen: einnahmenKosten.vermoegen?.toString(),
          veranlagungsStatus: einnahmenKosten.veranlagungsStatus,
          steuerjahr: einnahmenKosten.steuerjahr,
          steuern: toFormatedNumber(einnahmenKosten.steuern ?? 0),
        });
      }
    });
  }

  ngOnInit() {
    this.store.dispatch(SharedEventGesuchFormEinnahmenkosten.init());
    this.ausbildungsstaetteStore.loadAusbildungsstaetten$();
  }

  trackByIndex(index: number) {
    return index;
  }

  handleSave(): void {
    this.form.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);
    const { gesuchId, trancheId, gesuchFormular } =
      this.buildUpdatedGesuchFromForm();
    const isEKPartner = this.einkommenTyp() === 'PARTNER';

    if (this.form.valid && gesuchId && trancheId) {
      this.store.dispatch(
        SharedEventGesuchFormEinnahmenkosten.saveTriggered({
          gesuchId,
          trancheId,
          gesuchFormular,
          origin: isEKPartner ? EINNAHMEN_KOSTEN_PARTNER : EINNAHMEN_KOSTEN,
        }),
      );
      this.form.markAsPristine();
    }
  }

  handleContinue() {
    const { gesuch } = this.viewSig();
    const isEKPartner = this.einkommenTyp() === 'PARTNER';

    if (gesuch?.id) {
      this.store.dispatch(
        SharedEventGesuchFormEinnahmenkosten.nextTriggered({
          id: gesuch.id,
          trancheId: gesuch.gesuchTrancheToWorkWith.id,
          origin: isEKPartner ? EINNAHMEN_KOSTEN_PARTNER : EINNAHMEN_KOSTEN,
        }),
      );
    }
  }

  private buildUpdatedGesuchFromForm() {
    const { gesuch, gesuchFormular } = this.viewSig();
    const { hatKinder } = this.formStateSig();
    const formValues = convertTempFormToRealValues(this.form, [
      'nettoerwerbseinkommen',
      'arbeitspensumProzent',
      'ausbildungskosten',
      'fahrkosten',
      'wohnkosten',
      'wgWohnend',
      'auswaertigeMittagessenProWoche',
      'steuerjahr',
      'veranlagungsStatus',
      ...(hatKinder ? (['zulagen', 'betreuungskostenKinder'] as const) : []),
    ]);

    const isEKPartner = this.einkommenTyp() === 'PARTNER';

    const update = {
      ...formValues,
      nettoerwerbseinkommen: fromFormatedNumber(
        formValues.nettoerwerbseinkommen,
      ),
      arbeitspensumProzent: percentStringToNumber(
        formValues.arbeitspensumProzent,
      ),
      einnahmenBGSA: fromFormatedNumber(formValues.einnahmenBGSA),
      taggelderAHVIV: fromFormatedNumber(formValues.taggelderAHVIV),
      andereEinnahmen: fromFormatedNumber(formValues.andereEinnahmen),
      unterhaltsbeitraege: fromFormatedNumber(formValues.unterhaltsbeitraege),
      zulagen: fromFormatedNumber(formValues.zulagen),
      renten: fromFormatedNumber(formValues.renten),
      eoLeistungen: fromFormatedNumber(formValues.eoLeistungen),
      ergaenzungsleistungen: fromFormatedNumber(
        formValues.ergaenzungsleistungen,
      ),
      beitraege: fromFormatedNumber(formValues.beitraege),
      ausbildungskosten: fromFormatedNumber(formValues.ausbildungskosten),
      fahrkosten: fromFormatedNumber(formValues.fahrkosten),
      wohnkosten: fromFormatedNumber(formValues.wohnkosten),
      verpflegungskosten: fromFormatedNumber(formValues.verpflegungskosten),
      betreuungskostenKinder: fromFormatedNumber(
        formValues.betreuungskostenKinder,
      ),
      vermoegen: fromFormatedNumber(formValues.vermoegen),
      steuern: undefined,
      steuerjahr: formValues.steuerjahr,
      veranlagungsStatus: formValues.veranlagungsStatus,
    };

    return {
      gesuchId: gesuch?.id,
      trancheId: gesuch?.gesuchTrancheToWorkWith.id,
      gesuchFormular: isEKPartner
        ? {
            ...gesuchFormular,
            einnahmenKostenPartner: update,
          }
        : {
            ...gesuchFormular,
            einnahmenKosten: update,
          },
    };
  }

  private setDisabledStateAndHide(
    formControl: FormControl,
    disabled: boolean,
  ): void {
    this.formUtils.setDisabledState(formControl, disabled, true);

    this.hiddenFieldsSetSig.update((setToUpdate) => {
      if (disabled) {
        setToUpdate.add(formControl);
      } else {
        setToUpdate.delete(formControl);
      }
      return setToUpdate;
    });
  }

  protected readonly PERSON = PERSON;
}

const checkLimit = (value?: string | null, limit?: number) => {
  if (!isDefined(value) || !isDefined(limit)) {
    return false;
  }

  return fromFormatedNumber(value) > limit;
};
