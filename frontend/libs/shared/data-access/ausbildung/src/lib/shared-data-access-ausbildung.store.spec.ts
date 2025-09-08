import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

// eslint-disable-next-line @nx/enforce-module-boundaries
import { provideSharedPatternVitestTestSetup } from '@dv/shared/pattern/vitest-test-setup';

import { AusbildungStore } from './shared-data-access-ausbildung.store';

describe('AusbildungStore', () => {
  let store: AusbildungStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        AusbildungStore,
        provideHttpClient(),
        provideSharedPatternVitestTestSetup(),
      ],
    });
    store = TestBed.inject(AusbildungStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.ausbildung()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
});
