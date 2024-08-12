import {
  asBackendLocalDate,
  dateFromMonthYearString,
  formatBackendLocalDate,
  fromBackendLocalDate,
  getDateDifference,
  getLastDayOfYear,
  parseBackendLocalDateAndPrint,
  parseInputDateString,
  parseInputDateStringVariants,
  parseStringAndPrintForBackendLocalDate,
  printDate,
  printDateAsMonthYear,
  toBackendLocalDate,
} from './date-util';

describe('date-util functions', () => {
  it('parseBackendLocalDateAndPrint', () => {
    expect(parseBackendLocalDateAndPrint('2021-01-01', 'de')).toBe(
      '01.01.2021',
    );
  });

  it('printDate', () => {
    expect(printDate(new Date(2021, 0, 1), 'de', 'date')).toBe('01.01.2021');
  });

  it('toBackendLocalDate', () => {
    expect(toBackendLocalDate(new Date(2021, 0, 1))).toBe('2021-01-01');
  });

  it('fromBackendLocalDate', () => {
    expect(fromBackendLocalDate('2021-01-01')).toEqual(new Date(2021, 0, 1));
  });

  it('formatBackendLocalDate', () => {
    expect(formatBackendLocalDate('2021-01-01', 'de')).toBe('01.01.2021');
  });

  it('parseStringAndPrintForBackendLocalDate', () => {
    expect(
      parseStringAndPrintForBackendLocalDate('01.01.2021', 'de', new Date()),
    ).toBe('2021-01-01');
  });

  it('parseInputDateStringVariants', () => {
    expect(parseInputDateStringVariants('01.01.2021', ['dd.MM.yyyy'])).toEqual([
      new Date(2021, 0, 1),
    ]);
  });

  it('parseInputDateString', () => {
    expect(
      parseInputDateString('01.01.2021', ['dd.MM.yyyy'], new Date(2021, 0, 1)),
    ).toEqual(new Date(2021, 0, 1));
  });

  it('asBackendLocalDate', () => {
    expect(
      asBackendLocalDate('01.01.2021', ['dd.MM.yyyy'], new Date(2021, 0, 1)),
    ).toBe('2021-01-01');
  });

  it('dateFromMonthYearString', () => {
    expect(dateFromMonthYearString('01.2021')).toEqual(new Date(2021, 0, 1));
  });

  it('printDateAsMonthYear', () => {
    expect(printDateAsMonthYear(new Date(2021, 0, 1))).toBe('01.2021');
  });

  it('getLastDayOfYear', () => {
    expect(getLastDayOfYear(2021)).toEqual(new Date(2021, 11, 31));
  });

  it.each([
    [new Date(2021, 0, 1), new Date(2021, 0, 1), {}],
    [new Date(2021, 0, 1), new Date(2021, 0, 2), { days: 1 }],
    [new Date(2021, 0, 1), new Date(2021, 1, 1), { months: 1 }],
    [new Date(2021, 0, 1), new Date(2022, 0, 1), { years: 1 }],
    [new Date(2021, 0, 1), new Date(2022, 2, 1), { years: 1, months: 2 }],
    [new Date(2021, 0, 1), new Date(2022, 0, 4), { years: 1, days: 3 }],
  ])('getDateDifference - %p <-> %p [%p]', (date1, date2, expected) => {
    expect(getDateDifference(date1, date2)).toStrictEqual(expected);
  });
});
