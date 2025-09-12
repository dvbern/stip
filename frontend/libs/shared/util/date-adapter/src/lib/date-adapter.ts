import { Injectable, inject } from '@angular/core';
import { DateFnsAdapter } from '@angular/material-date-fns-adapter';
import { TranslocoService } from '@jsverse/transloco';
import { de, frCH } from 'date-fns/locale';
import { startWith } from 'rxjs';

import { parseDateForVariant } from '@dv/shared/util/validator-date';

@Injectable()
export class DvDateAdapter extends DateFnsAdapter {
  translate = inject(TranslocoService);

  constructor() {
    super();

    this.translate.langChanges$
      .pipe(startWith(this.translate.getActiveLang()))
      .subscribe((lang) => {
        this.setLocale(getLocaleFromLanguage(lang));
      });
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars, @typescript-eslint/no-explicit-any
  override parse(value: any, parseFormat: string | string[]): Date | null {
    if (value === '') {
      return null;
    }
    return parseDateForVariant(value, new Date(), 'date') ?? this.invalid();
  }
}

const getLocaleFromLanguage = (language: string) => {
  if (language === 'de') {
    return de;
  } else if (language === 'fr') {
    return frCH;
  } else {
    throw new Error('Diese Sprache existiert nicht');
  }
};
