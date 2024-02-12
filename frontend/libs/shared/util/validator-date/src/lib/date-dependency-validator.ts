import {
  AbstractControl,
  FormControl,
  ValidationErrors,
  ValidatorFn,
} from '@angular/forms';
import {
  areIntervalsOverlapping,
  compareAsc,
  isAfter,
  isEqual,
} from 'date-fns';

import { Language } from '@dv/shared/model/language';

import { DateFormatVariant } from './date-format-util';
import { parseDateForVariant } from '../index';

export function createDateDependencyValidator(
  direction: 'before' | 'after',
  otherControl: FormControl<string | null>,
  allowEqual: boolean,
  referenceDate: Date,
  locale: Language,
  dateFormatVariant: DateFormatVariant,
): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const otherValue = otherControl.value;
    const myValue = control.value as string;
    return direction === 'before'
      ? validateStartBeforeEnd(
          myValue,
          otherValue,
          allowEqual,
          referenceDate,
          locale,
          dateFormatVariant,
        )
      : validateStartBeforeEnd(
          otherValue,
          myValue,
          allowEqual,
          referenceDate,
          locale,
          dateFormatVariant,
        );
  };
}

export function createOverlappingValidator(
  otherDateControl: FormControl<string | null>,
  intervalValues: Readonly<[string, string]>[],
  referenceDate: Date,
  dateFormatVariant: DateFormatVariant,
): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const date1 = parseDateForVariant(
      otherDateControl.value,
      new Date(),
      dateFormatVariant,
    );
    const date2 = parseDateForVariant(
      control.value,
      new Date(),
      dateFormatVariant,
    );

    if (!date1 || !date2) {
      return null;
    }

    const [minDate, maxDate] = [date1, date2].sort(compareAsc);
    const intervals = intervalValues.map(([start, end]) => [
      parseDateForVariant(start, referenceDate, dateFormatVariant),
      parseDateForVariant(end, referenceDate, dateFormatVariant),
    ]);

    const hasOverlaps = intervals.some(([start, end]) => {
      if (!start || !end) {
        return false;
      }
      const isOverlapping = areIntervalsOverlapping(
        { start: minDate, end: maxDate },
        { start, end },
      );
      return (
        isOverlapping ||
        isOnIntervalLimits(minDate, start, end) ||
        isOnIntervalLimits(maxDate, start, end)
      );
    });
    return hasOverlaps ? { overlapsOthers: true } : null;
  };
}

export function validateStartBeforeEnd(
  startValue: string | null,
  endValue: string | null,
  allowEqual: boolean,
  referenceDate: Date,
  locale: Language,
  dateFormatVariant: DateFormatVariant,
): ValidationErrors | null {
  if (startValue && endValue) {
    const startDate = parseDateForVariant(
      startValue,
      new Date(),
      dateFormatVariant,
    );
    const endDate = parseDateForVariant(
      endValue,
      new Date(),
      dateFormatVariant,
    );

    if (startDate && endDate) {
      if (allowEqual && isEqual(endDate, startDate)) {
        return null;
      }
      return isAfter(endDate, startDate) ? null : { endDateAfterStart: true };
    }
  }
  return null;
}

const isOnIntervalLimits = (date: Date, start: Date, end: Date) => {
  return date !== null && (isEqual(date, start) || isEqual(date, end));
};
