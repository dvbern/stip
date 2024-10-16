import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { Notification, NotificationService } from '@dv/shared/model/gesuch';
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
  withDevtools('NotificationStore'),
) {
  private notificationService = inject(NotificationService);

  notificationListViewSig = computed(() => {
    return fromCachedDataSig(this.notifications) ?? [];
  });

  loadNotifications$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          notifications: cachedPending(state.notifications),
        }));
      }),
      switchMap(() =>
        this.notificationService
          .getNotificationsForCurrentUser$()
          .pipe(
            handleApiResponse((notifications) =>
              patchState(this, { notifications }),
            ),
          ),
      ),
    ),
  );
}
