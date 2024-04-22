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
import { MaskitoModule } from '@maskito/angular';
import { NgbAlert } from '@ng-bootstrap/ng-bootstrap';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';

import { selectLanguage } from '@dv/shared/data-access/language';
import { SharedEventGesuchFormEinnahmenkosten } from '@dv/shared/event/gesuch-form-einnahmenkosten';
import { DokumentTyp } from '@dv/shared/model/gesuch';
import {
  AUSBILDUNG,
  EINNAHMEN_KOSTEN,
  FAMILIENSITUATION,
  PERSON,
} from '@dv/shared/model/gesuch-form';
import {
  SharedPatternDocumentUploadComponent,
  createUploadOptionsFactory,
} from '@dv/shared/pattern/document-upload';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { GesuchAppUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
} from '@dv/shared/util/form';
import {
  fromFormatedNumber,
  maskitoMaxNumber,
  maskitoNumber,
  maskitoPositiveNumber,
} from '@dv/shared/util/maskito-util';
import {
  getDateDifference,
  parseBackendLocalDateAndPrint,
} from '@dv/shared/util/validator-date';
import { sharedUtilValidatorRange } from '@dv/shared/util/validator-range';

import { selectSharedFeatureGesuchFormEinnahmenkostenView } from './shared-feature-gesuch-form-einnahmenkosten.selector';

