import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnChanges,
  Output,
  computed,
  effect,
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
import { MaskitoDirective } from '@maskito/angular';
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';
import { subYears } from 'date-fns';
import { Observable, Subject } from 'rxjs';

import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { selectLanguage } from '@dv/shared/data-access/language';
import {
  Ausbildungssituation,
  DokumentTyp,
  KindUpdate,
  Wohnsitz,
} from '@dv/shared/model/gesuch';
import { isDefined } from '@dv/shared/model/type-util';
import {
  SharedPatternDocumentUploadComponent,
  createUploadOptionsFactory,
} from '@dv/shared/pattern/document-upload';
import { SharedUiChangeIndicatorComponent } from '@dv/shared/ui/change-indicator';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiFormReadonlyDirective,
  SharedUiFormZuvorHintComponent,
  SharedUiZuvorHintDirective,
} from '@dv/shared/ui/form';
import { SharedUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import { SharedUiTranslateChangePipe } from '@dv/shared/ui/translate-change';
import { SharedUiWohnsitzSplitterComponent } from '@dv/shared/ui/wohnsitz-splitter';
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
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    SharedUiFormFieldDirective,
    TranslatePipe,
    SharedUiFormMessageErrorDirective,
    MatFormFieldModule,
    MatCheckboxModule,
    MatInputModule,
    MatSelectModule,
    MatRadioModule,
    MaskitoDirective,
    SharedUiWohnsitzSplitterComponent,
    SharedUiZuvorHintDirective,
    SharedUiFormZuvorHintComponent,
    SharedUiTranslateChangePipe,
    SharedPatternDocumentUploadComponent,
    SharedUiStepFormButtonsComponent,
    SharedUiFormReadonlyDirective,
    SharedUiChangeIndicatorComponent,
  ],
  templateUrl: './shared-feature-gesuch-form-kind-editor.component.html',
  styleUrls: ['./shared-feature-gesuch-form-kind-editor.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormKinderEditorComponent implements OnChanges {
  private elementRef = inject(ElementRef);
  private formBuilder = inject(NonNullableFormBuilder);
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
    alimentenregelungExistiert: [<boolean | undefined>false],
    erhalteneAlimentebeitraege: [<string | null>null, [Validators.required]],
  });

  private gotReenabledSig = toSignal(this.gotReenabled$);
  private createUploadOptionsSig = createUploadOptionsFactory(this.viewSig);

  alimentenregelungExistiertSig = toSignal(
    this.form.controls.alimentenregelungExistiert.valueChanges,
  );

  alimentenBeitraegeSig = toSignal(
    this.form.controls.erhalteneAlimentebeitraege.valueChanges,
  );

  alimenteDocumentSig = this.createUploadOptionsSig(() => {
    const alimente = fromFormatedNumber(this.alimentenBeitraegeSig() ?? '0');

    return alimente > 0 ? DokumentTyp.KINDER_ALIMENTENVERORDUNG : null;
  });

  alimentenregelungExistiertChangeSig = computed(() => {
    const changes = this.changesSig();

    if (!changes || changes.erhalteneAlimentebeitraege === undefined) {
      return;
    }

    return changes.erhalteneAlimentebeitraege !== null;
  });

  constructor() {
    this.formIsUnsaved = observeUnsavedChanges(
      this.form,
      this.saveTriggered,
      this.closeTriggered,
    );
    this.formUtils.registerFormForUnsavedCheck(this);

    effect(
      () => {
        this.gotReenabledSig();
        this.formUtils.setDisabledState(
          this.form.controls.erhalteneAlimentebeitraege,
          this.viewSig().readonly || !this.alimentenregelungExistiertSig(),
          !this.viewSig().readonly,
        );
      },
      { allowSignalWrites: true },
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
      erhalteneAlimentebeitraege:
        this.kind.erhalteneAlimentebeitraege?.toString(),
      alimentenregelungExistiert: isDefined(
        this.kind.erhalteneAlimentebeitraege,
      ),
    });
  }

  handleSave() {
    this.form.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);
    this.updateValidity$.next({});

    const formValues = convertTempFormToRealValues(this.form, [
      'erhalteneAlimentebeitraege',
      'wohnsitzAnteilPia',
    ]);
    delete formValues.alimentenregelungExistiert;

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
        erhalteneAlimentebeitraege: fromFormatedNumber(
          formValues.erhalteneAlimentebeitraege,
        ),
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
