import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { BeschwerdeStore } from './sachbearbeitung-app-data-access-beschwerde.store';

describe('BeschwerdeStore', () => {
  let store: BeschwerdeStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [BeschwerdeStore, provideHttpClient()],
    });
    store = TestBed.inject(BeschwerdeStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.beschwerde()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
});
