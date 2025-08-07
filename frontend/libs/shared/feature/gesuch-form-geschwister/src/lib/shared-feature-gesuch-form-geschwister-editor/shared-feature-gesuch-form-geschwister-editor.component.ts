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
  untracked,
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
import { MatSelectModule } from '@angular/material/select';
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';
import { subYears } from 'date-fns';
import { Observable, Subject } from 'rxjs';

import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { selectLanguage } from '@dv/shared/data-access/language';
import {
  Ausbildungssituation,
  DokumentTyp,
  GeschwisterUpdate,
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
  SharedUiWohnsitzSplitterComponent,
  addWohnsitzControls,
  prepareWohnsitzForm,
} from '@dv/shared/ui/wohnsitz-splitter';
import { SharedUtilFormService } from '@dv/shared/util/form';
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
  selector: 'dv-shared-feature-gesuch-form-geschwister-editor',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    SharedUiFormFieldDirective,
    TranslatePipe,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatRadioModule,
    SharedUiFormMessageErrorDirective,
    SharedUiZuvorHintDirective,
    SharedUiFormZuvorHintComponent,
    SharedUiTranslateChangePipe,
    SharedUiWohnsitzSplitterComponent,
    SharedUiStepFormButtonsComponent,
    SharedPatternDocumentUploadComponent,
    SharedUiFormReadonlyDirective,
    SharedUiMaxLengthDirective,
  ],
  templateUrl: './shared-feature-gesuch-form-geschwister-editor.component.html',
  styleUrls: ['./shared-feature-gesuch-form-geschwister-editor.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormGeschwisterEditorComponent {
  private elementRef = inject(ElementRef);
  private formBuilder = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);
  private einreichenStore = inject(EinreichenStore);

  geschwisterSig = input.required<Partial<GeschwisterUpdate>>({
    // eslint-disable-next-line @angular-eslint/no-input-rename
    alias: 'geschwister',
  });
  @Input({ required: true }) changes:
    | Partial<GeschwisterUpdate>
    | undefined
    | null;

  @Output() saveTriggered = new EventEmitter<GeschwisterUpdate>();
  @Output() closeTriggered = new EventEmitter<void>();
  @Output() formIsUnsaved: Observable<boolean>;

  private store = inject(Store);

  protected readonly ausbildungssituationValues =
    Object.values(Ausbildungssituation);
  languageSig = this.store.selectSignal(selectLanguage);
  viewSig = this.store.selectSignal(selectSharedDataAccessGesuchsView);
  gotReenabled$ = new Subject<object>();
  updateValidity$ = new Subject<unknown>();

  private gotReenabledSig = toSignal(this.gotReenabled$);
  private createUploadOptionsSig = createUploadOptionsFactory(this.viewSig);

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
    ...addWohnsitzControls(this.formBuilder),
    ausbildungssituation: this.formBuilder.control<Ausbildungssituation>(
      '' as Ausbildungssituation,
      [Validators.required],
    ),
  });

  wohnsitzHelper = prepareWohnsitzForm({
    projector: (formular) =>
      formular?.geschwisters?.find((g) => g.id === this.geschwisterSig().id),
    form: this.form.controls,
    viewSig: this.viewSig,
    refreshSig: this.gotReenabledSig,
  });
  ausbildungssituationSig = toSignal(
    this.form.controls.ausbildungssituation.valueChanges,
  );
  ausbildungssituationDocumentSig = this.createUploadOptionsSig(() => {
    const ausbildungssituation = this.ausbildungssituationSig();

    return ausbildungssituation === Ausbildungssituation.IN_AUSBILDUNG
      ? DokumentTyp.GESCHWISTER_BESTAETIGUNG_AUSBILDUNGSSTAETTE
      : null;
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

    effect(() => {
      const geschwister = this.geschwisterSig();

      this.form.patchValue({
        ...geschwister,
        geburtsdatum: parseBackendLocalDateAndPrint(
          geschwister.geburtsdatum,
          this.languageSig(),
        ),
        ...this.wohnsitzHelper.wohnsitzAnteileAsString(),
      });
      this.formUtils.invalidateControlIfValidationFails(
        this.form,
        ['wohnsitz'],
        {
          shouldReset: true,
          specialValidationErrors: untracked(
            this.einreichenStore.validationViewSig,
          ).invalidFormularProps.specialValidationErrors,
          validatorFn: (value) =>
            this.wohnsitzHelper.wohnsitzValuesSig().includes(value as Wohnsitz),
        },
      );
    });
  }

  handleSave() {
    this.form.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);
    this.updateValidity$.next({});
    const geburtsdatum = parseStringAndPrintForBackendLocalDate(
      this.form.getRawValue().geburtsdatum,
      this.languageSig(),
      subYears(new Date(), MEDIUM_AGE),
    );
    if (this.form.valid && geburtsdatum) {
      this.saveTriggered.emit({
        ...this.form.getRawValue(),
        id: this.geschwisterSig().id,
        geburtsdatum,
        wohnsitz: this.form.getRawValue().wohnsitz as Wohnsitz,
        ...this.wohnsitzHelper.wohnsitzAnteileFromNumber(),
      });
      this.form.markAsPristine();
    }
  }

  handleCancel() {
    this.form.markAsPristine();
    this.closeTriggered.emit();
  }

  onGeburtsdatumBlur() {
    return onDateInputBlur(
      this.form.controls.geburtsdatum,
      subYears(new Date(), MEDIUM_AGE),
      this.languageSig(),
    );
  }
}
