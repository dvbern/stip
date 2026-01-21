import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  Injector,
  OnInit,
  computed,
  contentChild,
  effect,
  inject,
  input,
  runInInjectionContext,
  signal,
  untracked,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import {
  AbstractControl,
  ControlValueAccessor,
  FormsModule,
  NgControl,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { TranslocoPipe, TranslocoService } from '@jsverse/transloco';
import {
  MatSelectSearchClearDirective,
  MatSelectSearchComponent,
} from 'ngx-mat-select-search';

import { Language } from '@dv/shared/model/language';
import { LookupType } from '@dv/shared/model/select-search';
import { capitalized, compareById } from '@dv/shared/model/type-util';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiZuvorHintDirective,
} from '@dv/shared/ui/form';
import { TranslatedPropertyPipe } from '@dv/shared/ui/translated-property-pipe';
import { sortListByText } from '@dv/shared/util/table';

import { SharedUiSearchOptionLabelDirective } from './select-search-option-label.directive';

/**
 * A reusable select-search component for Angular applications.
 *
 * @example```ts
 *     valuesSig = computed(() =>
 *           this.someStore.someViewSig()?.map((something) => ({
 *             ...something,
 *             testId: something.nameDe,
 *             invalid: something.deleted,
 *             disabled: something.archived,
 *             displayValueDe: something.nameDe,
 *             displayValueFr: something.nameFr,
 *           })) ?? [],
 *     );
 *     isValidValueSig(value: Something | undefined): boolean {
 *           return !!value && !!value.id;
 *     }
 * ```
 *
 * @example```html
 *     <dv-shared-ui-select-search
 *           formControlName="somethingId"
 *           [valuesSig]="valuesSig()"
 *           [labelKeySig]="'shared.form.shared.something.label'"
 *           [languageSig]="languageSig()"
 *           [testIdSig]="'form-something-select-search'"
 *           [invalidValueLabelKeySig]="'shared.form.error.something.invalidValue'"
 *           [zuvorValueSig]="changes?.somethingId"
 *     ></dv-shared-ui-select-search>
 * ```
 */
