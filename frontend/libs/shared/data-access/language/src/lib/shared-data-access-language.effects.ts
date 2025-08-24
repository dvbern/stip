import { inject } from '@angular/core';
import { TranslocoService } from '@jsverse/transloco';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { map, tap } from 'rxjs';

import { DEFAULT_LANGUAGE } from '@dv/shared/model/language';

import { SharedDataAccessLanguageEvents } from './shared-data-access-language.events';
import { SharedDataAccessLanguageService } from './shared-data-access-language.service';

export const resolveLanguageOnInit = createEffect(
  (
    actions$ = inject(Actions),
    languageService = inject(SharedDataAccessLanguageService),
  ) => {
    return actions$.pipe(
      ofType(SharedDataAccessLanguageEvents.appInit),
      map(() => {
        const languageFromLocalStorage =
          languageService.getLanguageFromLocalStorage();
        if (languageFromLocalStorage) {
          return SharedDataAccessLanguageEvents.resolvedFromLocalStorage({
            language: languageFromLocalStorage,
          });
        }
        const languageFromBrowser = languageService.getLanguageFromBrowser();
        if (languageFromBrowser) {
          return SharedDataAccessLanguageEvents.resolvedFromBrowser({
            language: languageFromBrowser,
          });
        }
        return SharedDataAccessLanguageEvents.resolvedDefault({
          language: DEFAULT_LANGUAGE,
        });
      }),
    );
  },
  { functional: true },
);

export const syncLanguageToNgxTranslate = createEffect(
  (actions$ = inject(Actions), translocoService = inject(TranslocoService)) => {
    return actions$.pipe(
      ofType(
        SharedDataAccessLanguageEvents.resolvedDefault,
        SharedDataAccessLanguageEvents.resolvedFromBrowser,
        SharedDataAccessLanguageEvents.resolvedFromLocalStorage,
        SharedDataAccessLanguageEvents.headerMenuSelectorChange,
        SharedDataAccessLanguageEvents.footerSelectorChange,
      ),
      tap(({ language }) => {
        translocoService.setActiveLang(language);
      }),
    );
  },
  { functional: true, dispatch: false },
);

export const persistLanguageIntoLocalStorage = createEffect(
  (
    actions$ = inject(Actions),
    languageService = inject(SharedDataAccessLanguageService),
  ) => {
    return actions$.pipe(
      ofType(
        SharedDataAccessLanguageEvents.headerMenuSelectorChange,
        SharedDataAccessLanguageEvents.footerSelectorChange,
      ),
      tap(({ language }) => {
        languageService.setLanguageIntoLocalStorage(language);
      }),
    );
  },
  { functional: true, dispatch: false },
);

// add effects here
export const sharedDataAccessLanguageEffects = {
  resolveLanguageOnInit,
  syncLanguageToNgxTranslate,
  persistLanguageIntoLocalStorage,
};
