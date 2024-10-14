import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { NotificationStore } from './shared-data-access-notification.store';

describe('NotificationStore', () => {
  let store: NotificationStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [NotificationStore, provideHttpClient()],
    });
    store = TestBed.inject(NotificationStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.notifications()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
});
