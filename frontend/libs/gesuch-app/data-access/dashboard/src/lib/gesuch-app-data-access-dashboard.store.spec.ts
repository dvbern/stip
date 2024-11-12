import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { DashboardStore } from './gesuch-app-data-access-dashboard.store';

describe('DashboardStore', () => {
  let store: DashboardStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DashboardStore, provideHttpClient()],
    });
    store = TestBed.inject(DashboardStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.dashboard()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
});
