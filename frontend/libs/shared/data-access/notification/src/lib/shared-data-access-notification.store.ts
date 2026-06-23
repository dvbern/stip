import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import {
  Notification,
  NotificationService,
  NotificationServiceGetNotificationsForFallRequestParams,
  NotificationServiceMarkNotificationAsReadRequestParams,
} from '@dv/shared/model/gesuch';
import { getNotificationTranslationKey } from '@dv/shared/model/nachricht';
import {
  CachedRemoteData,
  RemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';

type NotificationState = {
  notifications: CachedRemoteData<Notification[]>;
  markNotificationAsReadRequest: RemoteData<Notification>;
  selectedNotificationId: string | undefined;
};

const initialState: NotificationState = {
  notifications: initial(),
  markNotificationAsReadRequest: initial(),
  selectedNotificationId: undefined,
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

  setSelectedNotificationId(notificationId: string | undefined) {
    patchState(this, { selectedNotificationId: notificationId });
  }

  getNotificationsForFall$ = rxMethod<{
    req: NotificationServiceGetNotificationsForFallRequestParams;
    onSuccess: (notifications: Notification[]) => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          notifications: cachedPending(state.notifications),
        }));
      }),
      switchMap(({ req, onSuccess }) =>
        this.notificationService.getNotificationsForFall$(req).pipe(
          handleApiResponse(
            (notifications) => {
              patchState(this, { notifications });
            },
            {
              onSuccess,
            },
          ),
        ),
      ),
    ),
  );

  markNotificationAsRead$ =
    rxMethod<NotificationServiceMarkNotificationAsReadRequestParams>(
      pipe(
        tap(() => {
          patchState(this, { markNotificationAsReadRequest: initial() });
        }),
        switchMap(({ notificationId }) =>
          this.notificationService
            .markNotificationAsRead$({ notificationId })
            .pipe(
              handleApiResponse((notification) =>
                patchState(this, {
                  markNotificationAsReadRequest: notification,
                }),
              ),
            ),
        ),
      ),
    );
}
