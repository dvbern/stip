import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { FallHeaderStore } from './shared-data-access-fall-header.store';

describe('FallHeaderStore', () => {
  let store: FallHeaderStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [FallHeaderStore, provideHttpClient()],
    });
    store = TestBed.inject(FallHeaderStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.cachedFallHeader()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
});
