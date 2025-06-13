import { Provider } from '@angular/core';
import { DateAdapter, MatDateFormats } from '@angular/material/core';
import { provideDateFnsAdapter } from '@angular/material-date-fns-adapter';

import { DvDateAdapter } from './date-adapter';

export const provideDvDateAdapter = (): Provider[] => {
  return [
    // Import default datefns adapter for correct MAT_DATE_LOCALE settings
    provideDateFnsAdapter(),
    // Re-Override the default date adapter with the custom one
    {
      provide: DateAdapter,
      useClass: DvDateAdapter,
    },
  ];
};

export const MONTH_YEAR_FORMAT: MatDateFormats = {
  parse: {
    dateInput: 'MM.yyyy',
  },
  display: {
    dateInput: 'MM.yyyy',
    monthYearLabel: 'MMM yyyy',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM yyyy',
  },
};

export const MAT_DEFAULT_FORMATS: MatDateFormats = {
  parse: {
    dateInput: 'dd.MM.yyyy',
  },
  display: {
    dateInput: 'dd.MM.yyyy',
    monthYearLabel: 'MMM yyyy',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM yyyy',
  },
};
