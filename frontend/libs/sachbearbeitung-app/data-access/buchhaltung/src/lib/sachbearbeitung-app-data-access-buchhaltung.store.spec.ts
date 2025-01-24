import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { BuchhaltungStore } from './sachbearbeitung-app-data-access-buchhaltung.store';

describe('BuchhaltungStore', () => {
  let store: BuchhaltungStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [BuchhaltungStore, provideHttpClient()],
    });
    store = TestBed.inject(BuchhaltungStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.buchhaltung()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
});
