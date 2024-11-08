import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/data-access/global-notification';
import {
  GesuchNotiz,
  GesuchNotizCreate,
  GesuchNotizService,
  GesuchNotizUpdate,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';

type NotizState = {
  notizen: CachedRemoteData<GesuchNotiz[]>;
  notiz: CachedRemoteData<GesuchNotiz>;
};

const initialState: NotizState = {
  notizen: initial(),
  notiz: initial(),
};

@Injectable()
export class NotizStore extends signalStore(
  { protectedState: false },
  withState(initialState),
  withDevtools('NotizStore'),
) {
  private notizService = inject(GesuchNotizService);
  private notificationStore = inject(GlobalNotificationStore);

  notizenListViewSig = computed(() => {
    return fromCachedDataSig(this.notizen);
  });

  notizViewSig = computed(() => {
    return fromCachedDataSig(this.notiz);
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

  loadNotiz$ = rxMethod<{ notizId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          notiz: cachedPending(state.notiz),
        }));
      }),
      switchMap(({ notizId }) =>
        this.notizService
          .getNotiz$({ notizId })
          .pipe(handleApiResponse((notiz) => patchState(this, { notiz }))),
      ),
    ),
  );

  editNotiz = rxMethod<{
    gesuchId: string;
    notizDaten: GesuchNotizUpdate;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          notiz: cachedPending(state.notiz),
        }));
      }),
      switchMap(({ gesuchId, notizDaten }) =>
        this.notizService
          .updateNotiz$({
            gesuchNotizUpdate: notizDaten,
          })
          .pipe(
            handleApiResponse(
              (notiz) => {
                patchState(this, { notiz });
              },
              {
                onSuccess: () => {
                  this.notificationStore.createSuccessNotification({
                    messageKey:
                      'sachbearbeitung-app.infos.notiz.bearbeiten.success',
                  });
                  this.loadNotizen$({ gesuchId });
                },
              },
            ),
          ),
      ),
    ),
  );

  createNotiz = rxMethod<{
    gesuchNotizCreate: GesuchNotizCreate;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          notiz: cachedPending(state.notiz),
        }));
      }),
      switchMap(({ gesuchNotizCreate }) =>
        this.notizService
          .createNotiz$({
            gesuchNotizCreate,
          })
          .pipe(
            handleApiResponse(
              (notiz) => {
                patchState(this, { notiz });
              },
              {
                onSuccess: () => {
                  this.notificationStore.createSuccessNotification({
                    messageKey:
                      'sachbearbeitung-app.infos.notiz.erstellen.success',
                  });
                  this.loadNotizen$({ gesuchId: gesuchNotizCreate.gesuchId });
                },
              },
            ),
          ),
      ),
    ),
  );

  deleteNotiz = rxMethod<{
    gesuchId: string;
    notizId: string;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          notiz: cachedPending(state.notiz),
        }));
      }),
      switchMap(({ gesuchId, notizId }) =>
        this.notizService
          .deleteNotiz$({
            notizId,
          })
          .pipe(
            handleApiResponse(
              (notiz) => {
                patchState(this, { notiz });
              },
              {
                onSuccess: () => {
                  this.notificationStore.createSuccessNotification({
                    messageKey:
                      'sachbearbeitung-app.infos.notiz.delete.success',
                  });
                  this.loadNotizen$({ gesuchId });
                },
              },
            ),
          ),
      ),
    ),
  );
}
