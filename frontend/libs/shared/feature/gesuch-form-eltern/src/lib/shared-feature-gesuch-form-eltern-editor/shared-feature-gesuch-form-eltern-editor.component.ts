import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  EventEmitter,
  Input,
  Output,
  effect,
  inject,
  input,
  signal,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { TranslocoPipe } from '@jsverse/transloco';
import { MaskitoDirective } from '@maskito/angular';
import { Store } from '@ngrx/store';
import { subYears } from 'date-fns';
import { Observable, Subject } from 'rxjs';

import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import { LandStore } from '@dv/shared/data-access/land';
import { selectLanguage } from '@dv/shared/data-access/language';
import {
  DokumentTyp,
  ElternTyp,
  ElternUpdate,
  GesuchFormularType,
  MASK_SOZIALVERSICHERUNGSNUMMER,
  Plz,
} from '@dv/shared/model/gesuch';
import { capitalized, isDefined, lowercased } from '@dv/shared/model/type-util';
import { BFSCODE_SCHWEIZ } from '@dv/shared/model/ui-constants';
import {
  SharedPatternDocumentUploadComponent,
  createUploadOptionsFactory,
} from '@dv/shared/pattern/document-upload';
import { SharedUiAppDatePipe } from '@dv/shared/ui/app-date-pipe';
import { SharedUiDownloadButtonDirective } from '@dv/shared/ui/download-button';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiFormReadonlyDirective,
  SharedUiFormZuvorHintComponent,
  SharedUiZuvorHintDirective,
} from '@dv/shared/ui/form';
import { SharedUiFormAddressComponent } from '@dv/shared/ui/form-address';
import { SharedUiIfSachbearbeiterDirective } from '@dv/shared/ui/if-app-type';
import { SharedUiInfoDialogDirective } from '@dv/shared/ui/info-dialog';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import { SharedUiPlzOrtAutocompleteDirective } from '@dv/shared/ui/plz-ort-autocomplete';
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
import { observeUnsavedChanges } from '@dv/shared/util/unsaved-changes';
import { sharedUtilValidatorAhv } from '@dv/shared/util/validator-ahv';
import {
  maxDateValidatorForLocale,
  minDateValidatorForLocale,
  onDateInputBlur,
  parseBackendLocalDateAndPrint,
  parseStringAndPrintForBackendLocalDate,
  parseableDateValidatorForLocale,
} from '@dv/shared/util/validator-date';
import { sharedUtilValidatorTelefonNummer } from '@dv/shared/util/validator-telefon-nummer';

import { selectSharedFeatureGesuchFormElternView } from '../shared-feature-gesuch-form-eltern/shared-feature-gesuch-form-eltern.selector';

const MAX_AGE_ADULT = 130;
const MIN_AGE_ADULT = 10;
const MEDIUM_AGE_ADULT = 40;

