import { TestBed } from '@angular/core/testing';

import { SharedModelError } from '@dv/shared/model/error';
import { SharedModelGlobalNotification } from '@dv/shared/model/global-notification';

import { GlobalNotificationStore } from './shared-data-access-global-notifications.store';

describe('GlobalNotificationStore', () => {
  let store: GlobalNotificationStore;
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [GlobalNotificationStore],
    });
    store = TestBed.inject(GlobalNotificationStore);
  });

  it('should add a new notification with message', () => {
    store.createSuccessNotification({
      message: 'message',
    });

    expect(store.notifications()).toEqual([
      {
        id: 1,
        type: 'SUCCESS',
        message: 'message',
      },
    ]);
  });

  it('should add a new notification with messageKey', () => {
    store.createSuccessNotification({
      messageKey: 'messageKey',
    });

    expect(store.notifications()).toEqual([
      {
        id: 1,
        type: 'SUCCESS',
        messageKey: 'messageKey',
      },
    ]);
  });

  it('should handle httpRequestFailed', () => {
    const error = {
      validationErrors: [
        {
          message: 'message',
          messsageTemplate: 'template',
          propertyPath: 'field',
        },
      ],
    };
    store.handleHttpRequestFailed([
      SharedModelError.parse({
        error,
      }),
    ]);
    expect(store.mostImportantNotificationSig()).toEqual(<
      SharedModelGlobalNotification
    >{
      autohide: true,
      list: [
        {
          content: {
            error: {
              error: {
                validationErrors: [
                  {
                    message: 'message',
                    messsageTemplate: 'template',
                    propertyPath: 'field',
                  },
                ],
              },
            },
            messageKey: 'shared.genericError.general',
            type: 'unknownError',
          },
          id: 1,
          type: 'ERROR',
        },
      ],
      type: 'ERROR',
    });
  });
});
