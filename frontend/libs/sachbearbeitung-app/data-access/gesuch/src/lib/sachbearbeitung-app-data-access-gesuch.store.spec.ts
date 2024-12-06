import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { provideMockStore } from '@ngrx/store/testing';

import { GesuchStore } from './sachbearbeitung-app-data-access-gesuch.store';

describe('GesuchStore', () => {
  let store: GesuchStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        GesuchStore,
        provideHttpClient(),
        provideMockStore({
          initialState: {},
        }),
      ],
    });
    store = TestBed.inject(GesuchStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.gesuche()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
});
