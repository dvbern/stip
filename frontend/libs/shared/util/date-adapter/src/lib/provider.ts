import { Provider } from '@angular/core';
import { DateAdapter } from '@angular/material/core';
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
