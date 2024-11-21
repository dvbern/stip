import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  EventEmitter,
  Output,
  computed,
  effect,
  inject,
  input,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import {
  FormControl,
  FormsModule,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MaskitoDirective } from '@maskito/angular';
import { Store } from '@ngrx/store';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { Observable, Subject } from 'rxjs';

import { selectLanguage } from '@dv/shared/data-access/language';
import {
  LebenslaufAusbildungsArt,
  LebenslaufItemUpdate,
  Taetigkeitsart,
  WohnsitzKanton,
} from '@dv/shared/model/gesuch';
import { SharedModelLebenslauf } from '@dv/shared/model/lebenslauf';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiFormReadonlyDirective,
} from '@dv/shared/ui/form';
import { SharedUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
} from '@dv/shared/util/form';
import { observeUnsavedChanges } from '@dv/shared/util/unsaved-changes';
import {
  createDateDependencyValidator,
  createOverlappingValidator,
  maxDateValidatorForLocale,
  minDateValidatorForLocale,
  onMonthYearInputBlur,
  parseableDateValidatorForLocale,
} from '@dv/shared/util/validator-date';

import { selectSharedFeatureGesuchFormLebenslaufVew } from '../shared-feature-gesuch-form-lebenslauf/shared-feature-gesuch-form-lebenslauf.selector';

