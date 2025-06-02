import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { InfosAdminStore } from './sachbearbeitung-app-data-access-infos-admin.store';

describe('InfosAdminStore', () => {
  let store: InfosAdminStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [InfosAdminStore, provideHttpClient()],
    });
    store = TestBed.inject(InfosAdminStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.verfuegungen()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
});
