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
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { MaskitoDirective } from '@maskito/angular';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';
import { subYears } from 'date-fns';
import { Observable, Subject } from 'rxjs';

import { selectLanguage } from '@dv/shared/data-access/language';
import {
  ElternTyp,
  ElternUpdate,
  Land,
  MASK_SOZIALVERSICHERUNGSNUMMER,
  SharedModelGesuchFormular,
} from '@dv/shared/model/gesuch';
import { capitalized, isDefined, lowercased } from '@dv/shared/model/type-util';
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
  standalone: true,
  imports: [
    CommonModule,
    MaskitoDirective,
    TranslateModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatCheckboxModule,
    MatRadioModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiFormAddressComponent,
    SharedUiStepFormButtonsComponent,
    SharedUiZuvorHintDirective,
    SharedUiFormZuvorHintComponent,
    SharedUiTranslateChangePipe,
    SharedUiFormReadonlyDirective,
    SharedPatternDocumentUploadComponent,
  ],
  templateUrl: './shared-feature-gesuch-form-eltern-editor.component.html',
  styleUrls: ['./shared-feature-gesuch-form-eltern-editor.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormElternEditorComponent {
  private elementRef = inject(ElementRef);
  private formBuilder = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);
  private store = inject(Store);

  gesuchFormularSig = input.required<SharedModelGesuchFormular>();
  elternteilSig = input.required<
    Omit<Partial<ElternUpdate>, 'elternTyp'> &
      Required<Pick<ElternUpdate, 'elternTyp'>>
  >();
  @Input({ required: true }) laender!: Land[];
  @Input({ required: true }) changes: Partial<ElternUpdate> | undefined | null;
  @Output() saveTriggered = new EventEmitter<ElternUpdate>();
  @Output() closeTriggered = new EventEmitter<void>();
  @Output() deleteTriggered = new EventEmitter<string>();
  @Output() formIsUnsaved: Observable<boolean>;

  viewSig = this.store.selectSignal(selectSharedFeatureGesuchFormElternView);
  gotReenabled$ = new Subject<object>();

  private gotReenabledSig = toSignal(this.gotReenabled$);

  readonly MASK_SOZIALVERSICHERUNGSNUMMER = MASK_SOZIALVERSICHERUNGSNUMMER;

  maskitoNumber = maskitoNumber;

  readonly ElternTyp = ElternTyp;

  languageSig = this.store.selectSignal(selectLanguage);

  form = this.formBuilder.group({
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
    telefonnummer: [
      '',
      [Validators.required, sharedUtilValidatorTelefonNummer()],
    ],
    sozialversicherungsnummer: [<string | undefined>undefined, []],
    ergaenzungsleistungen: [<string | null>null, [Validators.required]],
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
    'ergaenzungsleistungen',
    'wohnkosten',
  ]);

  svnIsRequiredSig = signal(false);
  private createUploadOptionsSig = createUploadOptionsFactory(this.viewSig);

  private ergaenzungsleistungChangedSig = toSignal(
    this.form.controls.ergaenzungsleistungen.valueChanges,
  );
  ergaenzungsleistungenDocumentSig = this.createUploadOptionsSig(() => {
    const elternTyp = this.elternteilSig().elternTyp;
    const ergaenzungsleistung =
      fromFormatedNumber(this.ergaenzungsleistungChangedSig() ?? undefined) ??
      0;

    return ergaenzungsleistung > 0
      ? `ELTERN_ERGAENZUNGSLEISTUNGEN_${elternTyp}`
      : null;
  });

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

  constructor() {
    this.formIsUnsaved = observeUnsavedChanges(
      this.form,
      this.saveTriggered,
      this.closeTriggered,
      this.deleteTriggered,
    );
    this.formUtils.registerFormForUnsavedCheck(this);
    // zivilrechtlicher Wohnsitz -> PLZ/Ort enable/disable
    const zivilrechtlichChangedSig = this.formUtils.signalFromChanges(
      this.form.controls.identischerZivilrechtlicherWohnsitz,
      { useDefault: true },
    );
    effect(
      () => {
        this.gotReenabledSig();
        const zivilrechtlichIdentisch = zivilrechtlichChangedSig() === true;
        this.formUtils.setDisabledState(
          this.form.controls.identischerZivilrechtlicherWohnsitzPLZ,
          zivilrechtlichIdentisch,
          true,
        );
        this.formUtils.setDisabledState(
          this.form.controls.identischerZivilrechtlicherWohnsitzOrt,
          zivilrechtlichIdentisch,
          true,
        );
        this.form.controls.identischerZivilrechtlicherWohnsitzPLZ.updateValueAndValidity();
        this.form.controls.identischerZivilrechtlicherWohnsitzOrt.updateValueAndValidity();
      },
      { allowSignalWrites: true },
    );
    const landChangedSig = this.formUtils.signalFromChanges(
      this.form.controls.adresse.controls.land,
      { useDefault: true },
    );

    // make SVN required if CH
    effect(
      () => {
        const land = landChangedSig();
        const svnIsRequired = land === 'CH';

        this.formUtils.setRequired(
          this.form.controls.sozialversicherungsnummer,
          svnIsRequired,
        );
        this.svnIsRequiredSig.set(svnIsRequired);
      },
      { allowSignalWrites: true },
    );

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
      if (otherElternteil && !isDefined(elternteil.wohnkosten)) {
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
    const formValues = convertTempFormToRealValues(this.form, [
      'ausweisbFluechtling',
      'ergaenzungsleistungen',
      'sozialhilfebeitraege',
      'wohnkosten',
    ]);
    const geburtsdatum = parseStringAndPrintForBackendLocalDate(
      formValues.geburtsdatum,
      this.languageSig(),
      subYears(new Date(), MEDIUM_AGE_ADULT),
    );
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
