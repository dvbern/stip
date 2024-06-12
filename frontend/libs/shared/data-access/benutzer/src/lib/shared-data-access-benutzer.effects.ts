import { inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { OAuthService } from 'angular-oauth2-oidc';
import { catchError, filter, map, switchMap, tap, withLatestFrom } from 'rxjs';

import { BenutzerService } from '@dv/shared/model/gesuch';
import { sharedUtilFnErrorTransformer } from '@dv/shared/util-fn/error-transformer';

import { SharedDataAccessBenutzerApiEvents } from './shared-data-access-benutzer.events';
import { selectLastFetchTs } from './shared-data-access-benutzer.feature';

const THREE_MINUTES = 1000 * 60 * 3;

export const loadCurrentBenutzer = createEffect(
  (
    store = inject(Store),
    events$ = inject(Actions),
    benutzerService = inject(BenutzerService),
    oauthService = inject(OAuthService),
  ) => {
    return events$.pipe(
      ofType(SharedDataAccessBenutzerApiEvents.loadCurrentBenutzer),
      filter(() => oauthService.hasValidIdToken()),
      withLatestFrom(store.select(selectLastFetchTs)),
      filter(([, lastFetchTs]) => hasStaleCache(lastFetchTs)),
      tap(() =>
        store.dispatch(
          SharedDataAccessBenutzerApiEvents.setCurrentBenutzerPending(),
        ),
      ),
      switchMap(() =>
        benutzerService.prepareCurrentBenutzer$().pipe(
          map((benutzer) =>
            SharedDataAccessBenutzerApiEvents.currentBenutzerLoadedSuccess({
              benutzer,
            }),
          ),
          catchError((error) => [
            SharedDataAccessBenutzerApiEvents.currentBenutzerLoadedFailure({
              error: sharedUtilFnErrorTransformer(error),
            }),
          ]),
        ),
      ),
    );
  },
  { functional: true },
);

// add effects here
export const sharedDataAccessBenutzerEffects = { loadCurrentBenutzer };

const hasStaleCache = (lastFetchTs: number | null) =>
  lastFetchTs === null || Date.now() - lastFetchTs > THREE_MINUTES;
