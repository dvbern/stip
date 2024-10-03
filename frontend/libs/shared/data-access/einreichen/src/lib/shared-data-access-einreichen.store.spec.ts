import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { EinreichenStore } from './shared-data-access-einreichen.store';

describe('EinreichenStore', () => {
  let store: EinreichenStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [EinreichenStore, provideHttpClient()],
    });
    store = TestBed.inject(EinreichenStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.einreichenViewSig()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
});
