import { createSelector } from '@ngrx/store';

import { SharedModelGlobalNotification } from '@dv/shared/model/global-notification';

import { sharedDataAccessGlobalNotificationsFeature } from './shared-data-access-global-notification.feature';

export const selectSharedDataAccessGlobalNotificationsView = createSelector(
  sharedDataAccessGlobalNotificationsFeature.selectGlobalNotificationsState,
  (state) => ({
    ...state,
    notificationList: Object.entries(
      state.globalNotificationsById,
    ).map<SharedModelGlobalNotification>(([id, error]) => ({
      id: +id,
      autohide: false,
      content: error,
      message: error.message,
      messageKey: error.messageKey,
    })),
  }),
);
