import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { format, isAfter, isBefore, isValid } from 'date-fns';

import { Language } from '@dv/shared/model/language';

import {
  DateFormatVariant,
  getFormatDef,
  parseInputDateString,
} from '../index';

export type DateValidatorError =
  | 'minDate'
  | 'maxDate'
  | (string & Record<never, never>);
export type DateValidatorOptions = {
  locale: Language;
  dateFormatVariant: DateFormatVariant;
  referenceDate?: Date;
};

export function minDateValidatorForLocale(
  locale: Language,
  minDate: Date,
  dateFormatVariant: DateFormatVariant,
  options?: { referenceDate?: Date; errorType: string },
) {
  return minDateValidator(minDate, { locale, dateFormatVariant, ...options });
}

export function maxDateValidatorForLocale(
  locale: Language,
  maxDate: Date,
  dateFormatVariant: DateFormatVariant,
  options?: { referenceDate?: Date; errorType: string },
) {
  return maxDateValidator(maxDate, { locale, dateFormatVariant, ...options });
}

export function minDateValidator(
  minDate: Date,
  options: DateValidatorOptions & {
    errorType?: string;
  },
): ValidatorFn {
  return dateValidator(minDate, isBefore, {
    errorType: options.errorType ?? 'minDate',
    ...options,
  });
}

export function maxDateValidator(
  maxDate: Date,
  options: DateValidatorOptions & {
    errorType?: string;
  },
): ValidatorFn {
  return dateValidator(maxDate, isAfter, {
    errorType: options.errorType ?? 'maxDate',
    ...options,
  });
}

function dateValidator(
  comparingDate: Date,
  compareFnc: (date1: Date, date2: Date) => boolean,
  options: DateValidatorOptions & {
    errorType: DateValidatorError;
  },
): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const date = control.value;
    if (!date) {
      return null;
    }
    const { locale, referenceDate, dateFormatVariant, errorType } = options;
    const parsedDate = parseInputDateString(
      control.value,
      getFormatDef(locale, dateFormatVariant).acceptedInputs,
      referenceDate ?? new Date(),
    );
    if (!parsedDate || !isValid(parsedDate)) {
      return null;
    }
    if (compareFnc(parsedDate, comparingDate)) {
      return {
        [errorType]: {
          value: control.value,
          [errorType]: format(
            comparingDate,
            getFormatDef(locale, dateFormatVariant).niceInput,
          ),
        },
      };
    }
    return null;
  };
}
