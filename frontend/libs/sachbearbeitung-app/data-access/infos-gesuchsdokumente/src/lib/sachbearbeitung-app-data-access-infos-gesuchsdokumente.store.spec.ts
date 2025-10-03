import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { InfosGesuchsdokumenteStore } from './sachbearbeitung-app-data-access-infos-gesuchsdokumente.store';

describe('InfosGesuchsdokumenteStore', () => {
  let store: InfosGesuchsdokumenteStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [InfosGesuchsdokumenteStore, provideHttpClient()],
    });
    store = TestBed.inject(InfosGesuchsdokumenteStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.adminDokumente()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
});
