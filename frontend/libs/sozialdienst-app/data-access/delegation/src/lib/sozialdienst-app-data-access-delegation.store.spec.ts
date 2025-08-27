import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { provideMockStore } from '@ngrx/store/testing';

import { PermissionStore } from '@dv/shared/global/permission';
// eslint-disable-next-line @nx/enforce-module-boundaries
import { provideSharedPatternJestTestSetup } from '@dv/shared/pattern/jest-test-setup';

import { DelegationStore } from './sozialdienst-app-data-access-delegation.store';

describe('DelegationStore', () => {
  let store: DelegationStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideMockStore(),
        DelegationStore,
        PermissionStore,
        provideHttpClient(),
        provideSharedPatternJestTestSetup(),
      ],
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
