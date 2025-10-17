import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  OnInit,
  computed,
  effect,
  inject,
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
import { Subject } from 'rxjs';

import { AusbildungsstaetteStore } from '@dv/shared/data-access/ausbildungsstaette';
import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import { selectLanguage } from '@dv/shared/data-access/language';
import { SharedEventGesuchFormEinnahmenkosten } from '@dv/shared/event/gesuch-form-einnahmenkosten';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import { Bildungskategorie, DokumentTyp } from '@dv/shared/model/gesuch';
import {
  AUSBILDUNG,
  EINNAHMEN_KOSTEN,
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
import { SharedUiInfoDialogDirective } from '@dv/shared/ui/info-dialog';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import { SharedUiRdIsPendingPipe } from '@dv/shared/ui/remote-data-pipe';
import { SharedUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import { SharedUiTranslateChangePipe } from '@dv/shared/ui/translate-change';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
} from '@dv/shared/util/form';
import {
  fromFormatedNumber,
  maskitoMaxNumber,
  maskitoNumber,
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
    SharedUiInfoDialogDirective,
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

  form = this.formBuilder.group({
    nettoerwerbseinkommen: [<string | null>null, [Validators.required]],
    alimente: [<string | null>null, [Validators.required]],
    zulagen: [<string | null>null, [Validators.required]],
    renten: [<string | null>null, [Validators.required]],
    eoLeistungen: [<string | undefined>undefined, []],
    ergaenzungsleistungen: [<string | undefined>undefined, []],
    beitraege: [<string | undefined>undefined, []],
    ausbildungskosten: [<string | null>null, [Validators.required]],
    fahrkosten: [<string | null>null, [Validators.required]],
    wohnkosten: [<string | null>null, [Validators.required]],
    betreuungskostenKinder: [<string | null>null, [Validators.required]],
    verdienstRealisiert: [<boolean | null>null, [Validators.required]],
    auswaertigeMittagessenProWoche: [
      <number | null>null,
      [Validators.required, sharedUtilValidatorRange(0, 5)],
    ],
    wgWohnend: [<boolean | null>null, [Validators.required]],
    wgAnzahlPersonen: [
      <number | undefined>undefined,
      [Validators.required, Validators.min(MIN_WG_ANZAHL_PERSONEN)],
    ],
    alternativeWohnformWohnend: [<boolean | undefined>undefined, []],
    vermoegen: [
      <string | undefined>undefined,
      [
        Validators.required,
        /* See `vermoegenValidator` bellow */
      ],
    ],
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
    const { personInAusbildung, familiensituation, kinds } = gesuchFormular;

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
      personInAusbildung.geburtsdatum,
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

  nettoerwerbseinkommenSig = toSignal(
    this.form.controls.nettoerwerbseinkommen.valueChanges,
  );

  steuernKantonGemeindeSig = computed(() => {
    const einkommen = fromFormatedNumber(
      this.nettoerwerbseinkommenSig() ?? '0',
    );
    const einkommenPartner =
      this.viewSig().gesuchFormular?.partner?.jahreseinkommen ?? 0;

    const gesamtEinkommen = einkommen + einkommenPartner;

    if (gesamtEinkommen >= 20_000) {
      return toFormatedNumber(gesamtEinkommen * 0.1);
    }

    return 0;
  });

  nettoerwerbseinkommenDocumentSig = this.createUploadOptionsSig(() => {
    const nettoerwerbseinkommen = fromFormatedNumber(
      this.nettoerwerbseinkommenSig() ?? '0',
    );

    return nettoerwerbseinkommen > 0 ? DokumentTyp.EK_LOHNABRECHNUNG : null;
  }, {});

  alimenteSig = toSignal(this.form.controls.alimente.valueChanges);

  alimenteDocumentSig = this.createUploadOptionsSig(() => {
    const alimente = fromFormatedNumber(this.alimenteSig() ?? '0');

    return alimente > 0 ? DokumentTyp.EK_BELEG_ALIMENTE : null;
  });

  zulagenSig = toSignal(this.form.controls.zulagen.valueChanges);

  zulagenDocumentSig = this.createUploadOptionsSig(() => {
    const zulagen = fromFormatedNumber(this.zulagenSig() ?? '0');

    return zulagen > 0 ? DokumentTyp.EK_BELEG_KINDERZULAGEN : null;
  });

  rentenSig = toSignal(this.form.controls.renten.valueChanges);

  rentenDocumentSig = this.createUploadOptionsSig(() => {
    const renten = fromFormatedNumber(this.rentenSig() ?? '0');

    return renten > 0 ? DokumentTyp.EK_BELEG_BEZAHLTE_RENTEN : null;
  });

  eoLeistungenSig = toSignal(this.form.controls.eoLeistungen.valueChanges);

  eoLeistungenDocumentSig = this.createUploadOptionsSig(() => {
    const eoLeistungen = fromFormatedNumber(this.eoLeistungenSig() ?? '0');

    return eoLeistungen > 0
      ? DokumentTyp.EK_ENTSCHEID_ERGAENZUNGSLEISTUNGEN_EO
      : null;
  });

  ergaenzungsleistungenSig = toSignal(
    this.form.controls.ergaenzungsleistungen.valueChanges,
  );

  ergaenzungsleistungenDocumentSig = this.createUploadOptionsSig(() => {
    const ergaenzungsleistungen = fromFormatedNumber(
      this.ergaenzungsleistungenSig() ?? '0',
    );

    return ergaenzungsleistungen > 0
      ? DokumentTyp.EK_VERFUEGUNG_ERGAENZUNGSLEISTUNGEN
      : null;
  });

  beitraegeSig = toSignal(this.form.controls.beitraege.valueChanges);

  beitraegeDocumentSig = this.createUploadOptionsSig(() => {
    const beitraege = fromFormatedNumber(this.beitraegeSig() ?? '0');

    return beitraege > 0
      ? DokumentTyp.EK_VERFUEGUNG_GEMEINDE_INSTITUTION
      : null;
  });

  fahrkostenSig = toSignal(this.form.controls.fahrkosten.valueChanges);

  fahrkostenDocumentSig = this.createUploadOptionsSig(() => {
    const fahrkosten = fromFormatedNumber(this.fahrkostenSig() ?? '0');

    return fahrkosten > 0 ? DokumentTyp.EK_BELEG_OV_ABONNEMENT : null;
  });

  wohnkostenSig = toSignal(this.form.controls.wohnkosten.valueChanges);

  wohnkostenDocumentSig = this.createUploadOptionsSig(() => {
    const wohnkosten = fromFormatedNumber(this.wohnkostenSig() ?? '0');

    return wohnkosten > 0 ? DokumentTyp.EK_MIETVERTRAG : null;
  });

  wgWohnendSig = toSignal(this.form.controls.wgWohnend.valueChanges);

  verdienstRealisiertSig = toSignal(
    this.form.controls.verdienstRealisiert.valueChanges,
  );

  verdienstRealisiertDocumentSig = this.createUploadOptionsSig(() => {
    const verdienstRealisiert = this.verdienstRealisiertSig();

    return verdienstRealisiert ? DokumentTyp.EK_VERDIENST : null;
  });

  betreuungskostenKinderSig = toSignal(
    this.form.controls.betreuungskostenKinder.valueChanges,
  );

  betreuungskostenKinderDocumentSig = this.createUploadOptionsSig(() => {
    const betreuungskostenKinder = fromFormatedNumber(
      this.betreuungskostenKinderSig() ?? '0',
    );

    return betreuungskostenKinder > 0
      ? DokumentTyp.EK_BELEG_BETREUUNGSKOSTEN_KINDER
      : null;
  });

  vermoegenSig = toSignal(this.form.controls.vermoegen.valueChanges);

  vermoegenDocumentSig = this.createUploadOptionsSig(() => {
    const vermoegen = fromFormatedNumber(this.vermoegenSig() ?? '0');

    return vermoegen > 0 ? DokumentTyp.EK_VERMOEGEN : null;
  });

  constructor() {
    this.formUtils.registerFormForUnsavedCheck(this);
    this.formUtils.observeInvalidFieldsAndMarkControls(
      this.einreichenStore.invalidFormularControlsSig,
      this.form,
    );
    effect(() => {
      this.gotReenabledSig();
      const { hasData, hatKinder, warErwachsenSteuerJahr } =
        this.formStateSig();

      const {
        wohnsitzNotEigenerHaushalt,
        existiertGerichtlicheAlimentenregelung,
      } = this.viewSig();
      const wgWohnend = this.wgWohnendSig();

      if (!hasData) {
        return;
      }

      this.formUtils.setRequired(this.form.controls.zulagen, hatKinder);

      this.setDisabledStateAndHide(
        this.form.controls.auswaertigeMittagessenProWoche,
        !wohnsitzNotEigenerHaushalt,
      );
      this.setDisabledStateAndHide(
        this.form.controls.wohnkosten,
        wohnsitzNotEigenerHaushalt,
      );
      this.setDisabledStateAndHide(
        this.form.controls.alimente,
        !existiertGerichtlicheAlimentenregelung,
      );
      this.setDisabledStateAndHide(
        this.form.controls.wgWohnend,
        wohnsitzNotEigenerHaushalt,
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
    });

    this.steuerjahrValidation.createEffect();

    // fill form
    effect(() => {
      const { einnahmenKosten } = this.viewSig();
      if (einnahmenKosten) {
        this.form.patchValue({
          ...einnahmenKosten,
          nettoerwerbseinkommen:
            einnahmenKosten.nettoerwerbseinkommen.toString(),
          alimente: einnahmenKosten.alimente?.toString(),
          zulagen: einnahmenKosten.zulagen?.toString(),
          renten: einnahmenKosten.renten?.toString(),
          eoLeistungen: einnahmenKosten.eoLeistungen?.toString(),
          ergaenzungsleistungen:
            einnahmenKosten.ergaenzungsleistungen?.toString(),
          beitraege: einnahmenKosten.beitraege?.toString(),
          ausbildungskosten: einnahmenKosten.ausbildungskosten?.toString(),
          fahrkosten: einnahmenKosten.fahrkosten.toString(),
          wohnkosten: einnahmenKosten.wohnkosten?.toString(),
          betreuungskostenKinder:
            einnahmenKosten.betreuungskostenKinder?.toString(),
          vermoegen: einnahmenKosten.vermoegen?.toString(),
          veranlagungsStatus: einnahmenKosten.veranlagungsStatus,
          steuerjahr: einnahmenKosten.steuerjahr,
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

    if (this.form.valid && gesuchId && trancheId) {
      this.store.dispatch(
        SharedEventGesuchFormEinnahmenkosten.saveTriggered({
          gesuchId,
          trancheId,
          gesuchFormular,
          origin: EINNAHMEN_KOSTEN,
        }),
      );
      this.form.markAsPristine();
    }
  }

  handleContinue() {
    const { gesuch } = this.viewSig();
    if (gesuch?.id) {
      this.store.dispatch(
        SharedEventGesuchFormEinnahmenkosten.nextTriggered({
          id: gesuch.id,
          trancheId: gesuch.gesuchTrancheToWorkWith.id,
          origin: EINNAHMEN_KOSTEN,
        }),
      );
    }
  }

  private buildUpdatedGesuchFromForm() {
    const { gesuch, gesuchFormular } = this.viewSig();
    const { hatKinder } = this.formStateSig();
    const formValues = convertTempFormToRealValues(this.form, [
      'nettoerwerbseinkommen',
      'alimente',
      'renten',
      'ausbildungskosten',
      'fahrkosten',
      'wohnkosten',
      'wgWohnend',
      'verdienstRealisiert',
      'auswaertigeMittagessenProWoche',
      'steuerjahr',
      'veranlagungsStatus',
      ...(hatKinder ? (['zulagen', 'betreuungskostenKinder'] as const) : []),
    ]);
    return {
      gesuchId: gesuch?.id,
      trancheId: gesuch?.gesuchTrancheToWorkWith.id,
      gesuchFormular: {
        ...gesuchFormular,
        einnahmenKosten: {
          ...formValues,
          nettoerwerbseinkommen: fromFormatedNumber(
            formValues.nettoerwerbseinkommen,
          ),
          alimente: fromFormatedNumber(formValues.alimente),
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
          betreuungskostenKinder: fromFormatedNumber(
            formValues.betreuungskostenKinder,
          ),
          vermoegen: fromFormatedNumber(formValues.vermoegen),
          steuerjahr: formValues.steuerjahr,
          veranlagungsStatus: formValues.veranlagungsStatus,
        },
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
