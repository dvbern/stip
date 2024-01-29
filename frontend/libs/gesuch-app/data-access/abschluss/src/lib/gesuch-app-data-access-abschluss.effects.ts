import { inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { catchError, switchMap } from 'rxjs';

import { SharedEventGesuchFormAbschluss } from '@dv/shared/event/gesuch-form-abschluss';
import { GesuchService } from '@dv/shared/model/gesuch';
import { sharedUtilFnErrorTransformer } from '@dv/shared/util-fn/error-transformer';

import { GesuchAppDataAccessAbschlussApiEvents } from './gesuch-app-data-access-abschluss.events';

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

// add effects here
export const gesuchAppDataAccessAbschlussEffects = { gesuchEinreichen };
