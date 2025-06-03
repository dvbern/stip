import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { LandStore } from './shared-data-access-land.store';

describe('LandStore', () => {
  let store: LandStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [LandStore, provideHttpClient()],
    });
    store = TestBed.inject(LandStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.laender()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
});
