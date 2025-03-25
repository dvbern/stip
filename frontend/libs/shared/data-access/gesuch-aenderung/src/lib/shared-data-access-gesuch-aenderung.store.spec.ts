import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { provideMockStore } from '@ngrx/store/testing';

// eslint-disable-next-line @nx/enforce-module-boundaries
import { provideSharedPatternJestTestSetup } from '@dv/shared/pattern/jest-test-setup';

import { GesuchAenderungStore } from './shared-data-access-gesuch-aenderung.store';

describe('GesuchAenderungStore', () => {
  let store: GesuchAenderungStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideSharedPatternJestTestSetup(),
        GesuchAenderungStore,
        provideHttpClient(),
        provideMockStore(),
      ],
    });
    store = TestBed.inject(GesuchAenderungStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.cachedGesuchAenderung()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
    expect(store.cachedTranchenList()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
});
