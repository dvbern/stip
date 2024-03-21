import { Inject, Injectable, Optional, inject } from '@angular/core';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { DateFnsAdapter } from '@angular/material-date-fns-adapter';
import { TranslateService } from '@ngx-translate/core';
import { de, frCH } from 'date-fns/locale';
import { startWith } from 'rxjs';

@Injectable()
export class DvDateAdapter extends DateFnsAdapter {
  translate = inject(TranslateService);

  constructor(@Optional() @Inject(MAT_DATE_LOCALE) matDateLocale?: string) {
    // Not relevant once the base adapter is implemented with `inject` instead of `@Inject`
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    console.log('DvDateAdapter', matDateLocale as any);
    super(matDateLocale as any);

    this.translate.onLangChange
      .pipe(startWith({ lang: this.translate.currentLang }))
      .subscribe(({ lang }) => {
        console.log('LOCALE?', { locale: getLocaleFromLanguage(lang), lang });
        this.setLocale(getLocaleFromLanguage(lang));
      });
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
