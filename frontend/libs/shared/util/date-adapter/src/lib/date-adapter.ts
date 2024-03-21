import { Injectable, inject } from '@angular/core';
import { DateFnsAdapter } from '@angular/material-date-fns-adapter';
import { TranslateService } from '@ngx-translate/core';
import { de, frCH } from 'date-fns/locale';

@Injectable()
export class DvDateAdapter extends DateFnsAdapter {
  translate = inject(TranslateService);

  constructor(locale: Record<string, never>) {
    super(locale);

    this.translate.onLangChange.subscribe(() => {
      //this.setLocale();
      const language = this.translate.currentLang;
      if (language === 'de') {
        this.setLocale(de);
      } else if (language === 'fr') {
        this.setLocale(frCH);
      } else {
        throw new Error('Diese Sprache existiert nicht');
      }
    });
  }
}
