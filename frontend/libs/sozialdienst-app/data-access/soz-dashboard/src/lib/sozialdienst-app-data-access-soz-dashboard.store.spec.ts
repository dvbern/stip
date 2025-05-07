import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { provideMockStore } from '@ngrx/store/testing';

// eslint-disable-next-line @nx/enforce-module-boundaries
import { provideSharedPatternJestTestSetup } from '@dv/shared/pattern/jest-test-setup';

import { SozDashboardStore } from './sozialdienst-app-data-access-soz-dashboard.store';

describe('SozDashboardStore', () => {
  let store: SozDashboardStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideMockStore(),
        SozDashboardStore,
        provideHttpClient(),
        provideSharedPatternJestTestSetup(),
      ],
    });
    store = TestBed.inject(SozDashboardStore);
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