@Component({
  selector: 'dv-shared-feature-gesuch-form-lebenslauf-editor',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    TranslatePipe,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MaskitoDirective,
    SharedUiStepFormButtonsComponent,
    MatCheckboxModule,
    SharedUiFormReadonlyDirective,
  ],
  templateUrl: './shared-feature-gesuch-form-lebenslauf-editor.component.html',
  styleUrls: ['./shared-feature-gesuch-form-lebenslauf-editor.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormLebenslaufEditorComponent {
  private elementRef = inject(ElementRef);
  private formBuilder = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);
  private translateService = inject(TranslateService);

  itemSig = input.required<Partial<SharedModelLebenslauf>>();
  ausbildungenSig = input.required<LebenslaufItemUpdate[]>();
  maxEndDateSig = input<Date | null>(null);
  minStartDateSig = input<Date | null>(null);

  @Output() saveTriggered = new EventEmitter<LebenslaufItemUpdate>();
  @Output() closeTriggered = new EventEmitter<void>();
  @Output() deleteTriggered = new EventEmitter<string>();
  @Output() formIsUnsaved: Observable<boolean>;

  private store = inject(Store);
  languageSig = this.store.selectSignal(selectLanguage);

  viewSig = this.store.selectSignal(selectSharedFeatureGesuchFormLebenslaufVew);
  gotReenabled$ = new Subject<object>();
  private gotReenabledSig = toSignal(this.gotReenabled$);

  form = this.formBuilder.group({
    taetigkeitsBeschreibung: [
      <string | undefined>undefined,
      [Validators.required],
    ],
    bildungsart: [
      <LebenslaufAusbildungsArt | undefined>undefined,
      [Validators.required],
    ],
    berufsbezeichnung: [<string | undefined>undefined, [Validators.required]],
    fachrichtung: [<string | undefined>undefined, [Validators.required]],
    titelDesAbschlusses: [<string | undefined>undefined, [Validators.required]],
    taetigkeitsart: [
      <Taetigkeitsart | undefined>undefined,
      [Validators.required],
    ],
    von: ['', []],
    bis: ['', []],
    wohnsitz: this.formBuilder.control<WohnsitzKanton>('' as WohnsitzKanton, [
      Validators.required,
    ]),
    ausbildungAbgeschlossen: [false, [Validators.required]],
  });

  bildungsartSig = toSignal(this.form.controls.bildungsart.valueChanges);
  startChangedSig = toSignal(this.form.controls.von.valueChanges);
  endChangedSig = toSignal(this.form.controls.bis.valueChanges);
  showBerufsbezeichnungSig = computed(
    () =>
      this.bildungsartSig() === 'EIDGENOESSISCHES_BERUFSATTEST' ||
      this.bildungsartSig() === 'EIDGENOESSISCHES_FAEHIGKEITSZEUGNIS',
  );
  showFachrichtungSig = computed(
    () =>
      this.bildungsartSig() === 'BACHELOR_FACHHOCHSCHULE' ||
      this.bildungsartSig() === 'BACHELOR_HOCHSCHULE_UNI' ||
      this.bildungsartSig() === 'MASTER',
  );
  showTitelDesAbschlussesSig = computed(
    () => this.bildungsartSig() === 'ANDERER_BILDUNGSABSCHLUSS',
  );
  kantonValues = this.prepareKantonValues();

  constructor() {
    this.formIsUnsaved = observeUnsavedChanges(
      this.form,
      this.saveTriggered,
      this.closeTriggered,
      this.deleteTriggered,
    );
    this.formUtils.registerFormForUnsavedCheck(this);
    // abhaengige Validierung zuruecksetzen on valueChanges
    effect(
      () => {
        this.startChangedSig();
        this.form.controls.bis.updateValueAndValidity();
      },
      { allowSignalWrites: true },
    );
    effect(
      () => {
        this.endChangedSig();
        this.form.controls.von.updateValueAndValidity();
      },
      { allowSignalWrites: true },
    );
    effect(
      () => {
        this.gotReenabledSig();
        this.formUtils.setDisabledState(
          this.form.controls.berufsbezeichnung,
          this.viewSig().readonly || !this.showBerufsbezeichnungSig(),
          true,
        );
      },
      { allowSignalWrites: true },
    );
    effect(
      () => {
        this.gotReenabledSig();
        this.formUtils.setDisabledState(
          this.form.controls.fachrichtung,
          this.viewSig().readonly || !this.showFachrichtungSig(),
          true,
        );
      },
      { allowSignalWrites: true },
    );
    effect(
      () => {
        this.gotReenabledSig();
        this.formUtils.setDisabledState(
          this.form.controls.titelDesAbschlusses,
          this.viewSig().readonly || !this.showTitelDesAbschlussesSig(),
          true,
        );
      },
      { allowSignalWrites: true },
    );
    const previousAusbildungenSig = computed(() =>
      this.ausbildungenSig()
        .filter((l) => {
          const item = this.itemSig();
          return !item.id || l.id !== item.id;
        })
        .map((a) => [a.von, a.bis] as const),
    );
    effect(() => {
      this.form.controls.von.clearValidators();
      this.form.controls.von.addValidators([
        Validators.required,
        parseableDateValidatorForLocale(this.languageSig(), 'monthYear'),
      ]);
      const minStartDate = this.minStartDateSig();
      if (minStartDate) {
        this.form.controls.von.addValidators([
          minDateValidatorForLocale(
            this.languageSig(),
            minStartDate,
            'monthYear',
          ),
        ]);
      }
    });
    effect(() => {
      this.form.controls.bis.clearValidators();
      this.form.controls.bis.addValidators([
        Validators.required,
        parseableDateValidatorForLocale(this.languageSig(), 'monthYear'),
        createDateDependencyValidator(
          'after',
          this.form.controls.von,
          true,
          new Date(),
          this.languageSig(),
          'monthYear',
        ),
      ]);
      const maxEndDate = this.maxEndDateSig();
      if (maxEndDate) {
        if (this.itemSig().type === 'AUSBILDUNG') {
          this.form.controls.von.addValidators([
            createOverlappingValidator(
              this.form.controls.bis,
              previousAusbildungenSig(),
              new Date(),
              'monthYear',
            ),
          ]);
          this.form.controls.bis.addValidators([
            createOverlappingValidator(
              this.form.controls.von,
              previousAusbildungenSig(),
              new Date(),
              'monthYear',
            ),
          ]);
        }
        this.form.controls.bis.addValidators([
          maxDateValidatorForLocale(
            this.languageSig(),
            maxEndDate,
            'monthYear',
          ),
        ]);
      }
    });
    effect(
      () => {
        const item = this.itemSig();
        if (item) {
          this.form.controls.bildungsart.clearValidators();
          this.form.controls.bildungsart.setValidators([
            item.type === 'AUSBILDUNG'
              ? Validators.required
              : Validators.nullValidator,
          ]);
          this.form.controls.taetigkeitsart.clearValidators();
          this.form.controls.taetigkeitsart.setValidators([
            item.type === 'TAETIGKEIT'
              ? Validators.required
              : Validators.nullValidator,
          ]);
        }

        this.form.patchValue(item);

        if (item.von && item.bis) {
          this.form.controls.bis.markAsTouched();
        }

        if (item.type === 'AUSBILDUNG') {
          this.formUtils.setRequired(this.form.controls.taetigkeitsart, false);
          this.formUtils.setRequired(
            this.form.controls.taetigkeitsBeschreibung,
            false,
          );
          this.formUtils.setRequired(this.form.controls.bildungsart, true);
        }

        if (item.type === 'TAETIGKEIT') {
          this.form.controls.bildungsart.clearValidators();
          this.form.controls.bildungsart.updateValueAndValidity();
          this.form.controls.taetigkeitsart.setValidators(Validators.required);
          this.form.controls.taetigkeitsart.updateValueAndValidity();
          this.form.controls.taetigkeitsBeschreibung.setValidators(
            Validators.required,
          );
          this.form.controls.taetigkeitsBeschreibung.updateValueAndValidity();
        }
      },
      { allowSignalWrites: true },
    );
  }

  private prepareKantonValues() {
    const kantonValues = Object.values(WohnsitzKanton);

    // remove Ausland befor sort
    const indexAusland = kantonValues.indexOf(WohnsitzKanton.AUSLAND);
    if (indexAusland != -1) {
      kantonValues.splice(indexAusland, 1);
    }
    kantonValues.sort((a, b) =>
      this.translateService
        .instant('shared.kanton.' + a)
        .localeCompare(this.translateService.instant('shared.kanton.' + b)),
    );
    //add Ausland after sort
    kantonValues.push(WohnsitzKanton.AUSLAND);
    return kantonValues;
  }

  handleSave() {
    this.form.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);
    this.onDateBlur(this.form.controls.von);
    this.onDateBlur(this.form.controls.bis);
    if (this.form.valid) {
      this.saveTriggered.emit({
        id: this.itemSig().id,
        ...convertTempFormToRealValues(
          this.form,
          this.itemSig().type === 'AUSBILDUNG'
            ? ['bildungsart', 'wohnsitz', 'ausbildungAbgeschlossen']
            : ['taetigkeitsart', 'taetigkeitsBeschreibung'],
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

  handleDelete() {
    const item = this.itemSig();
    if (item.id) {
      this.deleteTriggered.emit(item.id);
    }
  }

  onDateBlur(ctrl: FormControl) {
    return onMonthYearInputBlur(
      ctrl,
      this.minStartDateSig() ?? new Date(),
      this.languageSig(),
    );
  }

  protected readonly bildungsartValues = Object.values(
    LebenslaufAusbildungsArt,
  );
  protected readonly taetigkeitsartValues = Object.values(Taetigkeitsart);
}
