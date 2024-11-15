import { Pipe, PipeTransform, inject } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { format, isValid } from 'date-fns';
import { de } from 'date-fns/locale/de';
import { frCH } from 'date-fns/locale/fr-CH';
import { Observable, map, of, startWith } from 'rxjs';

import { Language } from '@dv/shared/model/language';

const localeMap = {
  de: de,
  fr: frCH,
} satisfies Record<Language, unknown>;

@Pipe({ name: 'translatedDate', standalone: true })
export class SharedUiTranslatedDatePipe implements PipeTransform {
  private translate = inject(TranslateService);

  transform(
    value: Date | string | undefined | null,
    formatStr: string,
  ): Observable<string | null> {
    const date = typeof value === 'string' ? new Date(value) : value;
    if (!date || !isValid(date)) {
      return of(null);
    }

    return this.translate.onLangChange.pipe(
      startWith({ lang: this.translate.currentLang }),
      map(({ lang }) =>
        format(date, formatStr, {
          locale: isLanguage(lang) ? localeMap[lang] : de,
        }),
      ),
    );
  }
}

const isLanguage = (lang: string): lang is Language => lang in localeMap;
