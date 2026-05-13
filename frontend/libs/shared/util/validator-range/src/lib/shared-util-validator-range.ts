import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

import { isDefined } from '@dv/shared/model/type-util';

export function sharedUtilValidatorRange(
  min: number,
  max?: number,
): ValidatorFn {
  return (control: AbstractControl<string | null>): ValidationErrors | null => {
    if (!isDefined(control?.value)) {
      return null;
    }
    if (isNaN(+control.value)) {
      return { notANumber: true };
    }

    const value = +control.value;
    const errors = {
      ...(value < min ? { min: true } : {}),
      ...(isDefined(max) && value > max ? { max: true } : {}),
    };

    if (Object.keys(errors).length) {
      if (isDefined(errors.max)) {
        return {
          range: errors,
        };
      }
      return errors;
    }

    return null;
  };
}
