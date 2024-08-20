import { format, intervalToDuration, isValid, parse, parseISO } from 'date-fns';

import { Language } from '@dv/shared/model/language';
import { isDefined } from '@dv/shared/util-fn/type-guards';

import { DateFormatVariant, getFormatDef, parseDateForVariant } from '../index';

type NullableString = string | null | undefined;

export function parseBackendLocalDateAndPrint(
  value: NullableString,
  locale: Language,
) {
  return formatBackendLocalDate(value, locale);
}

export function printDate(
  date: Date,
  locale: Language,
  dateFormatVariant: DateFormatVariant,
) {
  return format(date, getFormatDef(locale, dateFormatVariant).niceInput);
}

export function toBackendLocalDate(date: Date) {
  return format(date, 'yyyy-MM-dd');
}

export function fromBackendLocalDate(value: NullableString) {
  if (!isDefined(value)) {
    return undefined;
  }
  return parseISO(value);
}

export function formatBackendLocalDate(
  value: NullableString,
  locale: Language,
) {
  if (value === null || value === undefined) {
    return undefined;
  }
  const date = parseISO(value);
  if (date === undefined) {
    return undefined;
  }
  return printDate(date, locale, 'date');
}

export function parseStringAndPrintForBackendLocalDate(
  value: NullableString,
  locale: Language,
  referenceDate: Date,
) {
  return asBackendLocalDate(
    value,
    getFormatDef(locale, 'date').acceptedInputs,
    referenceDate,
  );
}

export function parseInputDateStringVariants(
  value: NullableString,
  srcFormats: string[],
) {
  if (value === null || value === undefined) {
    return undefined;
  }
  const parsedDates = srcFormats.map((srcFormat) =>
    parse(value, srcFormat, new Date()),
  );
  return parsedDates;
}

export function parseInputDateString(
  value: NullableString,
  srcFormats: string[],
  referenceDate: Date,
) {
  if (value === null || value === undefined) {
    return undefined;
  }
  // the reference date is used with 'yy' to guess the closest date
  const parsedDates = srcFormats.map((srcFormat) =>
    parse(value, srcFormat, referenceDate),
  );
  const validDates = parsedDates.filter((each) => isValid(each));
  if (validDates.length) {
    return validDates[0];
  }
  return undefined;
}

export function asBackendLocalDate(
  value: NullableString,
  srcFormats: string[],
  referenceDate: Date,
) {
  const date = parseInputDateString(value, srcFormats, referenceDate);
  if (date === undefined || !isValid(date)) {
    return undefined;
  }
  return format(date, 'yyyy-MM-dd');
}

export function dateFromMonthYearString(monthYearString?: string) {
  return parseDateForVariant(monthYearString, new Date(), 'monthYear');
}

export function printDateAsMonthYear(date: Date) {
  return printDate(date, 'de', 'monthYear');
}

/**
 * Returns the last day of the year before the given year.
 */
export function getLastDayOfYear(jahr: number): Date {
  return new Date(jahr, 11, 31);
}

export function getDateDifference(
  date: Date | string,
  refDate: Date,
  variant: DateFormatVariant = 'date',
) {
  const start =
    typeof date === 'string'
      ? parseDateForVariant(date, refDate, variant)
      : date;
  return !start
    ? null
    : intervalToDuration({
        start,
        end: refDate,
      });
}
