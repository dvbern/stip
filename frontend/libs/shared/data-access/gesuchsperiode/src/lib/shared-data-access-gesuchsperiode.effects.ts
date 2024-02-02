import { inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { catchError, map, switchMap } from 'rxjs';

import { GesuchsperiodeService } from '@dv/shared/model/gesuch';
import { sharedUtilFnErrorTransformer } from '@dv/shared/util-fn/error-transformer';

import { sharedDataAccessGesuchsperiodeEvents } from './shared-data-access-gesuchsperiode.events';

export const loadGesuchsperiodes = createEffect(
  (
    actions$ = inject(Actions),
    gesuchsperiodeService = inject(GesuchsperiodeService),
  ) => {
    return actions$.pipe(
      ofType(sharedDataAccessGesuchsperiodeEvents.init),
      switchMap(() =>
        gesuchsperiodeService.getGesuchsperioden$().pipe(
          map((gesuchsperiodes) =>
            sharedDataAccessGesuchsperiodeEvents.gesuchsperiodesLoadedSuccess({
              gesuchsperiodes,
            }),
          ),
          catchError((error) => [
            sharedDataAccessGesuchsperiodeEvents.gesuchsperiodesLoadedFailure({
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
export const sharedDataAccessGesuchsperiodeEffects = { loadGesuchsperiodes };
