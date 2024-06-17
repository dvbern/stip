import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

// eslint-disable-next-line @nx/enforce-module-boundaries
import { provideSharedPatternJestTestSetup } from '@dv/shared/pattern/jest-test-setup';
import { initial } from '@dv/shared/util/remote-data';

import { FallStore } from './shared-data-access-fall.store';

describe('FallStore', () => {
  let store: FallStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        FallStore,
        provideHttpClient(),
        provideSharedPatternJestTestSetup(),
      ],
    });
    store = TestBed.inject(FallStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.cachedCurrentFall()).toEqual(initial());
  });
});
