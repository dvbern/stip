import { Injectable, inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { catchError, map, of, switchMap } from 'rxjs';

import { SharedEventGesuchDokumente } from '@dv/shared/event/gesuch-dokumente';
import { Dokument } from '@dv/shared/model/gesuch';
import { sharedUtilFnErrorTransformer } from '@dv/shared/util-fn/error-transformer';

import { SharedDataAccessDokumenteApiEvents } from './shared-data-access-dokumente.events';

export const loadDokumentes = createEffect(
  (events$ = inject(Actions), gesuchService = inject(GesuchServiceMock)) => {
    return events$.pipe(
      ofType(SharedEventGesuchDokumente.init),
      switchMap(() =>
        gesuchService.getDokumente$().pipe(
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

export const doumentesMockData: Dokument[] = [
  {
    id: '1',
    filename: 'Test File Name 1',
    filepfad: 'test/file/pfad',
    filesize: '123456',
    objectId: 'eltern.5',
  },
  {
    id: '2',
    filename: 'Test File Name 2 sehr langer name.pfd',
    filepfad: 'test/file/pfad',
    filesize: '123456',
    objectId: 'education.2',
  },
  {
    id: '3',
    filename: 'Test File Name 3',
    filepfad: 'test/file/pfad',
    filesize: '123156',
    objectId: 'kinder.8',
  },
  {
    id: '4',
    filename: 'Test File Name 4',
    filepfad: 'test/file/pfad',
    filesize: '123654',
    objectId: 'lebenslauf.3',
  },
];

// for testing purposes
@Injectable({
  providedIn: 'root',
})
export class GesuchServiceMock {
  getDokumente$() {
    return of(doumentesMockData);
  }
}
