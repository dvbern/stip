import { Inject, Injectable, Optional, inject } from '@angular/core';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { DateFnsAdapter } from '@angular/material-date-fns-adapter';
import { TranslateService } from '@ngx-translate/core';
import { de, frCH } from 'date-fns/locale';
import { startWith } from 'rxjs';

@Injectable()
export class DvDateAdapter extends DateFnsAdapter {
  translate = inject(TranslateService);

  constructor(@Optional() @Inject(MAT_DATE_LOCALE) matDateLocale: object) {
    /** Not relevant once the base adapter is implemented with `inject` instead of `@Inject`
     *  @see https://github.com/angular/components/blob/main/src/material-date-fns-adapter/adapter/date-fns-adapter.ts#L55
     */
    super(matDateLocale);

    this.translate.onLangChange
      .pipe(startWith({ lang: this.translate.currentLang }))
      .subscribe(({ lang }) => {
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