import { inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { catchError, map, switchMap } from 'rxjs';

import { Land, StammdatenService } from '@dv/shared/model/gesuch';
import { sharedUtilFnErrorTransformer } from '@dv/shared/util-fn/error-transformer';

import { SharedDataAccessStammdatenApiEvents } from './shared-data-access-stammdaten.events';

export const loadStammdatens = createEffect(
  (
    events$ = inject(Actions),
    stammdatenService = inject(StammdatenService),
  ) => {
    return events$.pipe(
      ofType(SharedDataAccessStammdatenApiEvents.init),
      switchMap(() =>
        stammdatenService.getLaender$().pipe(
          map((laender) =>
            SharedDataAccessStammdatenApiEvents.stammdatensLoadedSuccess({
              laender: laender as Land[],
            }),
          ),
          catchError((error) => [
            SharedDataAccessStammdatenApiEvents.stammdatensLoadedFailure({
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
export const sharedDataAccessStammdatenEffects = { loadStammdatens };
