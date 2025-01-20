import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { DokumentsStore } from './shared-data-access-dokuments.store';

describe('DokumentsStore', () => {
  let store: DokumentsStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DokumentsStore, provideHttpClient()],
    });
    store = TestBed.inject(DokumentsStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.dokuments()).toEqual({
      data: undefined,
      error: undefined,
      type: 'initial',
    });
    expect(store.documentsToUpload()).toEqual({
      data: undefined,
      error: undefined,
      type: 'initial',
    });
  });
});
