import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { BerechnungStore } from './shared-data-access-berechnung.store';

describe('BerechnungStore', () => {
  let store: BerechnungStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [BerechnungStore, provideHttpClient()],
    });
    store = TestBed.inject(BerechnungStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.berechnung()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
});
