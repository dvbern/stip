import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { SteuerdatenStore } from './shared-data-access-steuerdaten.store';

describe('SteuerdatenStore', () => {
  let store: SteuerdatenStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SteuerdatenStore, provideHttpClient()],
    });
    store = TestBed.inject(SteuerdatenStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.cachedSteuerdaten()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
});
