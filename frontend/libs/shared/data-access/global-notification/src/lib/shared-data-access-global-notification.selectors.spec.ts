import { selectSharedDataAccessGlobalNotificationsView } from './shared-data-access-global-notification.selectors';

describe('selectSharedDataAccessGlobalNotificationsView', () => {
  it('selects view', () => {
    const state = {
      nextErrorId: 0,
      globalNotificationsById: {},
      notificationList: [],
    };
    const result =
      selectSharedDataAccessGlobalNotificationsView.projector(state);
    expect(result).toEqual(state);
  });
});
