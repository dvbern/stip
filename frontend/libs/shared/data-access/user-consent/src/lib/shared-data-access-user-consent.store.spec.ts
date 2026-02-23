import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { UserConsentStore } from './shared-data-access-user-consent.store';

describe('UserConsentStore', () => {
  let store: UserConsentStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [UserConsentStore, provideHttpClient()],
    });
    store = TestBed.inject(UserConsentStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.userConsent()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
});