@Component({
  selector: 'dv-shared-ui-select-search',
  standalone: true,
  imports: [
    CommonModule,
    TranslocoPipe,
    FormsModule,
    ReactiveFormsModule,
    MatSelectModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatSelectSearchComponent,
    MatSelectSearchClearDirective,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiZuvorHintDirective,
    TranslatedPropertyPipe,
  ],
  templateUrl: './shared-ui-select-search.component.html',
  styleUrl: './shared-ui-select-search.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiSelectSearchComponent<T extends LookupType>
  implements ControlValueAccessor, OnInit
{
  private formBuilder = inject(NonNullableFormBuilder);
  private ngControl = inject(NgControl, { optional: true });
  private injector = inject(Injector);
  translate = inject(TranslocoService);

  /**
   * The list of values to display in the autocomplete.
   * This should be an array of objects matching {@link T}.
   */
  valuesSig = input.required<T[]>();
  /**
   * The label key for the autocomplete input.
   */
  labelKeySig = input.required<string>();
  /**
   * The language to use for displaying values.
   */
  languageSig = input.required<Language>();
  /**
   * The error key to use when the value is invalid.
   */
  invalidValueLabelKeySig = input<string>();
  /**
   * Enables sorting of the values by their display value.
   */
  sortByValueSig = input<boolean>(true);
  /**
   * The width of the panel in pixels or as a percentage string.
   * Default is '100%' to fill the width of the parent container.
   *
   * @defaultValue 'fit-content
   */
  panelWidthSig = input<string | number | null>('fit-content');
  /**
   * The ID of the previously selected value, used to show a hint.
   * If not set, no hint will be shown.
   *
   * @defaultValue undefined
   */
  zuvorValueSig = input<string | undefined>(undefined);
  /**
   * Test ID for the input element of the autocomplete.
   *
   * @defaultValue 'input-autocomplete'
   */
  testIdSig = input<string>('input-autocomplete');
  /**
   * Test ID for the clear button of the autocomplete.
   *
   * @defaultValue 'button-autocomplete-clear'
   */
  clearTestIdSig = input<string>('button-autocomplete-clear');
  /**
   * Test ID for the hint element of the autocomplete.
   *
   * @defaultValue 'hint-autocomplete-zuvor'
   */
  zuvorHintTestIdSig = input<string>('hint-autocomplete-zuvor');

  // Component state
  private touched = false;
  private latestValueSig = signal<string | undefined>(undefined, {
    equal: (a, b) => a === b,
  });

  optionTemplateSig = contentChild(SharedUiSearchOptionLabelDirective<T>);
  valueId: string | undefined;
  /**
   * Internal autocomplete control - works with Land objects and searchstrings
   */
  form = this.formBuilder.group({
    search: [<string | undefined>undefined],
    select: [<LookupType | undefined>undefined],
  });

  displayValueWithSig = computed(() => {
    const language = this.languageSig();
    return (value: T) => {
      return value?.[`displayValue${capitalized(language)}`];
    };
  });

  autocompleteSearchValueChangesSig = toSignal(
    this.form.controls.search.valueChanges,
    { initialValue: undefined },
  );

  autocompleteSelectValueChangesSig = toSignal(
    this.form.controls.select.valueChanges,
    { initialValue: undefined },
  );

  filteredValuesSig = computed(() => {
    const valueInput = this.autocompleteSearchValueChangesSig();
    const shouldSort = this.sortByValueSig();
    const displayValue = this.displayValueWithSig();

    let values = this.valuesSig();

    if (!values) {
      return [];
    }

    if (shouldSort) {
      values = sortListByText(values, displayValue);
    }

    if (typeof valueInput === 'string' && valueInput.length > 0) {
      values = values.filter((value) =>
        this.displayValueWithSig()(value)
          ?.toLowerCase()
          .includes(valueInput.toLowerCase()),
      );
    }

    if (!valueInput) {
      const firstItemValues = values.filter((v) => v.alwaysOnTop);
      if (firstItemValues) {
        values = [...firstItemValues, ...values.filter((v) => !v.alwaysOnTop)];
      }
    }

    return values;
  });

  zuvorHintValueSig = computed(() => {
    const zuvorHintValue = this.zuvorValueSig();
    const values = this.valuesSig();
    if (!zuvorHintValue || !values) {
      return undefined;
    }

    const previousValue = values.find((land) => land.id === zuvorHintValue);
    if (!previousValue) {
      return undefined;
    }
    const displayValue = this.displayValueWithSig();

    return displayValue(previousValue);
  });

  compareById = compareById;

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

    // Initialize the values
    effect(() => {
      const valueId = this.latestValueSig();
      const values = this.valuesSig();

      if (!values) {
        return;
      }
      if (valueId) {
        this.valueId = valueId;
        const value = values?.find((l) => l.id === valueId);
        if (value) {
          this.form.controls.select.patchValue(value, { emitEvent: false });

          // Mark the control as touched if the entry is
          if (value.invalid) {
            this.markAsTouched();
          }
        } else {
          this.form.controls.search.setValue(undefined, {
            emitEvent: true,
          });
          this.form.controls.select.setValue(undefined, {
            emitEvent: true,
          });
          this.markAsTouched();
          this.onChange(undefined);
        }
        this.ngControl?.control?.updateValueAndValidity();
      } else {
        this.valueId = undefined;
        this.form.controls.search.setValue(undefined, {
          emitEvent: true,
        });
        this.form.controls.select.setValue(undefined, {
          emitEvent: true,
        });
      }
    });

    // Update the value when the inner form control changes
    effect(() => {
      const values = this.valuesSig();
      const value = this.autocompleteSelectValueChangesSig();

      if (!values) {
        return;
      }
      // Only emit the value if it is different from the previous value
      const previousValue = untracked(this.latestValueSig);
      if (value && value.id !== previousValue) {
        this.onChange(value.id);
        this.latestValueSig.set(value.id);
      }
    });
  }

  ngOnInit() {
    const control = this.ngControl?.control;

    if (control) {
      const hasRequiredValidator = control.hasValidator(Validators.required);
      if (hasRequiredValidator) {
        this.form.controls.select.setValidators(Validators.required);
      }

      runInInjectionContext(this.injector, () => {
        effect(() => {
          // Access the private touchedReactive signal as workaround
          const touched = control['touchedReactive']();
          if (touched) {
            this.markAsTouched();
          }
        });

        let currentValidator: ValidatorFn;
        effect(() => {
          if (currentValidator) {
            [control, this.form.controls.select].forEach((c) => {
              c.removeValidators(currentValidator);
            });
          }
          currentValidator = createValidator(this.form.controls.select);
          [control, this.form.controls.select].forEach((c) => {
            c.addValidators(currentValidator);
            c.updateValueAndValidity();
          });
        });
      });
    }
  }

  resetControl() {
    this.form.controls.select.patchValue(undefined);
    this.form.controls.search.patchValue(undefined);
    this.latestValueSig.set(undefined);
    this.onChange(undefined);
    this.markAsTouched();
  }

  markAsTouched() {
    if (!this.touched) {
      this.onTouched();
      this.touched = true;
      this.form.controls.select.markAsTouched();
      this.form.controls.search.markAsTouched();
    }
  }

  // ControlValueAccessor implementation
  writeValue(valueId: string | undefined): void {
    this.latestValueSig.set(valueId);
  }

  registerOnChange(fn: (value: string | undefined) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    if (isDisabled) {
      this.form.controls.select.disable({ emitEvent: false });
    } else {
      this.form.controls.select.enable({ emitEvent: false });
    }
  }
}

const createValidator =
  (autocompleteControl: AbstractControl<LookupType | undefined>) => () => {
    const value = autocompleteControl.value;
    if (value && value.invalid) {
      return {
        invalidValue: true,
      };
    }
    return null;
  };
