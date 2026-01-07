import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { DarlehenStore } from './shared-data-access-darlehen.store';

describe('DarlehenStore', () => {
  let store: DarlehenStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DarlehenStore, provideHttpClient()],
    });
    store = TestBed.inject(DarlehenStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.cachedDarlehen()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
});
