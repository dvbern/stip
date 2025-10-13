import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnChanges,
  Output,
  inject,
  input,
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
import { MatSelectModule } from '@angular/material/select';
import { TranslocoPipe } from '@jsverse/transloco';
import { MaskitoDirective } from '@maskito/angular';
import { Store } from '@ngrx/store';
import { subYears } from 'date-fns';
import { Observable, Subject } from 'rxjs';

import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { selectLanguage } from '@dv/shared/data-access/language';
import {
  Ausbildungssituation,
  DokumentTyp,
  KindUpdate,
  Wohnsitz,
} from '@dv/shared/model/gesuch';
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
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import { SharedUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import { SharedUiTranslateChangePipe } from '@dv/shared/ui/translate-change';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
  percentStringToNumber,
} from '@dv/shared/util/form';
import {
  fromFormatedNumber,
  maskitoNumber,
  maskitoPercent,
} from '@dv/shared/util/maskito-util';
import { observeUnsavedChanges } from '@dv/shared/util/unsaved-changes';
import {
  maxDateValidatorForLocale,
  minDateValidatorForLocale,
  onDateInputBlur,
  parseBackendLocalDateAndPrint,
  parseStringAndPrintForBackendLocalDate,
  parseableDateValidatorForLocale,
} from '@dv/shared/util/validator-date';

const MAX_AGE_ADULT = 130;
const MIN_AGE_CHILD = 0;
const MEDIUM_AGE = 20;

