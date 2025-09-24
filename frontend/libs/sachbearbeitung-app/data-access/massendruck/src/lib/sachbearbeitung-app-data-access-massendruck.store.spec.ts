import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { MassendruckStore } from './sachbearbeitung-app-data-access-massendruck.store';

describe('MassendruckStore', () => {
  let store: MassendruckStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [MassendruckStore, provideHttpClient()],
    });
    store = TestBed.inject(MassendruckStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.paginatedMassendruckJobs()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
});
