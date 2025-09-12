import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { DruckauftragStore } from './sachbearbeitung-app-data-access-druckauftrag.store';

describe('DruckauftragStore', () => {
  let store: DruckauftragStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DruckauftragStore, provideHttpClient()],
    });
    store = TestBed.inject(DruckauftragStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.cachedPaginatedDruckauftraege()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
});