@Component({
  selector: 'dv-shared-feature-gesuch-form-kinder-editor',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    SharedUiFormFieldDirective,
    TranslocoPipe,
    SharedUiFormMessageErrorDirective,
    MatFormFieldModule,
    MatCheckboxModule,
    MatInputModule,
    MatSelectModule,
    MatRadioModule,
    MaskitoDirective,
    SharedUiZuvorHintDirective,
    SharedUiFormZuvorHintComponent,
    SharedUiTranslateChangePipe,
    SharedPatternDocumentUploadComponent,
    SharedUiStepFormButtonsComponent,
    SharedUiFormReadonlyDirective,
    SharedUiMaxLengthDirective,
  ],
  templateUrl: './shared-feature-gesuch-form-kind-editor.component.html',
  styleUrls: ['./shared-feature-gesuch-form-kind-editor.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormKinderEditorComponent implements OnChanges {
  private elementRef = inject(ElementRef);
  private formBuilder = inject(NonNullableFormBuilder);
  private einreichenStore = inject(EinreichenStore);
  private formUtils = inject(SharedUtilFormService);

  @Input({ required: true }) kind!: Partial<KindUpdate>;
  changesSig = input<Partial<KindUpdate> | undefined | null>(undefined);
  @Output() saveTriggered = new EventEmitter<KindUpdate>();
  @Output() closeTriggered = new EventEmitter<void>();
  @Output() formIsUnsaved: Observable<boolean>;

  private store = inject(Store);
  languageSig = this.store.selectSignal(selectLanguage);
  viewSig = this.store.selectSignal(selectSharedDataAccessGesuchsView);
  gotReenabled$ = new Subject<object>();
  updateValidity$ = new Subject();
  maskitoNumber = maskitoNumber;
  maskitoOptionsPercent = maskitoPercent;

  form = this.formBuilder.group({
    nachname: ['', [Validators.required]],
    vorname: ['', [Validators.required]],
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
          subYears(new Date(), MIN_AGE_CHILD),
          'date',
        ),
      ],
    ],
    wohnsitzAnteilPia: [
      <string | null>null,
      [Validators.required, Validators.minLength(2)],
    ],
    ausbildungssituation: this.formBuilder.control<Ausbildungssituation>(
      '' as Ausbildungssituation,
      [Validators.required],
    ),
    unterhaltsbeitraege: [<string | null>null, [Validators.required]],
    kinderUndAusbildungszulagen: [<string | null>null],
    renten: [<string | null>null],
    ergaenzungsleistungen: [<string | null>null],
    andereEinnahmen: [<string | null>null],
  });

  private gotReenabledSig = toSignal(this.gotReenabled$);
  private createUploadOptionsSig = createUploadOptionsFactory(this.viewSig);

  unterhalsbetraegeChangeSig = toSignal(
    this.form.controls.unterhaltsbeitraege.valueChanges,
  );
  unterhaltsbeitraegeDocumentSig = this.createUploadOptionsSig(() => {
    const unterhaltsbeitraege = fromFormatedNumber(
      this.unterhalsbetraegeChangeSig() ?? '0',
    );

    return unterhaltsbeitraege > 0
      ? DokumentTyp.KINDER_ALIMENTENVERORDUNG
      : null;
  });

  kinderUndAusbildungszulagenChangeSig = toSignal(
    this.form.controls.kinderUndAusbildungszulagen.valueChanges,
  );
  kinderUndAusbildungszulagenDocumentSig = this.createUploadOptionsSig(() => {
    const amount = fromFormatedNumber(
      this.kinderUndAusbildungszulagenChangeSig() ?? '0',
    );
    return amount > 0 ? DokumentTyp.KINDER_UND_AUSBILDUNGSZULAGEN : null;
  });

  rentenChangeSig = toSignal(this.form.controls.renten.valueChanges);
  rentenDocumentSig = this.createUploadOptionsSig(() => {
    const amount = fromFormatedNumber(this.rentenChangeSig() ?? '0');
    return amount > 0 ? DokumentTyp.KINDER_RENTEN : null;
  });

  ergaenzungsleistungenChangeSig = toSignal(
    this.form.controls.ergaenzungsleistungen.valueChanges,
  );
  ergaenzungsleistungenDocumentSig = this.createUploadOptionsSig(() => {
    const amount = fromFormatedNumber(
      this.ergaenzungsleistungenChangeSig() ?? '0',
    );
    return amount > 0 ? DokumentTyp.KINDER_ERGAENZUNGSLEISTUNGEN : null;
  });

  andereEinnahmenChangeSig = toSignal(
    this.form.controls.andereEinnahmen.valueChanges,
  );
  andereEinnahmenDocumentSig = this.createUploadOptionsSig(() => {
    const amount = fromFormatedNumber(this.andereEinnahmenChangeSig() ?? '0');
    return amount > 0 ? DokumentTyp.KINDER_ANDERE_EINNAHMEN : null;
  });

  constructor() {
    this.formIsUnsaved = observeUnsavedChanges(
      this.form,
      this.saveTriggered,
      this.closeTriggered,
    );
    this.formUtils.registerFormForUnsavedCheck(this);
    this.formUtils.observeInvalidFieldsAndMarkControls(
      this.einreichenStore.invalidFormularControlsSig,
      this.form,
    );
  }

  ngOnChanges() {
    this.form.patchValue({
      ...this.kind,
      geburtsdatum: parseBackendLocalDateAndPrint(
        this.kind.geburtsdatum,
        this.languageSig(),
      ),
      wohnsitzAnteilPia: this.kind.wohnsitzAnteilPia?.toString(),
      unterhaltsbeitraege: this.kind.unterhaltsbeitraege?.toString(),
      kinderUndAusbildungszulagen:
        this.kind.kinderUndAusbildungszulagen?.toString(),
      renten: this.kind.renten?.toString(),
      ergaenzungsleistungen: this.kind.ergaenzungsleistungen?.toString(),
      andereEinnahmen: this.kind.andereEinnahmen?.toString(),
    });
  }

  handleSave() {
    this.form.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);
    this.updateValidity$.next({});

    const formValues = convertTempFormToRealValues(this.form, [
      'unterhaltsbeitraege',
      'wohnsitzAnteilPia',
      'kinderUndAusbildungszulagen',
      'renten',
      'ergaenzungsleistungen',
      'andereEinnahmen',
    ]);

    const geburtsdatum = parseStringAndPrintForBackendLocalDate(
      formValues.geburtsdatum,
      this.languageSig(),
      subYears(new Date(), MEDIUM_AGE),
    );
    if (this.form.valid && geburtsdatum) {
      this.saveTriggered.emit({
        ...formValues,
        id: this.kind?.id,
        geburtsdatum,
        wohnsitzAnteilPia: percentStringToNumber(formValues.wohnsitzAnteilPia),
        unterhaltsbeitraege: fromFormatedNumber(formValues.unterhaltsbeitraege),
        kinderUndAusbildungszulagen: fromFormatedNumber(
          formValues.kinderUndAusbildungszulagen,
        ),
        renten: fromFormatedNumber(formValues.renten),
        ergaenzungsleistungen: fromFormatedNumber(
          formValues.ergaenzungsleistungen,
        ),
        andereEinnahmen: fromFormatedNumber(formValues.andereEinnahmen),
      });
      this.form.markAsPristine();
    }
  }

  trackByIndex(index: number) {
    return index;
  }

  handleCancel() {
    this.closeTriggered.emit();
    this.form.markAsPristine();
  }

  onGeburtsdatumBlur() {
    return onDateInputBlur(
      this.form.controls.geburtsdatum,
      subYears(new Date(), MEDIUM_AGE),
      this.languageSig(),
    );
  }

  protected readonly wohnsitzValues = Object.values(Wohnsitz);
  protected readonly ausbildungssituationValues =
    Object.values(Ausbildungssituation);
}