@Component({
  selector: 'dv-shared-feature-gesuch-form-einnahmenkosten',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatRadioModule,
    MaskitoModule,
    NgbAlert,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    GesuchAppUiStepFormButtonsComponent,
    SharedUiLoadingComponent,
    SharedPatternDocumentUploadComponent,
  ],
  templateUrl: './shared-feature-gesuch-form-einnahmenkosten.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormEinnahmenkostenComponent implements OnInit {
  private store = inject(Store);
  private formBuilder = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);
  private elementRef = inject(ElementRef);

  form = this.formBuilder.group({
    nettoerwerbseinkommen: [<string | null>null, [Validators.required]],
    alimente: [<string | null>null, [Validators.required]],
    zulagen: [<string | null>null, [Validators.required]],
    renten: [<string | null>null, [Validators.required]],
    eoLeistungen: [<string | undefined>undefined, []],
    ergaenzungsleistungen: [<string | undefined>undefined, []],
    beitraege: [<string | undefined>undefined, []],
    ausbildungskostenSekundarstufeZwei: [
      <string | null>null,
      [Validators.required],
    ],
    ausbildungskostenTertiaerstufe: [
      <string | null>null,
      [Validators.required],
    ],
    fahrkosten: [<string | null>null, [Validators.required]],
    wohnkosten: [<string | null>null, [Validators.required]],
    betreuungskostenKinder: [<string | null>null, [Validators.required]],
    verdienstRealisiert: [<boolean | null>null, [Validators.required]],
    willDarlehen: [<boolean | undefined>undefined, [Validators.required]],
    auswaertigeMittagessenProWoche: [
      <number | null>null,
      [Validators.required, sharedUtilValidatorRange(0, 5)],
    ],
    wgWohnend: [<boolean | null>null, [Validators.required]],
  });

  viewSig = this.store.selectSignal(
    selectSharedFeatureGesuchFormEinnahmenkostenView,
  );
  languageSig = this.store.selectSignal(selectLanguage);
  //TODO: KSTIP-619 replace harcoded values with stammdaten
  maskitoTeritaer = maskitoMaxNumber(3000);
  maskitoSekundaer = maskitoMaxNumber(2000);
  maskitoNumber = maskitoNumber;
  maskitoPositiveNumber = maskitoPositiveNumber;
  hiddenFieldsSetSig = signal(new Set());

  private createUploadOptionsSig = createUploadOptionsFactory(this.viewSig);

  formStateSig = computed(() => {
    const { gesuchFormular, ausbildungsstaettes } = this.viewSig();

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
    const { personInAusbildung, familiensituation, kinds, ausbildung } =
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
      personInAusbildung.geburtsdatum,
      this.languageSig(),
    );
    const istErwachsen = !geburtsdatum
      ? false
      : (getDateDifference(geburtsdatum, new Date())?.years ?? 0) > 18;
    // TODO: Use stammdaten info once available
    const ausbildungsgang = ausbildungsstaettes
      .find(
        (a) =>
          a.ausbildungsgaenge?.some(
            (g) => g.id === ausbildung.ausbildungsgang.id,
          ),
      )
      ?.ausbildungsgaenge?.find((a) => a.id === ausbildung.ausbildungsgang.id);
    const willSekundarstufeZwei = ausbildungsgang?.bezeichnungDe === 'Bachelor';
    const willTertiaerstufe =
      ausbildungsgang?.bezeichnungDe?.includes('Master');

    return {
      hasData: true,
      hatElternteilVerloren,
      hatKinder,
      willSekundarstufeZwei,
      willTertiaerstufe,
      istErwachsen,
    } as const;
  });

  nettoerwerbseinkommenSig = toSignal(
    this.form.controls.nettoerwerbseinkommen.valueChanges,
  );

  nettoerwerbseinkommenDocumentSig = this.createUploadOptionsSig(() => {
    const nettoerwerbseinkommen = fromFormatedNumber(
      this.nettoerwerbseinkommenSig() ?? '0',
    );

    return nettoerwerbseinkommen > 0 ? DokumentTyp.EK_LOHNABRECHNUNG : null;
  });

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

  constructor() {
    this.formUtils.registerFormForUnsavedCheck(this);
    effect(
      () => {
        const {
          hasData,
          hatElternteilVerloren,
          hatKinder,
          // willSekundarstufeZwei,
          // willTertiaerstufe,
          istErwachsen,
        } = this.formStateSig();

        const {
          wohnsitzNotEigenerHaushalt,
          existiertGerichtlicheAlimentenregelung,
        } = this.viewSig();

        if (!hasData) {
          return;
        }

        this.setDisabledStateAndHide(
          this.form.controls.renten,
          !hatElternteilVerloren,
        );
        this.formUtils.setRequired(this.form.controls.zulagen, hatKinder);
        // KSTIP-918: use correct sekundarstufe/tertiaerstufe check once properties are available
        // <
        this.setDisabledStateAndHide(
          this.form.controls.ausbildungskostenSekundarstufeZwei,
          false,
        );
        this.setDisabledStateAndHide(
          this.form.controls.ausbildungskostenTertiaerstufe,
          false,
        );
        // >
        this.setDisabledStateAndHide(
          this.form.controls.willDarlehen,
          !istErwachsen,
        );
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
          this.form.controls.betreuungskostenKinder,
          !hatKinder,
        );
      },
      { allowSignalWrites: true },
    );

    // fill form
    effect(
      () => {
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
            ausbildungskostenSekundarstufeZwei:
              einnahmenKosten.ausbildungskostenSekundarstufeZwei?.toString(),
            ausbildungskostenTertiaerstufe:
              einnahmenKosten.ausbildungskostenTertiaerstufe?.toString(),
            fahrkosten: einnahmenKosten.fahrkosten.toString(),
            wohnkosten: einnahmenKosten.wohnkosten?.toString(),
            betreuungskostenKinder:
              einnahmenKosten.betreuungskostenKinder?.toString(),
          });
        } else {
          this.form.reset();
        }
      },
      { allowSignalWrites: true },
    );

    effect(
      () => {
        const { readonly } = this.viewSig();
        if (readonly) {
          Object.values(this.form.controls).forEach((control) =>
            control.disable(),
          );
        }
      },
      { allowSignalWrites: true },
    );
  }

  ngOnInit() {
    this.store.dispatch(SharedEventGesuchFormEinnahmenkosten.init());
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
      'ausbildungskostenSekundarstufeZwei',
      'ausbildungskostenTertiaerstufe',
      'fahrkosten',
      'wohnkosten',
      'wgWohnend',
      'verdienstRealisiert',
      'auswaertigeMittagessenProWoche',
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
          ausbildungskostenSekundarstufeZwei: fromFormatedNumber(
            formValues.ausbildungskostenSekundarstufeZwei,
          ),
          ausbildungskostenTertiaerstufe: fromFormatedNumber(
            formValues.ausbildungskostenTertiaerstufe,
          ),
          fahrkosten: fromFormatedNumber(formValues.fahrkosten),
          wohnkosten: fromFormatedNumber(formValues.wohnkosten),
          betreuungskostenKinder: fromFormatedNumber(
            formValues.betreuungskostenKinder,
          ),
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
