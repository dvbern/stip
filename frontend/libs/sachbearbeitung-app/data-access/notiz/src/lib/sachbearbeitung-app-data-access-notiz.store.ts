import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { GesuchNotiz, GesuchNotizService } from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';

type NotizState = {
  notizen: CachedRemoteData<GesuchNotiz[]>;
};

const initialState: NotizState = {
  notizen: initial(),
};

@Injectable()
export class NotizStore extends signalStore(
  { protectedState: false },
  withState(initialState),
  withDevtools('NotizStore'),
) {
  private notizService = inject(GesuchNotizService);

  notizenListViewSig = computed(() => {
    return fromCachedDataSig(this.notizen);
  });

  loadNotizen$ = rxMethod<{ gesuchId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          notizen: cachedPending(state.notizen),
        }));
      }),
      switchMap(({ gesuchId }) =>
        this.notizService
          .getNotizen$({ gesuchId })
          .pipe(handleApiResponse((notizen) => patchState(this, { notizen }))),
      ),
    ),
  );

  // saveNotiz$ = rxMethod<{
  //   notizId: string;
  //   values: unknown;
  // }>(
  //   pipe(
  //     tap(() => {
  //       patchState(this, (state) => ({
  //         cachedNotiz: cachedPending(state.cachedNotiz),
  //       }));
  //     }),
  //     switchMap(({ notizId, values }) =>
  //       this.notizService
  //         .updateNotiz$({
  //           notizId,
  //           payload: values,
  //         })
  //         .pipe(
  //           handleApiResponse(
  //             (notiz) => {
  //               patchState(this, { notiz });
  //             },
  //             {
  //               onSuccess: (notiz) => {
  //                 // Do something after save, like showing a notification
  //               },
  //             },
  //           ),
  //         ),
  //     ),
  //   ),
  // );
}
