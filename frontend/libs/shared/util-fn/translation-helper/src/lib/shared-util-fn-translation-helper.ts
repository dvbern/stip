import { TranslateService } from '@ngx-translate/core';
import { Observable, startWith } from 'rxjs';

export function onTranslationChanges$(
  translate: TranslateService,
): Observable<{ lang: string; translations: Record<string, string> }> {
  return translate.onLangChange.pipe(
    startWith({
      lang: translate.currentLang,
      translations: translate.translations[translate.currentLang],
    }),
  );
}
