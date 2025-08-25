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
  untracked,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import {
  AbstractControl,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { MatSelectModule } from '@angular/material/select';
import { MaskitoDirective } from '@maskito/angular';
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';
import { isAfter, subDays, subYears } from 'date-fns';
import { Subject } from 'rxjs';

import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import { LandStore } from '@dv/shared/data-access/land';
import { selectLanguage } from '@dv/shared/data-access/language';
import { PlzOrtStore } from '@dv/shared/data-access/plz-ort';
import { SharedEventGesuchFormPerson } from '@dv/shared/event/gesuch-form-person';
import {
  Anrede,
  DokumentTyp,
  GesuchFormularUpdate,
  MASK_SOZIALVERSICHERUNGSNUMMER,
  Niederlassungsstatus,
  PATTERN_EMAIL,
  Plz,
  Sprache,
  Wohnsitz,
  WohnsitzKanton,
  Zivilstand,
  ZustaendigeKESB,
  ZustaendigerKanton,
} from '@dv/shared/model/gesuch';
import { PERSON } from '@dv/shared/model/gesuch-form';
import { isDefined } from '@dv/shared/model/type-util';
import { BFSCODE_SCHWEIZ } from '@dv/shared/model/ui-constants';
import { AppSettings } from '@dv/shared/pattern/app-settings';
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
import { SharedUiFormAddressComponent } from '@dv/shared/ui/form-address';
import { SharedUiInfoContainerComponent } from '@dv/shared/ui/info-container';
import { SharedUiInfoDialogDirective } from '@dv/shared/ui/info-dialog';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import { SharedUiPlzOrtAutocompleteDirective } from '@dv/shared/ui/plz-ort-autocomplete';
import { SharedUiSelectSearchComponent } from '@dv/shared/ui/select-search';
import { SharedUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import { SharedUiTranslateChangePipe } from '@dv/shared/ui/translate-change';
import {
  SharedUiWohnsitzSplitterComponent,
  addWohnsitzControls,
  prepareWohnsitzForm,
} from '@dv/shared/ui/wohnsitz-splitter';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
  updateVisbilityAndDisbledState,
} from '@dv/shared/util/form';
import { sharedUtilValidatorAhv } from '@dv/shared/util/validator-ahv';
import {
  BEGRUENDUNGSSCHREIBEN_AGE,
  MAX_AGE_GESUCHSSTELLER,
  MEDIUM_AGE_GESUCHSSTELLER,
  MIN_AGE_GESUCHSSTELLER,
  dateFromDateString,
  dateFromMonthYearString,
  getDateDifference,
  maxDateValidatorForLocale,
  minDateValidatorForLocale,
  onDateInputBlur,
  parseBackendLocalDateAndPrint,
  parseDateForVariant,
  parseStringAndPrintForBackendLocalDate,
  parseableDateValidatorForLocale,
} from '@dv/shared/util/validator-date';
import { sharedUtilValidatorTelefonNummer } from '@dv/shared/util/validator-telefon-nummer';
import { LandLookupService } from '@dv/shared/util-data-access/land-lookup';

import { selectSharedFeatureGesuchFormPersonView } from './shared-feature-gesuch-form-person.selector';

const vorlaeufigAufgenommenF = 'VORLAEUFIG_AUFGENOMMEN_F';
type VorlaeufigAufgenommenF = typeof vorlaeufigAufgenommenF;

type AllNiederlassungsstatusExceptF = Exclude<
  Niederlassungsstatus,
  `${VorlaeufigAufgenommenF}${string}`
>;
type AvailableNiederlassungsstatus =
  | AllNiederlassungsstatusExceptF
  | VorlaeufigAufgenommenF;

type IsNiederlassungsstatusBerechtigt<T extends AvailableNiederlassungsstatus> =
  // Only B and C are considered as "berechtigt" for the gesuch (KSTIP-1993)
  // F is also considered as "berechtigt" but only if fluechtlingsstatus is true
  T extends `${string}_${'B' | 'C'}` ? true : false;
const berechtigteNiederlassungsstatus = {
  SAISONARBEITEND_A: false,
  AUFENTHALTSBEWILLIGUNG_B: true,
  NIEDERLASSUNGSBEWILLIGUNG_C: true,
  PARTNER_ERWERBSTAETIG_UND_KIND_CI: false,
  GRENZGAENGIG_G: false,
  KURZAUFENTHALT_L: false,
  ASYLSUCHEND_N: false,
  SCHUTZBEDUERFTIG_S: false,
  MELDEPFLICHTIG: false,
  DIPLOMATISCHE_FUNKTION: false,
  INTERNATIONALE_FUNKTION: false,
  NICHT_ZUGETEILT: false,
} satisfies {
  [T in AllNiederlassungsstatusExceptF]: IsNiederlassungsstatusBerechtigt<T>;
};

@Component({
  selector: 'dv-shared-feature-gesuch-form-person',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TranslatePipe,
    MaskitoDirective,
    MatFormFieldModule,
    MatInputModule,
    MatAutocompleteModule,
    MatCheckboxModule,
    MatSelectModule,
    MatRadioModule,
    SharedUiInfoContainerComponent,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiWohnsitzSplitterComponent,
    SharedUiFormAddressComponent,
    SharedPatternDocumentUploadComponent,
    SharedUiStepFormButtonsComponent,
    SharedUiLoadingComponent,
    SharedUiFormReadonlyDirective,
    SharedUiInfoDialogDirective,
    SharedUiZuvorHintDirective,
    SharedUiFormZuvorHintComponent,
    SharedUiTranslateChangePipe,
    SharedUiMaxLengthDirective,
    SharedUiSelectSearchComponent,
    SharedUiPlzOrtAutocompleteDirective,
  ],
  templateUrl: './shared-feature-gesuch-form-person.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormPersonComponent implements OnInit {
  private landLookupService = inject(LandLookupService);
  private elementRef = inject(ElementRef);
  private store = inject(Store);
  private formBuilder = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);
  private plzStore = inject(PlzOrtStore);
  private einreichenStore = inject(EinreichenStore);
  private landStore = inject(LandStore);

  readonly MASK_SOZIALVERSICHERUNGSNUMMER = MASK_SOZIALVERSICHERUNGSNUMMER;
  readonly Niederlassungstatus = Niederlassungsstatus;
  readonly anredeValues = Object.values(Anrede);
  readonly Zivilstand = Zivilstand;
  readonly spracheValues = Object.values(Sprache);
  readonly zivilstandValues = Object.values(Zivilstand);
  readonly niederlassungsStatusValues = createNiederlassungsStatusValues();
  readonly zustaendigerKantonValues = Object.values(ZustaendigerKanton);
  readonly zustaendigeKESBValues = Object.values(ZustaendigeKESB);

  laenderSig = this.landLookupService.getCachedLandLookup();
  isValidLandEntry = this.landLookupService.isValidLandEntry;
  languageSig = this.store.selectSignal(selectLanguage);
  viewSig = this.store.selectSignal(selectSharedFeatureGesuchFormPersonView);
  gotReenabled$ = new Subject<object>();

  private gotReenabledSig = toSignal(this.gotReenabled$);
  private createUploadOptionsSig = createUploadOptionsFactory(this.viewSig);

  updateValidity$ = new Subject<unknown>();
  appSettings = inject(AppSettings);
  vorlaeufigAufgenommenF = vorlaeufigAufgenommenF;
  hiddenFieldsSetSig = signal(new Set<AbstractControl>());
  plzValues?: Plz[];

  auslaenderausweisDocumentOptionsSig = this.createUploadOptionsSig(() => {
    const niederlassungsstatus = this.niederlassungsstatusChangedSig();
    const fluechtlingsstatus = this.fluechtlingsstatusChangedSig();
    const zustaendigerKanton = this.zustaendigerKantonChangedSig();

    const getVorlaeufigAufgenommenF = (): DokumentTyp | null => {
      if (!isDefined(fluechtlingsstatus)) {
        return null;
      }

      if (!fluechtlingsstatus) {
        return `PERSON_NIEDERLASSUNGSSTATUS_${vorlaeufigAufgenommenF}_OHNE_FLUECHTLINGSSTATUS`;
      }

      switch (zustaendigerKanton) {
        case ZustaendigerKanton.BERN:
          return `PERSON_NIEDERLASSUNGSSTATUS_${vorlaeufigAufgenommenF}_ZUESTAENDIGER_KANTON_MANDANT`;
        case ZustaendigerKanton.ANDERER_KANTON:
          return `PERSON_NIEDERLASSUNGSSTATUS_${vorlaeufigAufgenommenF}_ANDERER_ZUESTAENDIGER_KANTON`;
        default:
          return null;
      }
    };

    const niederlassungsstatusMap = {
      [Niederlassungsstatus.SAISONARBEITEND_A]:
        DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_SAISONARBEITEND_A,
      [Niederlassungsstatus.AUFENTHALTSBEWILLIGUNG_B]:
        DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_B,
      [Niederlassungsstatus.NIEDERLASSUNGSBEWILLIGUNG_C]:
        DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_C,
      [Niederlassungsstatus.PARTNER_ERWERBSTAETIG_UND_KIND_CI]:
        DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_PARTNER_ERWERBSTAETIG_UND_KIND_CI,
      [vorlaeufigAufgenommenF]: getVorlaeufigAufgenommenF(),
      [Niederlassungsstatus.VORLAEUFIG_AUFGENOMMEN_F_ZUESTAENDIGER_KANTON_MANDANT]:
        DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_VORLAEUFIG_AUFGENOMMEN_F_ZUESTAENDIGER_KANTON_MANDANT,
      [Niederlassungsstatus.VORLAEUFIG_AUFGENOMMEN_F_ANDERER_ZUESTAENDIGER_KANTON]:
        DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_VORLAEUFIG_AUFGENOMMEN_F_ANDERER_ZUESTAENDIGER_KANTON,
      [Niederlassungsstatus.GRENZGAENGIG_G]:
        DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_GRENZGAENGIG_G,
      [Niederlassungsstatus.KURZAUFENTHALT_L]:
        DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_KURZAUFENTHALT_L,
      [Niederlassungsstatus.ASYLSUCHEND_N]:
        DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_ASYLSUCHEND_N,
      [Niederlassungsstatus.DIPLOMATISCHE_FUNKTION]:
        DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_DIPLOMATISCHE_FUNKTION,
      [Niederlassungsstatus.INTERNATIONALE_FUNKTION]:
        DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_INTERNATIONALE_FUNKTION,
      [Niederlassungsstatus.MELDEPFLICHTIG]:
        DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_MELDEPFLICHTIG,
      [Niederlassungsstatus.SCHUTZBEDUERFTIG_S]:
        DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_SCHUTZBEDUERFTIG_S,
      [Niederlassungsstatus.NICHT_ZUGETEILT]:
        DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_NICHT_ZUGETEILT,
    };
    return niederlassungsstatus
      ? niederlassungsstatusMap[niederlassungsstatus]
      : null;
  });
  vormundschaftDocumentOptionsSig = this.createUploadOptionsSig(() => {
    const vormundschaft = this.vormundschaftChangedSig();
    return vormundschaft ? DokumentTyp.PERSON_KESB_ERNENNUNG : null;
  });
  wohnsitzBeiDocumentOptionsSig = this.createUploadOptionsSig(() => {
    const wohnsitzBei = this.wohnsitzBeiChangedSig();
    return wohnsitzBei === Wohnsitz.EIGENER_HAUSHALT
      ? DokumentTyp.PERSON_MIETVERTRAG
      : null;
  });
  sozialhilfebeitraegeDocumentOptionsSig = this.createUploadOptionsSig(() => {
    const sozialhilfebeitraege = this.sozialhilfebeitraegeChangedSig();
    return sozialhilfebeitraege ? DokumentTyp.PERSON_SOZIALHILFEBUDGET : null;
  });
  zivilstandDocumentOptionsSig = this.createUploadOptionsSig(() => {
    const zivilstand = this.zivilstandChangedSig();
    return zivilstand &&
      [
        Zivilstand.GESCHIEDEN_GERICHTLICH,
        Zivilstand.AUFGELOESTE_PARTNERSCHAFT,
      ].includes(zivilstand)
      ? DokumentTyp.PERSON_TRENNUNG_ODER_UNTERHALTS_BELEG
      : null;
  });
  geburtstagDocumentOptionsSig = this.createUploadOptionsSig(() => {
    const geburtstag = dateFromDateString(this.geburtstagChangedSig());
    let ausbildungsbegin = dateFromMonthYearString(
      this.viewSig().gesuchFormular?.ausbildung.ausbildungBegin,
    );

    if (!geburtstag || !ausbildungsbegin) return null;

    ausbildungsbegin = subDays(ausbildungsbegin, 1);
    const alter = getDateDifference(geburtstag, ausbildungsbegin)?.years ?? 0;

    return alter >= BEGRUENDUNGSSCHREIBEN_AGE
      ? DokumentTyp.PERSON_BEGRUENDUNGSSCHREIBEN_ALTER_AUSBILDUNGSBEGIN
      : null;
  });

  heimatortDocumentOptionsSig = this.createUploadOptionsSig((view) => {
    const eltern = view().gesuchFormular?.elterns;
    const plz = this.plzChangedSig();
    const laender = this.landStore.landListViewSig();
    const kanton = this.plzStore.getKantonByPlz(plz);
    const elternNichtSchweizer = eltern?.some((e) => {
      return (
        laender?.find((l) => l.id === e.adresse.landId)?.laendercodeBfs !==
        BFSCODE_SCHWEIZ
      );
    });

    return kanton && kanton === WohnsitzKanton.BE && elternNichtSchweizer
      ? DokumentTyp.PERSON_AUSWEIS
      : null;
  });

  form = this.formBuilder.group({
    sozialversicherungsnummer: ['', []],
    anrede: this.formBuilder.control<Anrede>('' as Anrede, {
      validators: Validators.required,
    }),
    nachname: ['', [Validators.required]],
    vorname: ['', [Validators.required]],
    adresse: SharedUiFormAddressComponent.buildAddressFormGroup(
      this.formBuilder,
    ),
    identischerZivilrechtlicherWohnsitz: [true, []],
    identischerZivilrechtlicherWohnsitzPlzOrt: this.formBuilder.group({
      plz: [<string | undefined>undefined, [Validators.required]],
      ort: [<string | undefined>undefined, [Validators.required]],
    }),
    email: ['', [Validators.required, Validators.pattern(PATTERN_EMAIL)]],
    telefonnummer: [
      '',
      [Validators.required, sharedUtilValidatorTelefonNummer()],
    ],
    geburtsdatum: [
      '',
      [
        Validators.required,
        parseableDateValidatorForLocale(this.languageSig(), 'date'),
        minDateValidatorForLocale(
          this.languageSig(),
          subYears(new Date(), MAX_AGE_GESUCHSSTELLER),
          'date',
        ),

        maxDateValidatorForLocale(
          this.languageSig(),
          subYears(new Date(), MIN_AGE_GESUCHSSTELLER),
          'date',
        ),
      ],
    ],
    nationalitaetId: this.formBuilder.control(<string | undefined>undefined, {
      validators: Validators.required,
    }),
    heimatortPlzOrt: this.formBuilder.group({
      plz: [<string | undefined>undefined, [Validators.required]],
      ort: [<string | undefined>undefined, [Validators.required]],
    }),
    niederlassungsstatus: this.formBuilder.control<
      AvailableNiederlassungsstatus | undefined
    >(undefined, { validators: Validators.required }),
    einreisedatum: [
      <string | undefined>undefined,
      [parseableDateValidatorForLocale(this.languageSig(), 'date')],
    ],
    fluechtlingsstatus: this.formBuilder.control<boolean | undefined>(
      undefined,
    ),
    zustaendigerKanton: this.formBuilder.control<
      ZustaendigerKanton | undefined
    >(undefined),
    vormundschaft: [false, []],
    zivilstand: this.formBuilder.control<Zivilstand>('' as Zivilstand, {
      validators: Validators.required,
    }),
    ...addWohnsitzControls(this.formBuilder),
    sozialhilfebeitraege: [<boolean | null>null, [Validators.required]],
    korrespondenzSprache: this.formBuilder.control<Sprache>('' as Sprache, {
      validators: Validators.required,
    }),
    zustaendigeKESB: this.formBuilder.control<ZustaendigeKESB | undefined>(
      undefined,
    ),
  });

  wohnsitzHelper = prepareWohnsitzForm({
    projector: (formular) => formular?.personInAusbildung,
    form: this.form.controls,
    viewSig: this.viewSig,
    refreshSig: this.gotReenabledSig,
  });
  showEinreiseDatumWarningSig = signal(false);
  showNiederlassungsstatusNichtBerechtigtWarningSig = signal(false);

  private niederlassungsstatusChangedSig = toSignal(
    this.form.controls.niederlassungsstatus.valueChanges,
  );
  private fluechtlingsstatusChangedSig = toSignal(
    this.form.controls.fluechtlingsstatus.valueChanges,
  );
  private vormundschaftChangedSig = toSignal(
    this.form.controls.vormundschaft.valueChanges,
  );
  private wohnsitzBeiChangedSig = toSignal(
    this.form.controls.wohnsitz.valueChanges,
  );
  private sozialhilfebeitraegeChangedSig = toSignal(
    this.form.controls.sozialhilfebeitraege.valueChanges,
  );
  private zivilstandChangedSig = toSignal(
    this.form.controls.zivilstand.valueChanges,
  );
  private geburtstagChangedSig = toSignal(
    this.form.controls.geburtsdatum.valueChanges,
  );
  private plzChangedSig = toSignal(
    this.form.controls.adresse.controls.plzOrt.controls.plz.valueChanges,
  );
  private nationalitaetIdChangedSig = toSignal(
    this.form.controls.nationalitaetId.valueChanges,
  );
  private zustaendigerKantonChangedSig = toSignal(
    this.form.controls.zustaendigerKanton.valueChanges,
  );

  niederlassungstatusChangesSig = computed(() => {
    const niederlassungstatusChanges =
      this.viewSig().formChanges?.niederlassungsstatus;
    const niederlassungstatusCurrent =
      this.viewSig().gesuchFormular?.personInAusbildung?.niederlassungsstatus;

    if (!niederlassungstatusChanges) {
      return null;
    }
    const changed = niederlassungsStatusConverter.from(
      niederlassungstatusChanges,
    );
    const current = niederlassungsStatusConverter.from(
      niederlassungstatusCurrent,
    );

    return {
      ...(changed.fluechtlingsstatus !== current.fluechtlingsstatus
        ? { fluechtlingsstatus: changed.fluechtlingsstatus }
        : {}),
      ...(changed.niederlassungsstatus !== current.niederlassungsstatus
        ? { niederlassungsstatus: changed.niederlassungsstatus }
        : {}),
      ...(changed.zustaendigerKanton !== current.zustaendigerKanton
        ? { zustaendigerKanton: changed.zustaendigerKanton }
        : {}),
    };
  });

  nationalitaetBfsCodeSig = computed(() => {
    const id = this.nationalitaetIdChangedSig();
    const laender = this.landStore.landListViewSig();
    return laender?.find((land) => land.id === id)?.laendercodeBfs;
  });
  bfsCodeSchweiz = BFSCODE_SCHWEIZ;

  constructor() {
    this.landStore.loadLaender$();
    this.formUtils.registerFormForUnsavedCheck(this);
    this.formUtils.observeInvalidFieldsAndMarkControls(
      this.einreichenStore.invalidFormularControlsSig,
      this.form,
    );
    const isUniqueSozialversicherungsnummer = (control: AbstractControl) => {
      const {
        invalidFormularProps: { specialValidationErrors },
      } = untracked(this.einreichenStore.validationViewSig);
      if (
        specialValidationErrors?.some(
          (e) => e.field === 'sozialversicherungsnummer',
        ) &&
        // remove error once the field has changed
        control.pristine
      ) {
        control.markAsTouched();
        return { alreadyUsedAhv: true };
      } else {
        return null;
      }
    };

    // patch form value
    effect(() => {
      const { gesuchFormular } = this.viewSig();

      const svValidators = [
        Validators.required,
        sharedUtilValidatorAhv('personInAusbildung', gesuchFormular),
        isUniqueSozialversicherungsnummer,
      ];
      this.form.controls.sozialversicherungsnummer.clearValidators();
      this.form.controls.sozialversicherungsnummer.addValidators(svValidators);
      if (gesuchFormular?.personInAusbildung) {
        const person = gesuchFormular.personInAusbildung;
        const personForForm = {
          ...person,
          geburtsdatum: parseBackendLocalDateAndPrint(
            person.geburtsdatum,
            this.languageSig(),
          ),
          einreisedatum: parseBackendLocalDateAndPrint(
            person.einreisedatum,
            this.languageSig(),
          ),
        };
        const niederlassungsstatus = personForForm.niederlassungsstatus;
        this.form.patchValue({
          ...personForForm,
          ...this.wohnsitzHelper.wohnsitzAnteileAsString(),
          ...niederlassungsStatusConverter.from(niederlassungsstatus),
          heimatortPlzOrt: {
            plz: personForForm.heimatortPLZ,
            ort: personForForm.heimatort,
          },
          identischerZivilrechtlicherWohnsitzPlzOrt: {
            plz: personForForm.identischerZivilrechtlicherWohnsitzPLZ,
            ort: personForForm.identischerZivilrechtlicherWohnsitzOrt,
          },
        });
        SharedUiFormAddressComponent.patchForm(
          this.form.controls.adresse,
          personForForm.adresse,
        );
        this.formUtils.invalidateControlIfValidationFails(
          this.form,
          ['wohnsitz'],
          {
            shouldReset: true,
            specialValidationErrors: untracked(
              this.einreichenStore.validationViewSig,
            ).invalidFormularProps.specialValidationErrors,
            validatorFn: (value) =>
              this.wohnsitzHelper
                .wohnsitzValuesSig()
                .includes(value as Wohnsitz),
          },
        );
      } else {
        this.form.reset();
      }
    });

    // visibility and disabled state for identischerZivilrechtlicherWohnsitz
    const zivilrechtlichChangedSig = this.formUtils.signalFromChanges(
      this.form.controls.identischerZivilrechtlicherWohnsitz,
      { useDefault: true },
    );
    effect(() => {
      this.gotReenabledSig();
      const zivilrechtlichIdentisch = zivilrechtlichChangedSig() === true;
      updateVisbilityAndDisbledState({
        hiddenFieldsSetSig: this.hiddenFieldsSetSig,
        formControl:
          this.form.controls.identischerZivilrechtlicherWohnsitzPlzOrt.controls
            .plz,
        visible: !zivilrechtlichIdentisch,
        disabled: this.viewSig().readonly,
      });
      updateVisbilityAndDisbledState({
        hiddenFieldsSetSig: this.hiddenFieldsSetSig,
        formControl:
          this.form.controls.identischerZivilrechtlicherWohnsitzPlzOrt.controls
            .ort,
        visible: !zivilrechtlichIdentisch,
        disabled: this.viewSig().readonly,
      });
      this.form.controls.identischerZivilrechtlicherWohnsitzPlzOrt.controls.plz.updateValueAndValidity();
      this.form.controls.identischerZivilrechtlicherWohnsitzPlzOrt.controls.ort.updateValueAndValidity();
    });

    // visibility and disabled state for heimatort, vormundschaft and niederlassungsstatus

    effect(() => {
      this.gotReenabledSig();
      // If nationality is Switzerland, show heimatort and vormundschaft
      const nationalitaetBfsCode = this.nationalitaetBfsCodeSig();
      if (nationalitaetBfsCode === BFSCODE_SCHWEIZ) {
        updateVisbilityAndDisbledState({
          hiddenFieldsSetSig: this.hiddenFieldsSetSig,
          formControl: this.form.controls.heimatortPlzOrt,
          visible: true,
          disabled: this.viewSig().readonly,
        });
        updateVisbilityAndDisbledState({
          hiddenFieldsSetSig: this.hiddenFieldsSetSig,
          formControl: this.form.controls.vormundschaft,
          visible: true,
          disabled: this.viewSig().readonly,
        });
        updateVisbilityAndDisbledState({
          hiddenFieldsSetSig: this.hiddenFieldsSetSig,
          formControl: this.form.controls.niederlassungsstatus,
          visible: false,
          disabled: this.viewSig().readonly,
          resetOnInvisible: true,
        });
      }
      // No nationality was selected
      else if (!isDefined(nationalitaetBfsCode)) {
        updateVisbilityAndDisbledState({
          hiddenFieldsSetSig: this.hiddenFieldsSetSig,
          formControl: this.form.controls.niederlassungsstatus,
          visible: false,
          disabled: this.viewSig().readonly,
          resetOnInvisible: false,
        });
        updateVisbilityAndDisbledState({
          hiddenFieldsSetSig: this.hiddenFieldsSetSig,
          formControl: this.form.controls.heimatortPlzOrt,
          visible: false,
          disabled: this.viewSig().readonly,
          resetOnInvisible: false,
        });
        updateVisbilityAndDisbledState({
          hiddenFieldsSetSig: this.hiddenFieldsSetSig,
          formControl: this.form.controls.vormundschaft,
          visible: false,
          disabled: this.viewSig().readonly,
          resetOnInvisible: false,
        });
      }
      // Any other nationality was selected
      else {
        updateVisbilityAndDisbledState({
          hiddenFieldsSetSig: this.hiddenFieldsSetSig,
          formControl: this.form.controls.niederlassungsstatus,
          visible: true,
          disabled: this.viewSig().readonly,
        });
        updateVisbilityAndDisbledState({
          hiddenFieldsSetSig: this.hiddenFieldsSetSig,
          formControl: this.form.controls.heimatortPlzOrt,
          visible: false,
          disabled: this.viewSig().readonly,
          resetOnInvisible: true,
        });
        updateVisbilityAndDisbledState({
          hiddenFieldsSetSig: this.hiddenFieldsSetSig,
          formControl: this.form.controls.vormundschaft,
          visible: false,
          disabled: this.viewSig().readonly,
        });
      }
    });

    effect(() => {
      this.gotReenabledSig();
      const niederlassungsstatus = this.niederlassungsstatusChangedSig();

      // Niederlassung B -> required einreisedatum
      const showEinreisedatum =
        niederlassungsstatus === Niederlassungsstatus.AUFENTHALTSBEWILLIGUNG_B;
      this.formUtils.setRequired(
        this.form.controls.einreisedatum,
        showEinreisedatum,
      );
      updateVisbilityAndDisbledState({
        hiddenFieldsSetSig: this.hiddenFieldsSetSig,
        formControl: this.form.controls.einreisedatum,
        visible: showEinreisedatum,
        disabled: this.viewSig().readonly,
        resetOnInvisible: true,
      });

      const showFluechtlingsstatus =
        niederlassungsstatus === vorlaeufigAufgenommenF;
      this.formUtils.setRequired(
        this.form.controls.fluechtlingsstatus,
        showFluechtlingsstatus,
      );
      updateVisbilityAndDisbledState({
        hiddenFieldsSetSig: this.hiddenFieldsSetSig,
        formControl: this.form.controls.fluechtlingsstatus,
        visible: showFluechtlingsstatus,
        disabled: this.viewSig().readonly,
        resetOnInvisible: true,
      });
    });

    effect(() => {
      this.gotReenabledSig();
      const niederlassungsstatus = this.niederlassungsstatusChangedSig();
      const fluechtlingsstatus = this.fluechtlingsstatusChangedSig();

      if (niederlassungsstatus) {
        const isNiederlassungsstatusBerechtigt =
          niederlassungsstatus === vorlaeufigAufgenommenF
            ? fluechtlingsstatus
            : berechtigteNiederlassungsstatus[niederlassungsstatus];
        this.showNiederlassungsstatusNichtBerechtigtWarningSig.set(
          !(isNiederlassungsstatusBerechtigt ?? true),
        );
      }
    });

    effect(() => {
      this.gotReenabledSig();
      const fluechtlingsstatus = this.fluechtlingsstatusChangedSig();
      const showZustaendigerKanton = !!fluechtlingsstatus;

      this.formUtils.setRequired(
        this.form.controls.zustaendigerKanton,
        !!fluechtlingsstatus,
      );
      updateVisbilityAndDisbledState({
        hiddenFieldsSetSig: this.hiddenFieldsSetSig,
        formControl: this.form.controls.zustaendigerKanton,
        visible: showZustaendigerKanton,
        disabled: this.viewSig().readonly,
        resetOnInvisible: true,
      });
    });

    effect(() => {
      this.gotReenabledSig();
      const vormundschaft = !!this.vormundschaftChangedSig();

      // Beistandschaft = True -> required zuständiger KESB
      this.formUtils.setRequired(
        this.form.controls.zustaendigeKESB,
        vormundschaft,
      );
      updateVisbilityAndDisbledState({
        hiddenFieldsSetSig: this.hiddenFieldsSetSig,
        formControl: this.form.controls.zustaendigeKESB,
        visible: vormundschaft,
        disabled: this.viewSig().readonly,
        resetOnInvisible: true,
      });
    });
    // einresiedatum warning
    const einreisedatumChangedSig = toSignal(
      this.form.controls.einreisedatum.valueChanges,
    );
    effect(() => {
      const einreisedatum = parseDateForVariant(
        einreisedatumChangedSig(),
        new Date(),
        'date',
      );
      this.showEinreiseDatumWarningSig.set(
        einreisedatum ? isAfter(einreisedatum, subYears(new Date(), 5)) : false,
      );
    });

    // dislable form if readonly
    effect(() => {
      const { readonly } = this.viewSig();
      if (readonly) {
        this.form.disable({ emitEvent: false });
      }
    });
  }

  ngOnInit() {
    this.store.dispatch(SharedEventGesuchFormPerson.init());
  }

  handleSave() {
    this.form.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);
    this.updateValidity$.next({});
    const { gesuchId, trancheId, gesuchFormular } =
      this.buildUpdatedGesuchFromForm();
    if (this.form.valid && gesuchId && trancheId) {
      this.store.dispatch(
        SharedEventGesuchFormPerson.saveTriggered({
          gesuchId,
          trancheId,
          gesuchFormular,
          origin: PERSON,
        }),
      );
      this.form.markAsPristine();
    }
  }

  handleContinue() {
    const { gesuch } = this.viewSig();
    if (gesuch?.id && gesuch?.gesuchTrancheToWorkWith.id) {
      this.store.dispatch(
        SharedEventGesuchFormPerson.nextTriggered({
          id: gesuch.id,
          trancheId: gesuch.gesuchTrancheToWorkWith.id,
          origin: PERSON,
        }),
      );
    }
  }

  onGeburtsdatumBlur() {
    return onDateInputBlur(
      this.form.controls.geburtsdatum,
      subYears(new Date(), MEDIUM_AGE_GESUCHSSTELLER),
      this.languageSig(),
    );
  }

  onEinreisedatumBlur() {
    return onDateInputBlur(
      this.form.controls.einreisedatum,
      new Date(),
      this.languageSig(),
    );
  }

  private buildUpdatedGesuchFromForm() {
    const { gesuch, gesuchFormular } = this.viewSig();
    const {
      fluechtlingsstatus,
      zustaendigerKanton,
      identischerZivilrechtlicherWohnsitzPlzOrt,
      heimatortPlzOrt,
      ...values
    } = convertTempFormToRealValues(this.form, [
      'nationalitaetId',
      'sozialhilfebeitraege',
    ]);

    const {
      plz: identischerZivilrechtlicherWohnsitzPLZ,
      ort: identischerZivilrechtlicherWohnsitzOrt,
    } = identischerZivilrechtlicherWohnsitzPlzOrt;
    const { plz: heimatortPLZ, ort: heimatort } = heimatortPlzOrt;

    return {
      gesuchId: gesuch?.id,
      trancheId: gesuch?.gesuchTrancheToWorkWith?.id,
      gesuchFormular: {
        ...gesuchFormular,
        personInAusbildung: {
          ...values,
          adresse: {
            id: gesuchFormular?.personInAusbildung?.adresse?.id,
            ...SharedUiFormAddressComponent.getRealValues(
              this.form.controls.adresse,
            ),
          },
          identischerZivilrechtlicherWohnsitzOrt,
          identischerZivilrechtlicherWohnsitzPLZ,
          heimatort,
          heimatortPLZ,
          niederlassungsstatus: niederlassungsStatusConverter.to({
            ...values,
            fluechtlingsstatus,
            zustaendigerKanton,
          }),
          geburtsdatum: parseStringAndPrintForBackendLocalDate(
            values.geburtsdatum,
            this.languageSig(),
            subYears(new Date(), MEDIUM_AGE_GESUCHSSTELLER),
          )!,
          einreisedatum: parseStringAndPrintForBackendLocalDate(
            values.einreisedatum,
            this.languageSig(),
            new Date(),
          ),
          ...this.wohnsitzHelper.wohnsitzAnteileFromNumber(),
        },
      } satisfies Partial<GesuchFormularUpdate>,
    };
  }
}

