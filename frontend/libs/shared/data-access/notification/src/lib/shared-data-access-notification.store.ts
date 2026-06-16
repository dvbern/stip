import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import {
  Notification,
  NotificationService,
  NotificationServiceGetNotificationsForFallRequestParams,
} from '@dv/shared/model/gesuch';
import { getNotificationTranslationKey } from '@dv/shared/model/nachricht';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';

type NotificationState = {
  notifications: CachedRemoteData<Notification[]>;
};

const initialState: NotificationState = {
  notifications: initial(),
};

@Injectable()
export class NotificationStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private notificationService = inject(NotificationService);

  notificationListViewSig = computed(() => {
    const n = fromCachedDataSig(this.notifications) ?? [];
    return n.map((notification) => ({
      ...notification,
      translationKey: getNotificationTranslationKey(notification),
    }));
  });

  getNotificationsForFall$ =
    rxMethod<NotificationServiceGetNotificationsForFallRequestParams>(
      pipe(
        tap(() => {
          patchState(this, (state) => ({
            notifications: cachedPending(state.notifications),
          }));
        }),
        switchMap(({ fallId }) =>
          this.notificationService
            .getNotificationsForFall$({ fallId })
            .pipe(
              handleApiResponse((notifications) =>
                patchState(this, { notifications }),
              ),
            ),
        ),
      ),
    );
}