@Component({
  selector: 'dv-shared-feature-gesuch-form-eltern-editor',
  imports: [
    CommonModule,
    MaskitoDirective,
    TranslocoPipe,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatAutocompleteModule,
    MatCheckboxModule,
    MatRadioModule,
    SharedUiDownloadButtonDirective,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiFormAddressComponent,
    SharedUiStepFormButtonsComponent,
    SharedUiZuvorHintDirective,
    SharedUiFormZuvorHintComponent,
    SharedUiTranslateChangePipe,
    SharedUiFormReadonlyDirective,
    SharedPatternDocumentUploadComponent,
    SharedUiMaxLengthDirective,
    SharedUiInfoDialogDirective,
    SharedUiIfSachbearbeiterDirective,
    SharedUiPlzOrtAutocompleteDirective,
    SharedUiAppDatePipe,
  ],
  templateUrl: './shared-feature-gesuch-form-eltern-editor.component.html',
  styleUrls: ['./shared-feature-gesuch-form-eltern-editor.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormElternEditorComponent {
  private elementRef = inject(ElementRef);
  private formBuilder = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);
  private einreichenStore = inject(EinreichenStore);
  private store = inject(Store);
  private landStore = inject(LandStore);

  gesuchFormularSig = input.required<GesuchFormularType>();
  elternteilSig = input.required<
    Omit<Partial<ElternUpdate>, 'elternTyp'> &
      Required<Pick<ElternUpdate, 'elternTyp'>>
  >();

  @Input({ required: true }) changes!: Partial<ElternUpdate>;
  @Input({ required: true }) sichtbar!: boolean;
  @Output() saveTriggered = new EventEmitter<ElternUpdate>();
  @Output() closeTriggered = new EventEmitter<void>();
  @Output() deleteTriggered = new EventEmitter<string>();
  @Output() formIsUnsaved: Observable<boolean>;
  @Output() sichtbarChanged = new EventEmitter<boolean>();

  viewSig = this.store.selectSignal(selectSharedFeatureGesuchFormElternView);
  gotReenabled$ = new Subject<object>();

  private gotReenabledSig = toSignal(this.gotReenabled$);

  readonly MASK_SOZIALVERSICHERUNGSNUMMER = MASK_SOZIALVERSICHERUNGSNUMMER;

  maskitoNumber = maskitoNumber;

  readonly ElternTyp = ElternTyp;

  languageSig = this.store.selectSignal(selectLanguage);
  plzValues?: Plz[];

  form = this.formBuilder.group({
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
    telefonnummer: [
      '',
      [Validators.required, sharedUtilValidatorTelefonNummer()],
    ],
    sozialversicherungsnummer: [<string | undefined>undefined, []],
    wohnkosten: [<string | null>null, [Validators.required]],
    geburtsdatum: [
      '',
      [
        Validators.required,
        parseableDateValidatorForLocale(this.languageSig(), 'date'),
        minDateValidatorForLocale(
          this.languageSig(),
          subYears(new Date(), MAX_AGE_ADULT),
          'date',
        ),
        maxDateValidatorForLocale(
          this.languageSig(),
          subYears(new Date(), MIN_AGE_ADULT),
          'date',
        ),
      ],
    ],
    sozialhilfebeitraege: [<boolean | null>null, [Validators.required]],
    ausweisbFluechtling: [<boolean | null>null, [Validators.required]],
  });
  private numberConverter = this.formUtils.createNumberConverter(this.form, [
    'wohnkosten',
  ]);

  svnIsRequiredSig = signal(false);
  private createUploadOptionsSig = createUploadOptionsFactory(this.viewSig);

  private sozialhilfeChangedSig = toSignal(
    this.form.controls.sozialhilfebeitraege.valueChanges,
  );
  sozialhilfeDocumentSig = this.createUploadOptionsSig(() => {
    const elternTyp = this.elternteilSig().elternTyp;
    const sozialhilfebeitraege = this.sozialhilfeChangedSig();

    return sozialhilfebeitraege === true
      ? `ELTERN_SOZIALHILFEBUDGET_${elternTyp}`
      : null;
  });

  private wohnkostenChangedSig = toSignal(
    this.form.controls.wohnkosten.valueChanges,
  );
  wohnkostenDocumentSig = this.createUploadOptionsSig(() => {
    const gesuchFormular = this.gesuchFormularSig();
    const elternTyp = gesuchFormular.familiensituation
      ?.elternVerheiratetZusammen
      ? 'FAMILIE'
      : this.elternteilSig().elternTyp;
    const wohnkosten =
      fromFormatedNumber(this.wohnkostenChangedSig() ?? undefined) ?? 0;

    return wohnkosten > 0
      ? `ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_${elternTyp}`
      : null;
  });

  ausweisbFluechtlingSig = toSignal(
    this.form.controls.ausweisbFluechtling.valueChanges,
  );

  lohnabrechnungVermoegenDocumentSig = this.createUploadOptionsSig(() => {
    const elternTyp = this.elternteilSig().elternTyp;
    const fluechtling = this.ausweisbFluechtlingSig();

    if (fluechtling) {
      return DokumentTyp[`ELTERN_LOHNABRECHNUNG_VERMOEGEN_${elternTyp}`];
    }

    return null;
  });

  constructor() {
    this.formIsUnsaved = observeUnsavedChanges(
      this.form,
      this.saveTriggered,
      this.closeTriggered,
      this.deleteTriggered,
    );
    this.formUtils.registerFormForUnsavedCheck(this);
    this.formUtils.observeInvalidFieldsAndMarkControls(
      this.einreichenStore.invalidFormularControlsSig,
      this.form,
    );
    // zivilrechtlicher Wohnsitz -> PLZ/Ort enable/disable
    const zivilrechtlichChangedSig = this.formUtils.signalFromChanges(
      this.form.controls.identischerZivilrechtlicherWohnsitz,
      { useDefault: true },
    );
    effect(() => {
      this.gotReenabledSig();
      const zivilrechtlichIdentisch = zivilrechtlichChangedSig() === true;
      const zivilrechtlicherWohnsitzPlzOrt =
        this.form.controls.identischerZivilrechtlicherWohnsitzPlzOrt.controls;
      this.formUtils.setDisabledState(
        zivilrechtlicherWohnsitzPlzOrt.plz,
        zivilrechtlichIdentisch,
        true,
      );
      this.formUtils.setDisabledState(
        zivilrechtlicherWohnsitzPlzOrt.ort,
        zivilrechtlichIdentisch,
        true,
      );
      zivilrechtlicherWohnsitzPlzOrt.plz.updateValueAndValidity();
      zivilrechtlicherWohnsitzPlzOrt.ort.updateValueAndValidity();
    });
    const landChangedSig = this.formUtils.signalFromChanges(
      this.form.controls.adresse.controls.landId,
      { useDefault: true },
    );

    // sozialversicherungsnummer required if land is CH
    effect(() => {
      const landId = landChangedSig();
      const laender = this.landStore.landListViewSig();
      const svnIsRequired =
        laender?.find((l) => l.id === landId)?.laendercodeBfs ===
        BFSCODE_SCHWEIZ;

      this.formUtils.setRequired(
        this.form.controls.sozialversicherungsnummer,
        svnIsRequired,
      );
      this.svnIsRequiredSig.set(svnIsRequired);
    });

    effect(() => {
      const elternteil = this.elternteilSig();
      const gesuchFormular = this.gesuchFormularSig();

      this.form.patchValue({
        ...elternteil,
        ...this.numberConverter.toString(elternteil),
        geburtsdatum: parseBackendLocalDateAndPrint(
          elternteil.geburtsdatum,
          this.languageSig(),
        ),
        identischerZivilrechtlicherWohnsitzPlzOrt: {
          plz: elternteil.identischerZivilrechtlicherWohnsitzPLZ,
          ort: elternteil.identischerZivilrechtlicherWohnsitzOrt,
        },
      });

      if (elternteil.adresse) {
        SharedUiFormAddressComponent.patchForm(
          this.form.controls.adresse,
          elternteil.adresse,
        );
      }

      const otherElternteil = gesuchFormular.elterns?.find(
        (e) => e.elternTyp !== elternteil.elternTyp,
      );
      const elternZusammen =
        gesuchFormular.familiensituation?.elternVerheiratetZusammen;
      if (
        otherElternteil?.wohnkosten &&
        !isDefined(elternteil.wohnkosten) &&
        elternZusammen
      ) {
        this.form.controls.wohnkosten.patchValue(
          otherElternteil.wohnkosten.toString(),
        );
      }

      const svValidators = [
        sharedUtilValidatorAhv(
          `eltern${capitalized(lowercased(elternteil.elternTyp))}`,
          gesuchFormular,
        ),
      ];
      this.form.controls.sozialversicherungsnummer.clearValidators();
      this.form.controls.sozialversicherungsnummer.addValidators(svValidators);
    });
  }

  handleSave() {
    this.form.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);
    const { identischerZivilrechtlicherWohnsitzPlzOrt, ...formValues } =
      convertTempFormToRealValues(this.form, [
        'ausweisbFluechtling',
        // 'ergaenzungsleistungen',
        'sozialhilfebeitraege',
        'wohnkosten',
      ]);
    const geburtsdatum = parseStringAndPrintForBackendLocalDate(
      formValues.geburtsdatum,
      this.languageSig(),
      subYears(new Date(), MEDIUM_AGE_ADULT),
    );
    const {
      plz: identischerZivilrechtlicherWohnsitzPLZ,
      ort: identischerZivilrechtlicherWohnsitzOrt,
    } = identischerZivilrechtlicherWohnsitzPlzOrt;
    const elternteil = this.elternteilSig();
    if (this.form.valid && geburtsdatum) {
      this.saveTriggered.emit({
        ...formValues,
        adresse: {
          ...SharedUiFormAddressComponent.getRealValues(
            this.form.controls.adresse,
          ),
          id: elternteil.adresse?.id,
        },
        id: elternteil.id,
        elternTyp: elternteil.elternTyp,
        geburtsdatum,
        identischerZivilrechtlicherWohnsitzPLZ,
        identischerZivilrechtlicherWohnsitzOrt,
        ...this.numberConverter.toNumber(formValues),
      });
      this.form.markAsPristine();
    }
  }

  handleDelete() {
    const elternteilId = this.elternteilSig()?.id;
    if (elternteilId) {
      this.deleteTriggered.emit(elternteilId);
    }
  }

  trackByIndex(index: number) {
    return index;
  }

  handleCancel() {
    this.form.markAsPristine();
    this.closeTriggered.emit();
  }

  onGeburtsdatumBlur() {
    return onDateInputBlur(
      this.form.controls.geburtsdatum,
      subYears(new Date(), MEDIUM_AGE_ADULT),
      this.languageSig(),
    );
  }
}
