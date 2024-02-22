import { inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { catchError, map, switchMap } from 'rxjs';

import { SharedEventGesuchDokumente } from '@dv/shared/event/gesuch-dokumente';
import { sharedUtilFnErrorTransformer } from '@dv/shared/util-fn/error-transformer';

import { SharedDataAccessDokumenteApiEvents } from './shared-data-access-dokumente.events';
import { SharedDataAccessDokumenteService } from './shared-data-access-dokumente.service';

export const loadDokumentes = createEffect(
  (
    events$ = inject(Actions),
    sharedDataAccessDokumenteService = inject(SharedDataAccessDokumenteService),
  ) => {
    return events$.pipe(
      ofType(SharedEventGesuchDokumente.init),
      switchMap(() =>
        sharedDataAccessDokumenteService.getAll().pipe(
          map((dokumentes) =>
            SharedDataAccessDokumenteApiEvents.dokumentesLoadedSuccess({
              dokumentes,
            }),
          ),
          catchError((error) => [
            SharedDataAccessDokumenteApiEvents.dokumentesLoadedFailure({
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
export const sharedDataAccessDokumenteEffects = { loadDokumentes };
