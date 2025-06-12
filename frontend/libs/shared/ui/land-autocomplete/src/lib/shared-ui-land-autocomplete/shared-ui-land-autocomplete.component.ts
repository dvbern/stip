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
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import {
  ControlValueAccessor,
  FormBuilder,
  FormsModule,
  NgControl,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { TranslatePipe } from '@ngx-translate/core';

import { Land } from '@dv/shared/model/gesuch';
import { Language } from '@dv/shared/model/language';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiZuvorHintDirective,
} from '@dv/shared/ui/form';
import { LandLookupService } from '@dv/shared/util-data-access/land-lookup';

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

  // Component state
  private isOpen = false;
  private disabled = false;
  private touched = false;
  private landId: string | undefined;
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
  }

  ngOnInit() {
    const control = this.ngControl?.control;

    if (control) {
      const hasRequiredValidator = control.hasValidator(Validators.required);
      if (hasRequiredValidator) {
        this.autocompleteControl.setValidators(Validators.required);
      }

      runInInjectionContext(this.injector, () => {
        effect(() => {
          // access the private touchedReactive signal as workaround
          // eslint-disable-next-line @typescript-eslint/no-explicit-any
          const touched = (control as any).touchedReactive();
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
    const laender = this.laenderSig()?.filter((l) => l.eintragGueltig);
    const languageDisplayField = this.languageDisplayFieldSig();

    if (laender) {
      if (typeof landInputVal === 'string') {
        return laender
          .filter((land) =>
            land[languageDisplayField]
              .toLowerCase()
              .includes(landInputVal.toLowerCase()),
          )
          .map((land) => ({
            ...land,
            displayValue: land[languageDisplayField],
          }));
      } else if (isLand(landInputVal)) {
        // When a Land object is selected
        this.onChange(landInputVal.id);
        return [];
      } else if (!landInputVal && this.landId) {
        // Necessary to hadle reloads and leander come in late
        const selectedLand = laender.find((l) => l.id === this.landId);
        if (selectedLand) {
          this.autocompleteControl.setValue(selectedLand, {
            emitEvent: false,
          });
          return [];
        }
      }
    }

    return [];
  });

  zuvorHintLandNameSig = computed(() => {
    const zuvorHintValue = this.zuvorHintValueSig();
    const languageDisplayField = this.languageDisplayFieldSig();

    if (zuvorHintValue) {
      return this.laenderSig()?.find((land) => land.id === zuvorHintValue)?.[
        languageDisplayField
      ];
    }
    return undefined;
  });

  // ControlValueAccessor implementation
  writeValue(landId: string | undefined): void {
    if (landId) {
      this.landId = landId;
      const land = this.laenderSig()?.find((l) => l.id === landId);
      if (land) {
        this.autocompleteControl.setValue(land, {
          emitEvent: false,
        });
      }
    } else {
      this.landId = undefined;
      this.autocompleteControl.setValue(undefined, {
        emitEvent: false,
      });
    }
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
      this.landId = undefined;
      this.onChange(undefined);
      this.autocompleteControl.setValue(undefined);
    }
  }
}

const isLand = (value: Land | string | undefined): value is Land =>
  typeof value === 'object' && value !== null && 'id' in value;
