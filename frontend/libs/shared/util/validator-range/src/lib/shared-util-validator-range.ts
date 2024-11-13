import {
  AbstractControl,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';

import { isDefined } from '@dv/shared/model/type-util';

export function sharedUtilValidatorRange(
  min: number,
  max: number,
): ValidatorFn {
  return (control: AbstractControl<number | null>): ValidationErrors | null => {
    if (!isDefined(control?.value)) {
      return null;
    }
    if (isNaN(control.value)) {
      return { notANumber: true };
    }

    const minErrors = Validators.min(min)(control);
    const maxErrors = Validators.max(max)(control);

    if (minErrors || maxErrors) {
      return {
        range: {
          ...minErrors,
          ...maxErrors,
        },
      };
    }

    return null;
  };
}
