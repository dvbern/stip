import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { FallDokumenteStore } from './shared-data-access-fall-dokumente.store';

describe('FallDokumenteStore', () => {
  let store: FallDokumenteStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [FallDokumenteStore, provideHttpClient()],
    });
    store = TestBed.inject(FallDokumenteStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has initial remote data states', () => {
    expect(store.verfuegungen()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });

    expect(store.darlehenBuchhaltung()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
});
