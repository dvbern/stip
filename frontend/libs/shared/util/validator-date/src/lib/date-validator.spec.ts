import { FormControl } from '@angular/forms';

import {
  maxDateValidatorForLocale,
  minDateValidatorForLocale,
} from './date-validator';
import { parseDateForVariant } from './parseable-date-validator';

describe('validator-date', () => {
  const minError = {
    minDate: {
      minDate: '02.2024',
    },
  };
  const maxError = {
    maxDate: {
      maxDate: '02.2024',
    },
  };
  const customError = {
    custom: {
      custom: '02.2024',
    },
  };

  it.each([
    ['02.2024', 'min', '02.2024', null, false],
    ['03.2024', 'min', '02.2024', null, false],
    ['01.2024', 'min', '02.2024', minError, false],
    ['02.2024', 'max', '02.2024', null, false],
    ['01.2024', 'max', '02.2024', null, false],
    ['03.2024', 'max', '02.2024', maxError, false],
    ['01.2024', 'min', '02.2024', customError, 'custom'],
    ['03.2024', 'max', '02.2024', customError, 'custom'],
  ] as const)(
    'should, if date %s is not %s %s, return %s ',
    (controlDate, operator, date, expectedValue, custom) => {
      const control = new FormControl(controlDate);
      const validator =
        operator === 'min'
          ? minDateValidatorForLocale
          : maxDateValidatorForLocale;
      const comparingDate = parseDateForVariant(date, new Date(), 'monthYear');
      if (comparingDate === null) {
        throw new Error('comparingDate is null');
      }
      const result = validator(
        'de',
        comparingDate,
        'monthYear',
        custom
          ? {
              errorType: custom,
            }
          : undefined,
      )(control);
      if (expectedValue === null) {
        expect(result).toBeNull();
        return;
      }
      expect(result).toMatchObject(expectedValue);
    },
  );
});
