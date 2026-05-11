import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { BfsStatistikStore } from './sachbearbeitung-app-data-access-bfs-statistik.store';

describe('BfsStatistikStore', () => {
  let store: BfsStatistikStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [BfsStatistikStore, provideHttpClient()],
    });
    store = TestBed.inject(BfsStatistikStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.bfsStatistik()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
});
