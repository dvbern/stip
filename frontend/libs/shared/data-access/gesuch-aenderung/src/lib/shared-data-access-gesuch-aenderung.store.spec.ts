import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { GesuchAenderungStore } from './shared-data-access-gesuch-aenderung.store';

describe('GesuchAenderungStore', () => {
  let store: GesuchAenderungStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [GesuchAenderungStore, provideHttpClient()],
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
  });
});