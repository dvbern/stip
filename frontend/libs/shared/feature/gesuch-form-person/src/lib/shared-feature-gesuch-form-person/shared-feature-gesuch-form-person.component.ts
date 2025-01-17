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
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import {
  AbstractControl,
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
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';
import { isAfter, subYears } from 'date-fns';
import { Subject } from 'rxjs';
import { switchMap } from 'rxjs/operators';

import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import { selectLanguage } from '@dv/shared/data-access/language';
import { PlzOrtStore } from '@dv/shared/data-access/plz-ort';
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
  WohnsitzKanton,
  Zivilstand,
  ZustaendigerKanton,
} from '@dv/shared/model/gesuch';
import { PERSON } from '@dv/shared/model/gesuch-form';
import { isDefined } from '@dv/shared/model/type-util';
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
import { SharedUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import { SharedUiTranslateChangePipe } from '@dv/shared/ui/translate-change';
import {
  SharedUiWohnsitzSplitterComponent,
  addWohnsitzControls,
  prepareWohnsitzForm,
} from '@dv/shared/ui/wohnsitz-splitter';
import { SharedUtilCountriesService } from '@dv/shared/util/countries';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
  updateVisbilityAndDisbledState,
} from '@dv/shared/util/form';
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
    TranslatePipe,
    MaskitoDirective,
    MatFormFieldModule,
    MatInputModule,
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
  private plzStore = inject(PlzOrtStore);
  private einreichenStore = inject(EinreichenStore);

  readonly MASK_SOZIALVERSICHERUNGSNUMMER = MASK_SOZIALVERSICHERUNGSNUMMER;
  readonly anredeValues = Object.values(Anrede);
  readonly Zivilstand = Zivilstand;
  readonly spracheValues = Object.values(Sprache);
  readonly zivilstandValues = Object.values(Zivilstand);
  readonly niederlassungsStatusValues = Object.values(Niederlassungsstatus);
  readonly zugstaendigerKantonValues = Object.values(ZustaendigerKanton);

  languageSig = this.store.selectSignal(selectLanguage);
  viewSig = this.store.selectSignal(selectSharedFeatureGesuchFormPersonView);
  gotReenabled$ = new Subject<object>();

  private gotReenabledSig = toSignal(this.gotReenabled$);
  private createUploadOptionsSig = createUploadOptionsFactory(this.viewSig);

  updateValidity$ = new Subject<unknown>();
  laenderSig = computed(() => this.viewSig().laender);
  translatedLaender$ = toObservable(this.laenderSig).pipe(
    switchMap((laender) => this.countriesService.getCountryList(laender)),
  );
  appSettings = inject(AppSettings);
  hiddenFieldsSetSig = signal(new Set<FormControl>());

  nationalitaetCH = 'CH';
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

    const kanton = this.plzStore.getKantonByPlz(plz);

    return kanton &&
      kanton === WohnsitzKanton.BE &&
      eltern?.some((e) => e.adresse.land !== 'CH')
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
    sozialhilfebeitraege: [<boolean | null>null, [Validators.required]],
    korrespondenzSprache: this.formBuilder.control<Sprache>('' as Sprache, {
      validators: Validators.required,
    }),
    zustaendigerKanton: this.formBuilder.control<
      ZustaendigerKanton | undefined
    >(undefined),
  });

  wohnsitzHelper = prepareWohnsitzForm({
    projector: (formular) => formular?.personInAusbildung,
    form: this.form.controls,
    viewSig: this.viewSig,
    refreshSig: this.gotReenabledSig,
  });
  showEinreiseDatumWarningSig = signal(false);

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
    this.form.controls.adresse.controls.plzOrt.controls.plz.valueChanges,
  );

  constructor() {
    this.formUtils.registerFormForUnsavedCheck(this);
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
    effect(
      () => {
        const { gesuchFormular } = this.viewSig();

        const svValidators = [
          Validators.required,
          sharedUtilValidatorAhv('personInAusbildung', gesuchFormular),
          isUniqueSozialversicherungsnummer,
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
            ...this.wohnsitzHelper.wohnsitzAnteileAsString(),
          });
          SharedUiFormAddressComponent.patchForm(
            this.form.controls.adresse,
            personForForm.adresse,
          );
          this.formUtils.invalidateControlIfValidationFails(
            this.form,
            ['wohnsitz'],
            untracked(this.einreichenStore.validationViewSig)
              .invalidFormularProps.specialValidationErrors,
            (value) =>
              this.wohnsitzHelper
                .wohnsitzValuesSig()
                .includes(value as Wohnsitz),
          );
        } else {
          this.form.reset();
        }
      },
      { allowSignalWrites: true },
    );

    // visibility and disabled state for identischerZivilrechtlicherWohnsitz
    const zivilrechtlichChangedSig = this.formUtils.signalFromChanges(
      this.form.controls.identischerZivilrechtlicherWohnsitz,
      { useDefault: true },
    );
    effect(
      () => {
        this.gotReenabledSig();
        const zivilrechtlichIdentisch = zivilrechtlichChangedSig() === true;
        updateVisbilityAndDisbledState({
          hiddenFieldsSetSig: this.hiddenFieldsSetSig,
          formControl:
            this.form.controls.identischerZivilrechtlicherWohnsitzPLZ,
          visible: !zivilrechtlichIdentisch,
          disabled: this.viewSig().readonly,
        });
        updateVisbilityAndDisbledState({
          hiddenFieldsSetSig: this.hiddenFieldsSetSig,
          formControl:
            this.form.controls.identischerZivilrechtlicherWohnsitzOrt,
          visible: !zivilrechtlichIdentisch,
          disabled: this.viewSig().readonly,
        });
        this.form.controls.identischerZivilrechtlicherWohnsitzPLZ.updateValueAndValidity();
        this.form.controls.identischerZivilrechtlicherWohnsitzOrt.updateValueAndValidity();
      },
      { allowSignalWrites: true },
    );

    // visibility and disabled state for heimatort, vormundschaft and niederlassungsstatus
    const nationalitaetChangedSig = toSignal(
      this.form.controls.nationalitaet.valueChanges,
    );
    effect(
      () => {
        this.gotReenabledSig();
        const nationalitaetChanged = nationalitaetChangedSig();
        // If nationality is Switzerland
        if (this.form.controls.nationalitaet.value === this.nationalitaetCH) {
          updateVisbilityAndDisbledState({
            hiddenFieldsSetSig: this.hiddenFieldsSetSig,
            formControl: this.form.controls.heimatort,
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
        else if (!isDefined(nationalitaetChanged)) {
          updateVisbilityAndDisbledState({
            hiddenFieldsSetSig: this.hiddenFieldsSetSig,
            formControl: this.form.controls.niederlassungsstatus,
            visible: false,
            disabled: this.viewSig().readonly,
            resetOnInvisible: true,
          });
          updateVisbilityAndDisbledState({
            hiddenFieldsSetSig: this.hiddenFieldsSetSig,
            formControl: this.form.controls.heimatort,
            visible: false,
            disabled: this.viewSig().readonly,
            resetOnInvisible: true,
          });
          updateVisbilityAndDisbledState({
            hiddenFieldsSetSig: this.hiddenFieldsSetSig,
            formControl: this.form.controls.vormundschaft,
            visible: false,
            disabled: this.viewSig().readonly,
            resetOnInvisible: true,
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
            formControl: this.form.controls.heimatort,
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
      },
      { allowSignalWrites: true },
    );

    // einreisedatum visibility and disabled state
    const niederlassungsstatusChangedSig = toSignal(
      this.form.controls.niederlassungsstatus.valueChanges,
    );
    effect(
      () => {
        this.gotReenabledSig();
        const niederlassungsstatus = niederlassungsstatusChangedSig();

        // Niederlassung B -> required einreisedatum
        const showEinreisedatum =
          niederlassungsstatus ===
          Niederlassungsstatus.AUFENTHALTSBEWILLIGUNG_B;
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

        // Fluechtling -> required zustaendigerKanton
        const showZustaendigerKanton =
          niederlassungsstatus === Niederlassungsstatus.FLUECHTLING;

        this.formUtils.setRequired(
          this.form.controls.zustaendigerKanton,
          showZustaendigerKanton,
        );
        updateVisbilityAndDisbledState({
          hiddenFieldsSetSig: this.hiddenFieldsSetSig,
          formControl: this.form.controls.zustaendigerKanton,
          visible: showZustaendigerKanton,
          disabled: this.viewSig().readonly,
          resetOnInvisible: true,
        });
      },
      { allowSignalWrites: true },
    );

    // einresiedatum warning
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

    // dislable form if readonly
    effect(
      () => {
        const { readonly } = this.viewSig();
        if (readonly) {
          this.form.disable({ emitEvent: false });
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
          ...this.wohnsitzHelper.wohnsitzAnteileFromNumber(),
        },
      },
    };
  }
}
