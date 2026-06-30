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
import {
  CachedRemoteData,
  RemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  isSuccess,
  mapCachedData,
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

  notificationListViewSig = computed(
    () => fromCachedDataSig(this.notifications) ?? [],
  );

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

  markNotificationAsRead$ = rxMethod<{
    req: NotificationServiceMarkNotificationAsReadRequestParams;
    onSuccess: (res: Notification) => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, { markNotificationAsReadRequest: initial() });
      }),
      switchMap(({ req, onSuccess }) =>
        this.notificationService.markNotificationAsRead$(req).pipe(
          handleApiResponse(
            (res) =>
              patchState(this, (state) => ({
                markNotificationAsReadRequest: res,
                notifications: mapCachedData(
                  state.notifications,
                  (notifications) => {
                    if (isSuccess(res)) {
                      return notifications.map((notification) =>
                        notification.id === req.notificationId
                          ? { ...notification, read: true }
                          : notification,
                      );
                    }
                    return notifications;
                  },
                ),
              })),
            { onSuccess },
          ),
        ),
      ),
    ),
  );
}
