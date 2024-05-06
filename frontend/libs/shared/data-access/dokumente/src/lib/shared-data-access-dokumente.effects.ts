import { inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { catchError, map, switchMap } from 'rxjs';

import { SharedEventGesuchDokumente } from '@dv/shared/event/gesuch-dokumente';
import { GesuchService } from '@dv/shared/model/gesuch';
import { sharedUtilFnErrorTransformer } from '@dv/shared/util-fn/error-transformer';

import { SharedDataAccessDokumenteApiEvents } from './shared-data-access-dokumente.events';

export const loadDokumentes = createEffect(
  (events$ = inject(Actions), gesuchService = inject(GesuchService)) => {
    return events$.pipe(
      ofType(SharedEventGesuchDokumente.loadDocuments),
      switchMap(({ gesuchId }) =>
        gesuchService.getGesuchDokumente$({ gesuchId }).pipe(
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

export const getRequiredDocumentTypes = createEffect(
  (events$ = inject(Actions), gesuchService = inject(GesuchService)) => {
    return events$.pipe(
      ofType(SharedEventGesuchDokumente.loadDocuments),
      switchMap(({ gesuchId }) =>
        gesuchService.getRequiredGesuchDokumentTyp$({ gesuchId }).pipe(
          map((requiredDocumentTypes) =>
            SharedDataAccessDokumenteApiEvents.getRequiredDocumentTypeSuccess({
              requiredDocumentTypes,
            }),
          ),
          catchError((error) => [
            SharedDataAccessDokumenteApiEvents.getRequiredDocumentTypeFailure({
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
export const sharedDataAccessDokumenteEffects = {
  loadDokumentes,
  getRequiredDocumentTypes,
};
