import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { SozDashboardStore } from './sozialdienst-app-data-access-soz-dashboard.store';

describe('SozDashboardStore', () => {
  let store: SozDashboardStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SozDashboardStore, provideHttpClient()],
    });
    store = TestBed.inject(SozDashboardStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.sozDashboard()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
});
