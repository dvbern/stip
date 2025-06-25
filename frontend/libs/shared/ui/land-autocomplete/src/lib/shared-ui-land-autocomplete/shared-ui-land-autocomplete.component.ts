import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  Injector,
  OnInit,
  computed,
  effect,
  inject,
  input,
  runInInjectionContext,
  signal,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import {
  AbstractControl,
  ControlValueAccessor,
  FormBuilder,
  FormsModule,
  NgControl,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { TranslatePipe } from '@ngx-translate/core';

import { Land } from '@dv/shared/model/gesuch';
import { Language } from '@dv/shared/model/language';
import { BFSCODE_SCHWEIZ } from '@dv/shared/model/ui-constants';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiZuvorHintDirective,
} from '@dv/shared/ui/form';
import { LandLookupService } from '@dv/shared/util-data-access/land-lookup';

type LanguageDisplayFields = Exclude<keyof Land, 'eintragGueltig' | 'isEuEfta'>;

@Component({
  selector: 'dv-shared-ui-land-autocomplete',
  standalone: true,
  imports: [
    CommonModule,
    TranslatePipe,
    FormsModule,
    ReactiveFormsModule,
    MatAutocompleteModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiZuvorHintDirective,
    MatButtonModule,
  ],
  templateUrl: './shared-ui-land-autocomplete.component.html',
  styleUrl: './shared-ui-land-autocomplete.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiLandAutocompleteComponent
  implements ControlValueAccessor, OnInit
{
  private landLookupService = inject(LandLookupService);
  private laenderSig = this.landLookupService.getCachedLandLookup();
  private fb = inject(FormBuilder);
  private ngControl = inject(NgControl, { optional: true });
  private injector = inject(Injector);

  // Inputs
  zuvorHintValueSig = input<string | undefined>(undefined);
  languageSig = input.required<Language>();
  testIdSig = input<string>('form-address-land');
  labelKeySig = input<string>('shared.form.shared.address.country.label');

  // Component state
  private isOpen = false;
  private disabled = false;
  private touched = false;
  private latestValue = signal<string | undefined>(undefined);
  landId: string | undefined;
  // Internal autocomplete control - works with Land objects and searchstrings
  autocompleteControl = this.fb.nonNullable.control<Land | string | undefined>(
    undefined,
  );

  // ControlValueAccessor methods - only deals with string IDs
  private onChange: (value: string | undefined) => void = () => {
    // Default empty implementation
  };
  private onTouched: () => void = () => {
    // Default empty implementation
  };

  constructor() {
    // this is a workaround to get access to the NgControl instance and not run into circular dependency issues
    // https://stackoverflow.com/questions/45755958/how-to-get-formcontrol-instance-from-controlvalueaccessor
    if (this.ngControl) {
      this.ngControl.valueAccessor = this;
    }

    effect(() => {
      const landId = this.latestValue();
      const laender = this.laenderSig();
      if (landId) {
        this.landId = landId;
        const land = laender?.find((l) => l.id === landId);
        if (land) {
          this.setAutocomplete(land);
          this.ngControl?.control?.updateValueAndValidity();
        }
      } else {
        this.landId = undefined;
        this.autocompleteControl.setValue(undefined, {
          emitEvent: false,
        });
        this.ngControl?.control?.updateValueAndValidity();
      }
    });
  }

  ngOnInit() {
    const control = this.ngControl?.control;

    if (control) {
      const hasRequiredValidator = control.hasValidator(Validators.required);
      if (hasRequiredValidator) {
        this.autocompleteControl.setValidators(Validators.required);
      }
      control.addValidators(validateLand(this.autocompleteControl));
      control.updateValueAndValidity();

      runInInjectionContext(this.injector, () => {
        effect(() => {
          // access the private touchedReactive signal as workaround
          const touched = control['touchedReactive']();
          if (touched) {
            this.markAsTouched();
          }
        });
      });
    }
  }

  languageDisplayFieldSig = computed(() => {
    const language = this.languageSig();

    switch (language) {
      case 'de':
        return 'deKurzform';
      case 'fr':
        return 'frKurzform';
      default:
        console.warn(
          `Unsupported language: ${language}. Defaulting to 'deKurzform'.`,
        );
        return 'deKurzform';
    }
  });

  languageDisplayFieldFNSig = computed(() => {
    const languageDisplayField = this.languageDisplayFieldSig();

    return (land: Land | undefined): string => {
      return land ? land[languageDisplayField] : '';
    };
  });

  autocompleteControlValueChangesSig = toSignal(
    this.autocompleteControl.valueChanges,
    { initialValue: undefined },
  );

  laenderValuesSig = computed(() => {
    const landInputVal = this.autocompleteControlValueChangesSig();
    const laender = this.laenderSig();
    const languageDisplayField = this.languageDisplayFieldSig();

    if (!laender) {
      return [];
    }

    if (typeof landInputVal === 'string') {
      return this.handleStringInput(
        landInputVal,
        laender,
        languageDisplayField,
      );
    }

    if (isLand(landInputVal)) {
      return this.handleLandSelection(
        landInputVal,
        laender,
        languageDisplayField,
      );
    }

    if (!landInputVal && this.landId) {
      return this.handleInitialLandLoad(laender, languageDisplayField);
    }

    return this.getDefaultLandList(laender, languageDisplayField);
  });

  zuvorHintLandNameSig = computed(() => {
    const zuvorHintValue = this.zuvorHintValueSig();
    const languageDisplayField = this.languageDisplayFieldSig();

    if (zuvorHintValue) {
      return (
        this.laenderSig()?.find((land) => land.id === zuvorHintValue)?.[
          languageDisplayField
        ] ?? undefined
      );
    }
    return undefined;
  });

  // ControlValueAccessor implementation
  writeValue(landId: string | undefined): void {
    this.latestValue.set(landId);
  }

  registerOnChange(fn: (value: string | undefined) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
    if (isDisabled) {
      this.autocompleteControl.disable({ emitEvent: false });
    } else {
      this.autocompleteControl.enable({ emitEvent: false });
    }
  }

  // needed so moving focus to the overlay does not count as blur and clear the control
  toggleOpen() {
    this.isOpen = !this.isOpen;
  }

  resetControl() {
    this.autocompleteControl.setValue(undefined, {
      emitEvent: false,
    });
    this.landId = undefined;
    this.onChange(undefined);
    this.markAsTouched();
  }

  markAsTouched() {
    if (!this.touched) {
      this.onTouched();
      this.touched = true;
      this.autocompleteControl.markAsTouched();
    }
  }

  // clear the control if the value is not a valid Land
  onBlur() {
    if (!this.touched) {
      this.onTouched();
    }
    const value = this.autocompleteControl.value;
    if (!isLand(value) && !this.isOpen) {
      // find the landId from the current value
      if (typeof value === 'string' && value.length > 0) {
        const land = this.laenderSig()?.find(
          (l) =>
            l[this.languageDisplayFieldSig()]?.toLowerCase() ===
            value.toLowerCase(),
        );
        if (land) {
          this.landId = land.id;
          this.onChange(land.id);
          this.setAutocomplete(land);
        } else {
          this.landId = undefined;
          this.onChange(undefined);
          this.autocompleteControl.setValue(undefined);
        }
      }
    }
  }

  private setAutocomplete(land: Land) {
    this.autocompleteControl.setValue(land, {
      emitEvent: false,
    });
    // set an error if the land no longer valid
    if (!land.eintragGueltig) {
      this.autocompleteControl.setErrors({
        invalidLand: true,
      });
      this.markAsTouched();
    }
  }

  private handleStringInput(
    inputValue: string,
    laender: Land[],
    languageDisplayField: LanguageDisplayFields,
  ): Array<Land & { displayValue: string }> {
    const filteredLaender = laender.filter(
      (land) =>
        land.eintragGueltig &&
        land[languageDisplayField]
          ?.toLowerCase()
          .startsWith(inputValue.toLowerCase()),
    );

    return this.sortAndMapLaender(
      filteredLaender,
      languageDisplayField,
      inputValue.length === 0,
    );
  }

  private handleLandSelection(
    selectedLand: Land,
    laender: Land[],
    languageDisplayField: LanguageDisplayFields,
  ) {
    this.onChange(selectedLand.id);
    return this.getDefaultLandList(laender, languageDisplayField);
  }

  private handleInitialLandLoad(
    laender: Land[],
    languageDisplayField: LanguageDisplayFields,
  ) {
    const selectedLand = laender.find((l) => l.id === this.landId);
    if (selectedLand) {
      this.setAutocomplete(selectedLand);
    }
    return this.getDefaultLandList(laender, languageDisplayField);
  }

  private getDefaultLandList(
    laender: Land[],
    languageDisplayField: LanguageDisplayFields,
  ): Array<Land & { displayValue: string }> {
    const validLaender = laender.filter((land) => land.eintragGueltig);
    return this.sortAndMapLaender(validLaender, languageDisplayField, true);
  }

  private sortAndMapLaender(
    laender: Land[],
    languageDisplayField: Exclude<keyof Land, 'eintragGueltig' | 'isEuEfta'>,
    shouldPrioritizeSwitzerland: boolean,
  ): Array<Land & { displayValue: string }> {
    const sortedLaender = [...laender]; // create a shallow copy before sorting
    sortedLaender.sort((a, b) => {
      if (shouldPrioritizeSwitzerland) {
        if (a.laendercodeBfs === BFSCODE_SCHWEIZ) return -1;
        if (b.laendercodeBfs === BFSCODE_SCHWEIZ) return 1;
      }
      return (a[languageDisplayField] ?? '').localeCompare(
        b[languageDisplayField] ?? '',
      );
    });

    return sortedLaender.map((land) => ({
      ...land,
      displayValue: land[languageDisplayField] ?? '',
    }));
  }
}

const isLand = (value: Land | string | undefined): value is Land =>
  typeof value === 'object' && value !== null && 'id' in value;

const validateLand =
  (autocompleteControl: AbstractControl) => (control: AbstractControl) => {
    if (control.invalid || autocompleteControl.invalid) {
      const errors = { ...control.errors, ...autocompleteControl.errors };
      const land = autocompleteControl.value;
      if (isLand(land) && !land.eintragGueltig) {
        return {
          ...errors,
          invalidLand: true,
        };
      }
      return errors;
    }
    return null;
  };
