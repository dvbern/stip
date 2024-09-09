import { inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { catchError, switchMap } from 'rxjs';

import { SharedEventGesuchFormAbschluss } from '@dv/shared/event/gesuch-form-abschluss';
import { GesuchService, GesuchTrancheService } from '@dv/shared/model/gesuch';
import { shouldIgnoreErrorsIf } from '@dv/shared/util/http';
import { sharedUtilFnErrorTransformer } from '@dv/shared/util-fn/error-transformer';

import { GesuchAppDataAccessAbschlussApiEvents } from './gesuch-app-data-access-abschluss.events';

export const gesuchCheck = createEffect(
  (events$ = inject(Actions), gesuchService = inject(GesuchService)) => {
    return events$.pipe(
      ofType(GesuchAppDataAccessAbschlussApiEvents.check),
      switchMap(({ gesuchId }) =>
        gesuchService
          .gesuchEinreichenValidieren$({ gesuchId }, undefined, undefined, {
            context: shouldIgnoreErrorsIf(true),
          })
          .pipe(
            switchMap((validation) => [
              GesuchAppDataAccessAbschlussApiEvents.gesuchCheckSuccess({
                error: sharedUtilFnErrorTransformer({ error: validation }),
              }),
            ]),
            catchError((error) => [
              GesuchAppDataAccessAbschlussApiEvents.gesuchCheckFailure({
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
      ofType(GesuchAppDataAccessAbschlussApiEvents.gesuchAbschliessen),
      switchMap(({ gesuchId }) =>
        gesuchService.gesuchEinreichen$({ gesuchId }).pipe(
          switchMap(() => [
            GesuchAppDataAccessAbschlussApiEvents.abschlussSuccess(),
            SharedEventGesuchFormAbschluss.init(),
          ]),
          catchError((error) => [
            GesuchAppDataAccessAbschlussApiEvents.abschlussFailure({
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
      ofType(GesuchAppDataAccessAbschlussApiEvents.trancheAbschliessen),
      switchMap(({ trancheId }) =>
        trancheService.aenderungEinreichen$({ aenderungId: trancheId }).pipe(
          switchMap(() => [
            GesuchAppDataAccessAbschlussApiEvents.abschlussSuccess(),
            SharedEventGesuchFormAbschluss.init(),
          ]),
          catchError((error) => [
            GesuchAppDataAccessAbschlussApiEvents.abschlussFailure({
              error: sharedUtilFnErrorTransformer(error),
            }),
          ]),
        ),
      ),
    ),
  { functional: true },
);

// add effects here
export const gesuchAppDataAccessAbschlussEffects = {
  gesuchCheck,
  gesuchEinreichen,
  trancheEinreichen,
};