const createNiederlassungsStatusValues = () => {
  const niederlassungsStatus = Object.values(Niederlassungsstatus);

  const filtered: string[] = niederlassungsStatus.filter((status) => {
    return !status.startsWith(vorlaeufigAufgenommenF);
  });
  filtered.splice(
    niederlassungsStatus.indexOf(
      'VORLAEUFIG_AUFGENOMMEN_F_OHNE_FLUECHTLINGSSTATUS',
    ),
    0,
    vorlaeufigAufgenommenF,
  );
  return filtered;
};

type KnownNiederlassungsValues = {
  niederlassungsstatus: AvailableNiederlassungsstatus | undefined;
  fluechtlingsstatus: boolean | undefined;
  zustaendigerKanton: ZustaendigerKanton | undefined;
};

const niederlassungsStatusConverter = {
  from: (
    value: Niederlassungsstatus | undefined,
  ): KnownNiederlassungsValues => {
    switch (value) {
      case 'VORLAEUFIG_AUFGENOMMEN_F_OHNE_FLUECHTLINGSSTATUS':
        return {
          niederlassungsstatus: vorlaeufigAufgenommenF,
          fluechtlingsstatus: false,
          zustaendigerKanton: undefined,
        };
      case 'VORLAEUFIG_AUFGENOMMEN_F_ANDERER_ZUESTAENDIGER_KANTON':
        return {
          niederlassungsstatus: vorlaeufigAufgenommenF,
          fluechtlingsstatus: true,
          zustaendigerKanton: ZustaendigerKanton.ANDERER_KANTON,
        };
      case 'VORLAEUFIG_AUFGENOMMEN_F_ZUESTAENDIGER_KANTON_MANDANT':
        return {
          niederlassungsstatus: vorlaeufigAufgenommenF,
          fluechtlingsstatus: true,
          zustaendigerKanton: ZustaendigerKanton.BERN,
        };
      case undefined:
        return {
          niederlassungsstatus: undefined,
          fluechtlingsstatus: undefined,
          zustaendigerKanton: undefined,
        };
      default:
        return {
          niederlassungsstatus: value,
          fluechtlingsstatus: undefined,
          zustaendigerKanton: undefined,
        };
    }
  },
  to: (value: KnownNiederlassungsValues): Niederlassungsstatus | undefined => {
    if (value.niederlassungsstatus === vorlaeufigAufgenommenF) {
      if (value.fluechtlingsstatus === false) {
        return 'VORLAEUFIG_AUFGENOMMEN_F_OHNE_FLUECHTLINGSSTATUS';
      }
      switch (value.zustaendigerKanton) {
        case ZustaendigerKanton.BERN:
          return 'VORLAEUFIG_AUFGENOMMEN_F_ZUESTAENDIGER_KANTON_MANDANT';
        case ZustaendigerKanton.ANDERER_KANTON:
          return 'VORLAEUFIG_AUFGENOMMEN_F_ANDERER_ZUESTAENDIGER_KANTON';
        default:
          return undefined;
      }
    }
    return value.niederlassungsstatus;
  },
};
