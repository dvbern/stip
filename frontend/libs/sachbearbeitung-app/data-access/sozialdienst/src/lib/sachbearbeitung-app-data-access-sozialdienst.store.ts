import { Injectable, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import {
  Sozialdienst,
  SozialdienstCreate,
  SozialdienstService,
  SozialdienstUpdate,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  RemoteData,
  cachedPending,
  handleApiResponse,
  initial,
  pending,
} from '@dv/shared/util/remote-data';

type SozialdienstState = {
  sozialdienste: CachedRemoteData<Sozialdienst[]>;
  sozialdienst: RemoteData<Sozialdienst>;
};

const initialState: SozialdienstState = {
  sozialdienste: initial(),
  sozialdienst: initial(),
};

@Injectable()
export class SozialdienstStore extends signalStore(
  { protectedState: false },
  withState(initialState),
  withDevtools('SozialdienstStore'),
) {
  private sozialdienstService = inject(SozialdienstService);

  // sozialdiensteListViewSig = computed(() => {
  //   return fromCachedDataSig(this.sozialdienste);
  // });

  // sozialdienstViewSig = computed(() => {
  //   return this.sozialdienst.data();
  // });

  loadAllSozialdienste$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          sozialdienste: cachedPending(state.sozialdienste),
        }));
      }),
      switchMap(() =>
        this.sozialdienstService
          .getAllSozialdienste$()
          .pipe(
            handleApiResponse((sozialdienste) =>
              patchState(this, { sozialdienste }),
            ),
          ),
      ),
    ),
  );

  loadSozialdienst$ = rxMethod<{ sozialdienstId: string }>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          sozialdienst: pending(),
        }));
      }),
      switchMap(({ sozialdienstId }) =>
        this.sozialdienstService
          .getSozialdienst$({ sozialdienstId })
          .pipe(
            handleApiResponse((sozialdienst) =>
              patchState(this, { sozialdienst }),
            ),
          ),
      ),
    ),
  );

  // saveSozialdienst$ = rxMethod<{
  //   sozialdienstId: string;
  //   values: unknown;
  // }>(
  //   pipe(
  //     tap(() => {
  //       patchState(this, (state) => ({
  //         sozialdienste: cachedPending(state.sozialdienste),
  //       }));
  //     }),
  //     switchMap(({ sozialdienstId, values }) =>
  //       this.sozialdienstService
  //         .updateSozialdienst$({
  //           sozialdienstId,
  //           payload: values,
  //         })
  //         .pipe(
  //           handleApiResponse(
  //             (sozialdienst) => {
  //               patchState(this, { sozialdienst });
  //             },
  //             {
  //               onSuccess: (sozialdienst) => {
  //                 // Do something after save, like showing a notification
  //               },
  //             },
  //           ),
  //         ),
  //     ),
  //   ),
  // );

  createSozialdienst$ = rxMethod<{ sozialdienstCreate: SozialdienstCreate }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          sozialdienste: cachedPending(state.sozialdienste),
        }));
      }),
      switchMap(({ sozialdienstCreate }) =>
        this.sozialdienstService
          .createSozialdienst$({ sozialdienstCreate })
          .pipe(
            handleApiResponse((sozialdienst) =>
              patchState(this, { sozialdienst }),
            ),
          ),
      ),
    ),
  );

  deleteSozialdienst$ = rxMethod<{ sozialdienstId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          sozialdienste: cachedPending(state.sozialdienste),
        }));
      }),
      switchMap(({ sozialdienstId }) =>
        this.sozialdienstService.deleteSozialdienst$({ sozialdienstId }).pipe(
          handleApiResponse(() => {
            patchState(this, (state) => ({
              sozialdienste: cachedPending(state.sozialdienste),
            }));
          }),
        ),
      ),
    ),
  );

  updateSozialdienst$ = rxMethod<{ sozialdienstUpdate: SozialdienstUpdate }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          sozialdienste: cachedPending(state.sozialdienste),
        }));
      }),
      switchMap(({ sozialdienstUpdate }) =>
        this.sozialdienstService
          .updateSozialdienst$({ sozialdienstUpdate })
          .pipe(
            handleApiResponse((sozialdienst) =>
              patchState(this, { sozialdienst }),
            ),
          ),
      ),
    ),
  );

  // updateSozialdienstAdmin$ = rxMethod<{ sozialdienstId: string; values: unknown }>(
  //   pipe(
  //     tap(() => {
  //       patchState(this, (state) => ({
  //         sozialdienste: cachedPending(state.sozialdienste),
  //       }));
  //     }),
  //     switchMap(({ sozialdienstId, values }) =>
  //       this.sozialdienstService
  //         .updateSozialdienstAdmin$({ sozialdienstId, payload: values })
  //         .pipe(
  //           handleApiResponse((sozialdienst) =>
  //             patchState(this, { sozialdienst }),
  //           ),
  //         ),
  //     ),
  //   ),
  // );

  // replaceSozialdienstAdmin$ = rxMethod<{ sozialdienstId: string; values: unknown }>(
  //   pipe(
  //     tap(() => {
  //       patchState(this, (state) => ({
  //         sozialdienste: cachedPending(state.sozialdienste),
  //       }));
  //     }),
  //     switchMap(({ sozialdienstId, values }) =>
  //       this.sozialdienstService
  //         .replaceSozialdienstAdmin$({ sozialdienstId, payload: values })
  //         .pipe(
  //           handleApiResponse((sozialdienst) =>
  //             patchState(this, { sozialdienst }),
  //           ),
  //         ),
  //     ),
  //   ),
  // );
}
