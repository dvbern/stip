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
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import {
  FormControl,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { MatSelectModule } from '@angular/material/select';
import { MaskitoDirective } from '@maskito/angular';
import { NgbAlert, NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';
import { isAfter, subYears } from 'date-fns';
import { Subject } from 'rxjs';
import { switchMap } from 'rxjs/operators';

import { selectLanguage } from '@dv/shared/data-access/language';
import { SharedDataAccessStammdatenApiEvents } from '@dv/shared/data-access/stammdaten';
import { SharedEventGesuchFormPerson } from '@dv/shared/event/gesuch-form-person';
import {
  Anrede,
  DokumentTyp,
  Land,
  MASK_SOZIALVERSICHERUNGSNUMMER,
  Niederlassungsstatus,
  PATTERN_EMAIL,
  Sprache,
  Wohnsitz,
  Zivilstand,
} from '@dv/shared/model/gesuch';
import { PERSON } from '@dv/shared/model/gesuch-form';
import { AppSettings } from '@dv/shared/pattern/app-settings';
import {
  SharedPatternDocumentUploadComponent,
  createUploadOptionsFactory,
} from '@dv/shared/pattern/document-upload';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { SharedUiFormAddressComponent } from '@dv/shared/ui/form-address';
import { SharedUiFormCountryComponent } from '@dv/shared/ui/form-country';
import { SharedUiInfoOverlayComponent } from '@dv/shared/ui/info-overlay';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { GesuchAppUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import {
  SharedUiWohnsitzSplitterComponent,
  addWohnsitzControls,
  updateWohnsitzControlsState,
  wohnsitzAnteileNumber,
  wohnsitzAnteileString,
} from '@dv/shared/ui/wohnsitz-splitter';
import { SharedUtilCountriesService } from '@dv/shared/util/countries';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
} from '@dv/shared/util/form';
import {
  fromFormatedNumber,
  maskitoNumber,
} from '@dv/shared/util/maskito-util';
import { sharedUtilValidatorAhv } from '@dv/shared/util/validator-ahv';
import {
  maxDateValidatorForLocale,
  minDateValidatorForLocale,
  onDateInputBlur,
  parseBackendLocalDateAndPrint,
  parseDateForVariant,
  parseStringAndPrintForBackendLocalDate,
  parseableDateValidatorForLocale,
} from '@dv/shared/util/validator-date';
import { sharedUtilValidatorTelefonNummer } from '@dv/shared/util/validator-telefon-nummer';
import { isDefined } from '@dv/shared/util-fn/type-guards';

import { selectSharedFeatureGesuchFormPersonView } from './shared-feature-gesuch-form-person.selector';

const MIN_AGE_GESUCHSSTELLER = 10;
const MAX_AGE_GESUCHSSTELLER = 130;
const MEDIUM_AGE_GESUCHSSTELLER = 20;

@Component({
  selector: 'dv-shared-feature-gesuch-form-person',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TranslateModule,
    MaskitoDirective,
    MatFormFieldModule,
    MatInputModule,
    MatCheckboxModule,
    MatSelectModule,
    MatRadioModule,
    SharedUiInfoOverlayComponent,
    NgbInputDatepicker,
    NgbAlert,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiFormCountryComponent,
    SharedUiWohnsitzSplitterComponent,
    SharedUiFormAddressComponent,
    SharedPatternDocumentUploadComponent,
    GesuchAppUiStepFormButtonsComponent,
    SharedUiLoadingComponent,
  ],
  templateUrl: './shared-feature-gesuch-form-person.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormPersonComponent implements OnInit {
  private elementRef = inject(ElementRef);
  private store = inject(Store);
  private formBuilder = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);
  private countriesService = inject(SharedUtilCountriesService);
  readonly MASK_SOZIALVERSICHERUNGSNUMMER = MASK_SOZIALVERSICHERUNGSNUMMER;
  readonly anredeValues = Object.values(Anrede);
  readonly Zivilstand = Zivilstand;
  readonly spracheValues = Object.values(Sprache);
  readonly zivilstandValues = Object.values(Zivilstand);
  readonly wohnsitzValues = Object.values(Wohnsitz);
  readonly niederlassungsStatusValues = Object.values(Niederlassungsstatus);
  languageSig = this.store.selectSignal(selectLanguage);
  viewSig = this.store.selectSignal(selectSharedFeatureGesuchFormPersonView);

  private createUploadOptionsSig = createUploadOptionsFactory(this.viewSig);

  updateValidity$ = new Subject<unknown>();
  laenderSig = computed(() => this.viewSig().laender);
  translatedLaender$ = toObservable(this.laenderSig).pipe(
    switchMap((laender) => this.countriesService.getCountryList(laender)),
  );
  appSettings = inject(AppSettings);
  hiddenFieldsSetSig = signal(new Set());
  isSozialversicherungsnummerInfoShown = false;
  isNiederlassungsstatusInfoShown = false;
  nationalitaetCH = 'CH';
  maskitoNumber = maskitoNumber;
  auslaenderausweisDocumentOptionsSig = this.createUploadOptionsSig(() => {
    const niederlassungsstatus = this.niederlassungsstatusChangedSig();
    const niederlassungsstatusMap = {
      [Niederlassungsstatus.AUFENTHALTSBEWILLIGUNG_B]:
        DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_B,
      [Niederlassungsstatus.NIEDERLASSUNGSBEWILLIGUNG_C]:
        DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_C,
      [Niederlassungsstatus.FLUECHTLING]:
        DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_COMPLETE,
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
  heimatortDocumentOptionsSig = this.createUploadOptionsSig((view) => {
    const eltern = view().gesuchFormular?.elterns;
    const plz = this.plzChangedSig();
    return plz &&
      +plz <= 4000 &&
      +plz >= 3000 &&
      eltern?.some((e) => e.adresse.land !== 'CH')
      ? DokumentTyp.PERSON_AUSWEIS
      : null;
  });
  vermoegensnachweisVorjahrDocumentOptionsSig = this.createUploadOptionsSig(
    () => {
      return DokumentTyp.PERSON_VERMOEGENSNACHWEIS_VORJAHR;
    },
  );

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
    identischerZivilrechtlicherWohnsitzPLZ: [
      <string | undefined>undefined,
      [Validators.required],
    ],
    identischerZivilrechtlicherWohnsitzOrt: [
      <string | undefined>undefined,
      [Validators.required],
    ],
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
    nationalitaet: this.formBuilder.control(<Land | undefined>undefined, {
      validators: Validators.required,
    }),
    heimatort: [<string | undefined>undefined, [Validators.required]],
    niederlassungsstatus: this.formBuilder.control<
      Niederlassungsstatus | undefined
    >(undefined, { validators: Validators.required }),
    einreisedatum: [
      <string | undefined>undefined,
      [parseableDateValidatorForLocale(this.languageSig(), 'date')],
    ],
    vormundschaft: [false, []],
    zivilstand: this.formBuilder.control<Zivilstand>('' as Zivilstand, {
      validators: Validators.required,
    }),
    ...addWohnsitzControls(this.formBuilder),
    vermoegenVorjahr: [<string | undefined>undefined, [Validators.required]],
    sozialhilfebeitraege: [<boolean | null>null, [Validators.required]],
    korrespondenzSprache: this.formBuilder.control<Sprache>('' as Sprache, {
      validators: Validators.required,
    }),
  });

  showWohnsitzSplitterSig = computed(() => {
    return this.wohnsitzChangedSig() === Wohnsitz.MUTTER_VATER;
  });

  showEinreiseDatumWarningSig = signal(false);

  private wohnsitzChangedSig = toSignal(
    this.form.controls.wohnsitz.valueChanges,
  );
  private niederlassungsstatusChangedSig = toSignal(
    this.form.controls.niederlassungsstatus.valueChanges,
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
  private plzChangedSig = toSignal(
    this.form.controls.adresse.controls.plz.valueChanges,
  );

  constructor() {
    this.formUtils.registerFormForUnsavedCheck(this);
    effect(
      () => {
        updateWohnsitzControlsState(
          this.formUtils,
          this.form.controls,
          this.viewSig().readonly,
        );
        this.updateVisbility(
          this.form.controls.wohnsitzAnteilMutter,
          this.showWohnsitzSplitterSig(),
          { resetOnInvisible: true },
        );
        this.updateVisbility(
          this.form.controls.wohnsitzAnteilVater,
          this.showWohnsitzSplitterSig(),
          { resetOnInvisible: true },
        );
      },
      { allowSignalWrites: true },
    );
    // patch form value
    effect(
      () => {
        const { gesuchFormular } = this.viewSig();

        const svValidators = [
          Validators.required,
          sharedUtilValidatorAhv('personInAusbildung', gesuchFormular),
        ];
        this.form.controls.sozialversicherungsnummer.clearValidators();
        this.form.controls.sozialversicherungsnummer.addValidators(
          svValidators,
        );
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
          this.form.patchValue({
            ...personForForm,
            vermoegenVorjahr: person.vermoegenVorjahr?.toString(),
            ...wohnsitzAnteileString(person),
          });
        } else {
          this.form.reset();
        }
      },
      { allowSignalWrites: true },
    );
    const zivilrechtlichChangedSig = this.formUtils.signalFromChanges(
      this.form.controls.identischerZivilrechtlicherWohnsitz,
      { useDefault: true },
    );
    effect(
      () => {
        const zivilrechtlichIdentisch = zivilrechtlichChangedSig() === true;
        this.updateVisibilityAndDisabledState(
          this.form.controls.identischerZivilrechtlicherWohnsitzPLZ,
          !zivilrechtlichIdentisch,
          this.viewSig().readonly,
        );
        this.updateVisibilityAndDisabledState(
          this.form.controls.identischerZivilrechtlicherWohnsitzOrt,
          !zivilrechtlichIdentisch,
          this.viewSig().readonly,
        );
        this.form.controls.identischerZivilrechtlicherWohnsitzPLZ.updateValueAndValidity();
        this.form.controls.identischerZivilrechtlicherWohnsitzOrt.updateValueAndValidity();
      },
      { allowSignalWrites: true },
    );

    const nationalitaetChangedSig = toSignal(
      this.form.controls.nationalitaet.valueChanges,
    );
    effect(
      () => {
        const nationalitaetChanged = nationalitaetChangedSig();
        // If nationality is Switzerland
        if (this.form.controls.nationalitaet.value === this.nationalitaetCH) {
          this.updateVisibilityAndDisabledState(
            this.form.controls.heimatort,
            true,
            this.viewSig().readonly,
          );
          this.updateVisibilityAndDisabledState(
            this.form.controls.vormundschaft,
            true,
            this.viewSig().readonly,
          );
          this.updateVisbility(this.form.controls.niederlassungsstatus, false, {
            resetOnInvisible: true,
          });
        }
        // No nationality was selected
        else if (!isDefined(nationalitaetChanged)) {
          this.updateVisbility(this.form.controls.niederlassungsstatus, false, {
            resetOnInvisible: true,
          });
          this.updateVisbility(this.form.controls.heimatort, false, {
            resetOnInvisible: true,
          });
          this.updateVisbility(this.form.controls.vormundschaft, false, {
            resetOnInvisible: true,
          });
        }
        // Any other nationality was selected
        else {
          this.updateVisibilityAndDisabledState(
            this.form.controls.niederlassungsstatus,
            true,
            this.viewSig().readonly,
          );
          this.updateVisbility(this.form.controls.heimatort, false, {
            resetOnInvisible: true,
          });
          this.updateVisbility(this.form.controls.vormundschaft, false, {
            resetOnInvisible: true,
          });
        }
      },
      { allowSignalWrites: true },
    );

    const niederlassungsstatusChangedSig = toSignal(
      this.form.controls.niederlassungsstatus.valueChanges,
    );
    effect(
      () => {
        // Niederlassung B -> required einreisedatum
        const showEinreisedatum =
          niederlassungsstatusChangedSig() ===
          Niederlassungsstatus.AUFENTHALTSBEWILLIGUNG_B;
        this.formUtils.setRequired(
          this.form.controls.einreisedatum,
          showEinreisedatum,
        );
        this.updateVisbility(
          this.form.controls.einreisedatum,
          showEinreisedatum,
          {
            resetOnInvisible: true,
          },
        );
      },
      { allowSignalWrites: true },
    );

    effect(
      () => {
        const niederlassungsstatus = this.niederlassungsstatusChangedSig();
        const plz = this.plzChangedSig();

        this.updateVisibilityAndDisabledState(
          this.form.controls.vermoegenVorjahr,
          isFluechtlingOrHasAusweisB(niederlassungsstatus) ||
            (!!plz && !isFromBern(plz)),
          this.viewSig().readonly,
        );
      },
      { allowSignalWrites: true },
    );

    const einreisedatumChangedSig = toSignal(
      this.form.controls.einreisedatum.valueChanges,
    );
    effect(
      () => {
        const einreisedatum = parseDateForVariant(
          einreisedatumChangedSig(),
          new Date(),
          'date',
        );
        this.showEinreiseDatumWarningSig.set(
          einreisedatum
            ? isAfter(einreisedatum, subYears(new Date(), 5))
            : false,
        );
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
    this.store.dispatch(SharedEventGesuchFormPerson.init());
    this.store.dispatch(SharedDataAccessStammdatenApiEvents.init());
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

  trackByIndex(index: number) {
    return index;
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
    const values = convertTempFormToRealValues(this.form, [
      'nationalitaet',
      'sozialhilfebeitraege',
    ]);
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
          vermoegenVorjahr: fromFormatedNumber(values.vermoegenVorjahr),
          ...wohnsitzAnteileNumber(values),
        },
      },
    };
  }

  private updateVisibilityAndDisabledState(
    formControl: FormControl,
    visibile: boolean,
    disabled: boolean,
    opts = { resetOnInvisible: true },
  ): void {
    this.formUtils.setDisabledState(formControl, disabled);
    this.updateVisbility(formControl, visibile, opts);
  }

  private updateVisbility(
    formControl: FormControl,
    visible: boolean,
    opts: { resetOnInvisible: boolean },
  ): void {
    this.hiddenFieldsSetSig.update((hiddenFieldsSet) => {
      if (visible) {
        hiddenFieldsSet.delete(formControl);
        formControl.enable();
      } else {
        hiddenFieldsSet.add(formControl);
        formControl.disable();
        if (opts.resetOnInvisible) {
          formControl.reset();
        }
      }
      return hiddenFieldsSet;
    });
  }
}

export function isFluechtlingOrHasAusweisB(
  niederlassungsstatus?: Niederlassungsstatus,
) {
  return (
    !!niederlassungsstatus &&
    [
      Niederlassungsstatus.AUFENTHALTSBEWILLIGUNG_B,
      Niederlassungsstatus.FLUECHTLING,
    ].includes(niederlassungsstatus)
  );
}

export function isFromBern(plz?: string) {
  return !!plz && +plz < 4000 && +plz >= 3000;
}
