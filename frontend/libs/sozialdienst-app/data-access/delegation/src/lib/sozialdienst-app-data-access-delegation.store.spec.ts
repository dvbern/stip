import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { DelegationStore } from './sozialdienst-app-data-access-delegation.store';

describe('DelegationStore', () => {
  let store: DelegationStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DelegationStore, provideHttpClient()],
    });
    store = TestBed.inject(DelegationStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.paginatedSozDashboard()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
});
