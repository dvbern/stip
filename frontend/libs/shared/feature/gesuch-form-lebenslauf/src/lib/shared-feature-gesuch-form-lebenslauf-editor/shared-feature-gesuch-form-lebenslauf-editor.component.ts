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
  AbschlussSlim,
  LebenslaufItemUpdate,
  Taetigkeitsart,
  WohnsitzKanton,
} from '@dv/shared/model/gesuch';
import { SharedModelLebenslauf } from '@dv/shared/model/lebenslauf';
import { getTranslatableProp, isDefined } from '@dv/shared/model/type-util';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiFormReadonlyDirective,
} from '@dv/shared/ui/form';
import { SharedUiInfoDialogDirective } from '@dv/shared/ui/info-dialog';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
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
  ],
  templateUrl: './shared-feature-gesuch-form-lebenslauf-editor.component.html',
  styleUrls: ['./shared-feature-gesuch-form-lebenslauf-editor.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormLebenslaufEditorComponent {
  private elementRef = inject(ElementRef);
  private formBuilder = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);
  private ausbildungsstatteStore = inject(AusbildungsstaetteStore);
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
    abschluss: [
      <AbschlussSlim | string | undefined>undefined,
      [Validators.required],
    ],
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

  abschlussSig = toSignal(this.form.controls.abschluss.valueChanges);
  selectedAbschlussSig = computed(() => {
    const abschluss = this.abschlussSig();
    if (!isAbschluss(abschluss)) {
      return null;
    }
    return abschluss;
  });
  startChangedSig = toSignal(this.form.controls.von.valueChanges);
  endChangedSig = toSignal(this.form.controls.bis.valueChanges);
  kantonValues = this.prepareKantonValues();
  abschlussOptionsSig = computed(() => {
    const rawAbschluesse = this.ausbildungsstatteStore.abschluesseViewSig();
    const language = this.languageSig();
    let rawAbschluss = this.abschlussSig();
    const abschluesse =
      rawAbschluesse.map((abschluss) => ({
        ...abschluss,
        translatedName: getTranslatableProp(abschluss, 'bezeichnung', language),
      })) ?? [];

    if (!!rawAbschluss && typeof rawAbschluss !== 'string') {
      rawAbschluss =
        getTranslatableProp(rawAbschluss, 'bezeichnung', language) ?? undefined;
    }

    if (!rawAbschluss) {
      return abschluesse;
    }

    return abschluesse.filter((abschluss) => {
      const bezeichnung = getTranslatableProp(
        abschluss,
        'bezeichnung',
        language,
      );
      const ausbildungskategorie = this.translateService.instant(
        `shared.ausbildungskategorie.${abschluss.ausbildungskategorie}`,
      );
      return `${bezeichnung} - ${ausbildungskategorie}`
        .toLocaleLowerCase()
        .includes(rawAbschluss.toLocaleLowerCase());
    });
  });

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
        this.form.controls.abschluss.clearValidators();
        this.form.controls.abschluss.setValidators([
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
        abschluss: abschlussId
          ? this.ausbildungsstatteStore
              .abschluesseViewSig()
              .find((a) => a.id === abschlussId)
          : undefined,
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
        this.formUtils.setRequired(this.form.controls.abschluss, true);
      }

      if (item.type === 'TAETIGKEIT') {
        this.form.controls.abschluss.clearValidators();
        this.form.controls.abschluss.updateValueAndValidity();
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
      const { abschluss, ...formValues } = convertTempFormToRealValues(
        this.form,
        this.itemSig().type === 'AUSBILDUNG'
          ? ['abschluss', 'wohnsitz', 'ausbildungAbgeschlossen']
          : ['taetigkeitsart', 'taetigkeitsBeschreibung'],
      );
      this.saveTriggered.emit({
        id: this.itemSig().id,
        ...formValues,
        abschlussId: isAbschluss(abschluss) ? abschluss.id : undefined,
      });
      this.form.markAsPristine();
    }
  }

  // clear the control if the value is not a valid Abschluss
  onBlurAbschluss() {
    const value = this.form.controls.abschluss.value;
    if (!isAbschluss(value) && !this.abschluesseAutocompleteSig()?.isOpen) {
      // find the abschluss from the current value
      if (typeof value === 'string' && value.length > 0) {
        const abschluss = this.ausbildungsstatteStore
          .abschluesseViewSig()
          .find((abschluss) => {
            const translatedName = getTranslatableProp(
              abschluss,
              'bezeichnung',
              this.languageSig(),
            );
            return (
              translatedName?.toLocaleLowerCase() === value.toLocaleLowerCase()
            );
          });
        if (abschluss) {
          this.form.controls.abschluss.setValue(abschluss);
        } else {
          this.form.controls.abschluss.setValue(undefined);
        }
      }
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

  displayAbschluss = (abschluss: AbschlussSlim | string | undefined) => {
    if (isAbschluss(abschluss)) {
      return abschlussFullName(
        abschluss,
        this.translateService,
        this.languageSig(),
      );
    }
    return abschluss ?? '';
  };

  onDateBlur(ctrl: FormControl) {
    return onMonthYearInputBlur(
      ctrl,
      this.minStartDateSig() ?? new Date(),
      this.languageSig(),
    );
  }

  protected readonly taetigkeitsartValues = Object.values(Taetigkeitsart);
}

const isAbschluss = (
  value: AbschlussSlim | string | undefined,
): value is AbschlussSlim => isDefined(value) && typeof value !== 'string';

const abschlussFullName = (
  abschluss: AbschlussSlim,
  translate: TranslateService,
  language: string,
) => {
  return `${getTranslatableProp(abschluss, 'bezeichnung', language)} - ${translate.instant(
    `shared.ausbildungskategorie.${abschluss.ausbildungskategorie}`,
  )}`;
};
