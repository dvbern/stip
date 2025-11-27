import { Injectable, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import {
  Darlehen,
  DarlehenService,
  DarlehenServiceDarlehenAblehenRequestParams,
  DarlehenServiceDarlehenAkzeptierenRequestParams,
  DarlehenServiceDarlehenEingebenRequestParams,
  DarlehenServiceDarlehenFreigebenRequestParams,
  DarlehenServiceDarlehenUpdateGsRequestParams,
  DarlehenServiceDarlehenUpdateSbRequestParams,
  DarlehenServiceDarlehenZurueckweisenRequestParams,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  RemoteData,
  cachedPending,
  handleApiResponse,
  initial,
  pending,
} from '@dv/shared/util/remote-data';

type DarlehenState = {
  getDarlehenRequest: RemoteData<Darlehen | null>;
  cachedDarlehen: CachedRemoteData<Darlehen>;
};

const initialState: DarlehenState = {
  getDarlehenRequest: initial(),
  cachedDarlehen: initial(),
};

@Injectable()
export class DarlehenStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private darlehenService = inject(DarlehenService);

  getDarlehen$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          cachedDarlehen: pending(),
        }));
      }),
      // switchMap(() =>
      //   this.darlehenService
      //     .getDarlehen$()
      //     .pipe(
      //       handleApiResponse((darlehen) => patchState(this, { darlehen })),
      //     ),
      // ),
    ),
  );

  createDarlehen$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedDarlehen: cachedPending(state.cachedDarlehen),
        }));
      }),
      switchMap(() =>
        this.darlehenService.createDarlehen$().pipe(
          handleApiResponse((darlehen) => {
            patchState(this, { cachedDarlehen: darlehen });
          }),
        ),
      ),
    ),
  );

  darlehenUpdateGS$ = rxMethod<DarlehenServiceDarlehenUpdateGsRequestParams>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedDarlehen: cachedPending(state.cachedDarlehen),
        }));
      }),
      switchMap((data) =>
        this.darlehenService.darlehenUpdateGs$(data).pipe(
          handleApiResponse((darlehen) => {
            patchState(this, { cachedDarlehen: darlehen });
          }),
        ),
      ),
    ),
  );

  darlehenEingeben$ = rxMethod<DarlehenServiceDarlehenEingebenRequestParams>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedDarlehen: cachedPending(state.cachedDarlehen),
        }));
      }),
      switchMap((data) =>
        this.darlehenService.darlehenEingeben$(data).pipe(
          handleApiResponse((darlehen) => {
            patchState(this, { cachedDarlehen: darlehen });
          }),
        ),
      ),
    ),
  );

  // SB Methoden

  darlehenUpdateSb$ = rxMethod<DarlehenServiceDarlehenUpdateSbRequestParams>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedDarlehen: cachedPending(state.cachedDarlehen),
        }));
      }),
      switchMap((data) =>
        this.darlehenService.darlehenUpdateSb$(data).pipe(
          handleApiResponse((darlehen) => {
            patchState(this, { cachedDarlehen: darlehen });
          }),
        ),
      ),
    ),
  );

  darlehenFreigeben$ = rxMethod<DarlehenServiceDarlehenFreigebenRequestParams>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedDarlehen: cachedPending(state.cachedDarlehen),
        }));
      }),
      switchMap((data) =>
        this.darlehenService.darlehenFreigeben$(data).pipe(
          handleApiResponse((darlehen) => {
            patchState(this, { cachedDarlehen: darlehen });
          }),
        ),
      ),
    ),
  );

  darlehenZurueckweisen$ =
    rxMethod<DarlehenServiceDarlehenZurueckweisenRequestParams>(
      pipe(
        tap(() => {
          patchState(this, (state) => ({
            cachedDarlehen: cachedPending(state.cachedDarlehen),
          }));
        }),
        switchMap((data) =>
          this.darlehenService.darlehenZurueckweisen$(data).pipe(
            handleApiResponse((darlehen) => {
              patchState(this, { cachedDarlehen: darlehen });
            }),
          ),
        ),
      ),
    );

  // SB Freigabestelle Methoden

  darlehenAkzeptieren$ =
    rxMethod<DarlehenServiceDarlehenAkzeptierenRequestParams>(
      pipe(
        tap(() => {
          patchState(this, (state) => ({
            cachedDarlehen: cachedPending(state.cachedDarlehen),
          }));
        }),
        switchMap((data) =>
          this.darlehenService.darlehenAkzeptieren$(data).pipe(
            handleApiResponse((darlehen) => {
              patchState(this, { cachedDarlehen: darlehen });
            }),
          ),
        ),
      ),
    );

  darlehenAblehen$ = rxMethod<DarlehenServiceDarlehenAblehenRequestParams>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedDarlehen: cachedPending(state.cachedDarlehen),
        }));
      }),
      switchMap((data) =>
        this.darlehenService.darlehenAblehen$(data).pipe(
          handleApiResponse((darlehen) => {
            patchState(this, { cachedDarlehen: darlehen });
          }),
        ),
      ),
    ),
  );
}
