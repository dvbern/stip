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
  viewChild,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import {
  FormControl,
  FormsModule,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import {
  MatAutocomplete,
  MatAutocompleteModule,
} from '@angular/material/autocomplete';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { Store } from '@ngrx/store';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { Observable, Subject } from 'rxjs';

import { AusbildungsstaetteStore } from '@dv/shared/data-access/ausbildungsstaette';
import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import { selectLanguage } from '@dv/shared/data-access/language';
import {
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
import { SharedUiInfoDialogDirective } from '@dv/shared/ui/info-dialog';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import {
  SharedUiSearchOptionLabelDirective,
  SharedUiSelectSearchComponent,
} from '@dv/shared/ui/select-search';
import { SharedUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import { TranslatedPropertyPipe } from '@dv/shared/ui/translated-property-pipe';
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
  imports: [
    FormsModule,
    ReactiveFormsModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    TranslatePipe,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatAutocompleteModule,
    MatCheckboxModule,
    SharedUiStepFormButtonsComponent,
    SharedUiFormReadonlyDirective,
    SharedUiMaxLengthDirective,
    SharedUiInfoDialogDirective,
    SharedUiSelectSearchComponent,
    SharedUiSearchOptionLabelDirective,
    TranslatedPropertyPipe,
  ],
  templateUrl: './shared-feature-gesuch-form-lebenslauf-editor.component.html',
  styleUrls: ['./shared-feature-gesuch-form-lebenslauf-editor.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormLebenslaufEditorComponent {
  private elementRef = inject(ElementRef);
  private formBuilder = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);
  private einreichenStore = inject(EinreichenStore);
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
  ausbildungsstatteStore = inject(AusbildungsstaetteStore);
  languageSig = this.store.selectSignal(selectLanguage);

  viewSig = this.store.selectSignal(selectSharedFeatureGesuchFormLebenslaufVew);
  gotReenabled$ = new Subject<object>();
  private gotReenabledSig = toSignal(this.gotReenabled$);
  abschluesseAutocompleteSig = viewChild<MatAutocomplete>(
    'abschluesseAutocomplete',
  );

  form = this.formBuilder.group({
    taetigkeitsBeschreibung: [
      <string | undefined>undefined,
      [Validators.required],
    ],
    abschlussId: [<string | undefined>undefined, [Validators.required]],
    fachrichtungBerufsbezeichnung: [
      <string | undefined>undefined,
      [Validators.required],
    ],
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

  private abschlussIdSig = toSignal(
    this.form.controls.abschlussId.valueChanges,
  );
  selectedAbschlussSig = computed(() => {
    const abschlussId = this.abschlussIdSig();
    const abschluesse = this.ausbildungsstatteStore.abschluesseViewSig();

    return abschluesse.find((a) => a.id === abschlussId);
  });
  startChangedSig = toSignal(this.form.controls.von.valueChanges);
  endChangedSig = toSignal(this.form.controls.bis.valueChanges);
  kantonValues = this.prepareKantonValues();

  constructor() {
    this.ausbildungsstatteStore.loadAbschluesse$();
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
    // abhaengige Validierung zuruecksetzen on valueChanges
    effect(() => {
      this.startChangedSig();
      this.form.controls.bis.updateValueAndValidity();
    });
    effect(() => {
      this.endChangedSig();
      this.form.controls.von.updateValueAndValidity();
    });
    effect(() => {
      this.gotReenabledSig();
      this.formUtils.setDisabledState(
        this.form.controls.fachrichtungBerufsbezeichnung,
        this.viewSig().readonly || !this.selectedAbschlussSig()?.zusatzfrage,
        true,
      );
    });
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
    effect(() => {
      const { abschlussId, ...item } = this.itemSig();
      if (item) {
        this.form.controls.abschlussId.clearValidators();
        this.form.controls.abschlussId.setValidators([
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

      this.form.patchValue({
        ...item,
        abschlussId,
      });

      if (item.von && item.bis) {
        this.form.controls.bis.markAsTouched();
      }

      if (item.type === 'AUSBILDUNG') {
        this.formUtils.setRequired(this.form.controls.taetigkeitsart, false);
        this.formUtils.setRequired(
          this.form.controls.taetigkeitsBeschreibung,
          false,
        );
        this.formUtils.setRequired(this.form.controls.abschlussId, true);
      }

      if (item.type === 'TAETIGKEIT') {
        this.form.controls.abschlussId.clearValidators();
        this.form.controls.abschlussId.updateValueAndValidity();
        this.form.controls.taetigkeitsart.setValidators(Validators.required);
        this.form.controls.taetigkeitsart.updateValueAndValidity();
        this.form.controls.taetigkeitsBeschreibung.setValidators(
          Validators.required,
        );
        this.form.controls.taetigkeitsBeschreibung.updateValueAndValidity();
      }
    });
  }

  private prepareKantonValues() {
    const kantonValues = Object.values(WohnsitzKanton);

    // remove Ausland befor sort
    const indexAusland = kantonValues.indexOf(WohnsitzKanton.AUSLAND);
    if (indexAusland !== -1) {
      kantonValues.splice(indexAusland, 1);
    }
    kantonValues.sort((a, b) =>
      this.translateService
        .instant(`shared.kanton.${a}`)
        .localeCompare(this.translateService.instant(`shared.kanton.${b}`)),
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
      const { abschlussId, ...formValues } = convertTempFormToRealValues(
        this.form,
        this.itemSig().type === 'AUSBILDUNG'
          ? ['abschlussId', 'wohnsitz', 'ausbildungAbgeschlossen']
          : ['taetigkeitsart', 'taetigkeitsBeschreibung'],
      );
      this.saveTriggered.emit({
        id: this.itemSig().id,
        ...formValues,
        abschlussId,
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

  protected readonly taetigkeitsartValues = Object.values(Taetigkeitsart);
}
