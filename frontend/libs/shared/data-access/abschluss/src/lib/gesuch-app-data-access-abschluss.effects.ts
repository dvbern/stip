import { inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { catchError, switchMap } from 'rxjs';

import { SharedEventGesuchFormAbschluss } from '@dv/shared/event/gesuch-form-abschluss';
import { GesuchService, GesuchTrancheService } from '@dv/shared/model/gesuch';
import { shouldIgnoreErrorsIf } from '@dv/shared/util/http';
import { sharedUtilFnErrorTransformer } from '@dv/shared/util-fn/error-transformer';

import { SharedDataAccessAbschlussApiEvents } from './gesuch-app-data-access-abschluss.events';

export const gesuchCheck = createEffect(
  (
    events$ = inject(Actions),
    tranchenService = inject(GesuchTrancheService),
  ) => {
    return events$.pipe(
      ofType(SharedDataAccessAbschlussApiEvents.check),
      switchMap(({ gesuchTrancheId }) =>
        tranchenService
          .gesuchTrancheEinreichenValidieren$(
            { gesuchTrancheId },
            undefined,
            undefined,
            {
              context: shouldIgnoreErrorsIf(true),
            },
          )
          .pipe(
            switchMap((validation) => [
              SharedDataAccessAbschlussApiEvents.gesuchCheckSuccess({
                error: sharedUtilFnErrorTransformer({ error: validation }),
              }),
            ]),
            catchError((error) => [
              SharedDataAccessAbschlussApiEvents.gesuchCheckFailure({
                error: sharedUtilFnErrorTransformer(error),
              }),
            ]),
          ),
      ),
    );
  },
  { functional: true },
);

export const gesuchEinreichen = createEffect(
  (events$ = inject(Actions), gesuchService = inject(GesuchService)) => {
    return events$.pipe(
      ofType(SharedDataAccessAbschlussApiEvents.gesuchAbschliessen),
      switchMap(({ gesuchId }) =>
        gesuchService.gesuchEinreichen$({ gesuchId }).pipe(
          switchMap(() => [
            SharedDataAccessAbschlussApiEvents.abschlussSuccess(),
            SharedEventGesuchFormAbschluss.init(),
          ]),
          catchError((error) => [
            SharedDataAccessAbschlussApiEvents.abschlussFailure({
              error: sharedUtilFnErrorTransformer(error),
            }),
          ]),
        ),
      ),
    );
  },
  { functional: true },
);

export const trancheEinreichen = createEffect(
  (events$ = inject(Actions), trancheService = inject(GesuchTrancheService)) =>
    events$.pipe(
      ofType(SharedDataAccessAbschlussApiEvents.trancheAbschliessen),
      switchMap(({ trancheId }) =>
        trancheService.aenderungEinreichen$({ aenderungId: trancheId }).pipe(
          switchMap(() => [
            SharedDataAccessAbschlussApiEvents.abschlussSuccess(),
            SharedEventGesuchFormAbschluss.init(),
          ]),
          catchError((error) => [
            SharedDataAccessAbschlussApiEvents.abschlussFailure({
              error: sharedUtilFnErrorTransformer(error),
            }),
          ]),
        ),
      ),
    ),
  { functional: true },
);

// add effects here
export const sharedDataAccessAbschlussEffects = {
  gesuchCheck,
  gesuchEinreichen,
  trancheEinreichen,
};
