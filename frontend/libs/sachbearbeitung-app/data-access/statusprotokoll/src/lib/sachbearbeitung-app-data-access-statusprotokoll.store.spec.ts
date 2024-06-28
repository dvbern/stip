import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { StatusprotokollStore } from './sachbearbeitung-app-data-access-statusprotokoll.store';

describe('StatusprotokollStore', () => {
  let store: StatusprotokollStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [StatusprotokollStore, provideHttpClient()],
    });
    store = TestBed.inject(StatusprotokollStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.statusprotokoll()).toEqual({
      data: null,
      loading: false,
      error: null,
    });
  });
});
