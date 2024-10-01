import { Injectable, computed } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';

import { SharedModelError } from '@dv/shared/model/error';
import {
  CreateNotification,
  Notification,
  NotificationType,
  SharedModelGlobalNotification,
} from '@dv/shared/model/global-notification';

type MessageOrKey =
  | Required<Pick<CreateNotification, 'message'>>
  | Required<Pick<CreateNotification, 'messageKey'>>;

const CRITICAL_NOTIFICATIONS: NotificationType[] = ['ERROR', 'SEVERE'];
const PERMANENT_NOTIFICATIONS: NotificationType[] = ['ERROR_PERMANENT'];

export interface State {
  nextNotificationId: number;
  notifications: Notification[];
}

const initialState: State = {
  nextNotificationId: 1,
  notifications: [],
};

@Injectable({ providedIn: 'root' })
export class GlobalNotificationStore extends signalStore(
  { protectedState: false },
  withDevtools('GlobalNotificationStore'),
  withState(initialState),
) {
  mostImportantNotificationSig = computed(() =>
    getMostImportantNotification(this.notifications()),
  );

  /**
   * Add a new notification to the list of notifications.
   */
  createNotification(notification: CreateNotification) {
    return patchState(this, (state) => ({
      nextNotificationId: state.nextNotificationId + 1,
      notifications: [
        {
          id: state.nextNotificationId,
          ...notification,
        },
      ],
    }));
  }

  /**
   * Helper function to create a new success notification.
   */
  createSuccessNotification(notification: MessageOrKey) {
    return this.createNotification({
      type: 'SUCCESS',
      ...notification,
    });
  }

  /**
   * Helper function to handle a failed http request.
   */
  handleHttpRequestFailed(errors: SharedModelError[]) {
    return patchState(this, (state) => ({
      nextNotificationId: state.nextNotificationId + 1 + errors.length,
      notifications: [
        ...this.notifications(),
        ...errors.map((error, i) => ({
          id: state.nextNotificationId + i,
          type: 'ERROR' as const,
          content: error,
        })),
      ],
    }));
  }

  /**
   * Helper function to create a new error notification specific to 403 Forbidden errors.
   */
  handleForbiddenError(baseError?: SharedModelError) {
    return this.createNotification({
      type: 'ERROR',
      messageKey: 'shared.genericError.forbidden',
      content: baseError,
    });
  }

  /**
   * Remove a notification from the list of notifications.
   */
  removeNotification(notificationId: number) {
    const notifications = this.notifications().filter(
      (notification) => notification.id !== notificationId,
    );

    return patchState(this, {
      notifications: notifications,
    });
  }

  /**
   * Clear all notifications that are not important.
   */
  clearUnimportantNotifications() {
    const notifications = this.notifications().filter((notification) =>
      CRITICAL_NOTIFICATIONS.includes(notification.type),
    );

    return patchState(this, {
      notifications: notifications,
    });
  }

  /**
   * Clear all persistent notifications.
   */
  clearNonPermanentNotifications() {
    const notifications = this.notifications().filter(
      (notification) =>
        PERMANENT_NOTIFICATIONS.includes(notification.type) ||
        notification.type === 'SUCCESS',
    );

    return patchState(this, {
      notifications: notifications,
    });
  }
}

const PRIORITY: NotificationType[] = [
  'SUCCESS',
  'INFO',
  'SEVERE',
  'ERROR',
  'ERROR_PERMANENT',
  'WARNING',
];
/**
 * Returns the most important notification from the list of notifications.
 *
 * If there are multiple notifications of the same type, the first one is returned
 * and once it is dismissed, the next one will be shown.
 */
const getMostImportantNotification = (
  notifications: State['notifications'],
): SharedModelGlobalNotification | undefined => {
  const groupedNotifications = notifications.reduce(
    (acc, notification) => {
      const type = notification.type;
      return {
        ...acc,
        [type]: [...(acc[type] ?? []), notification],
      };
    },
    {} as Record<NotificationType, Notification[]>,
  );
  const type = PRIORITY.find((type) => groupedNotifications[type]?.length > 0);
  if (!type) {
    return undefined;
  }
  return {
    autohide: !PERMANENT_NOTIFICATIONS.includes(type),
    type,
    list: groupedNotifications[type],
  };
};
