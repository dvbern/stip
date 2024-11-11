import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { EuEftaLaenderStore } from './sachbearbeitung-app-data-access-eu-efta-laender.store';

describe('EuEftaLaenderStore', () => {
  let store: EuEftaLaenderStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [EuEftaLaenderStore, provideHttpClient()],
    });
    store = TestBed.inject(EuEftaLaenderStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.euEftaLaender()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
});
