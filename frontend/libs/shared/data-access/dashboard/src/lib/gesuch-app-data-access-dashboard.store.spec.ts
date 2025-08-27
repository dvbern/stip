import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { provideMockStore } from '@ngrx/store/testing';

// eslint-disable-next-line @nx/enforce-module-boundaries
import { provideSharedPatternVitestTestSetup } from '@dv/shared/pattern/vitest-test-setup';

import { DashboardStore } from './gesuch-app-data-access-dashboard.store';

describe('DashboardStore', () => {
  let store: DashboardStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideMockStore(),
        DashboardStore,
        provideHttpClient(),
        provideSharedPatternVitestTestSetup(),
      ],
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
