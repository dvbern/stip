import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { NotizStore } from './sachbearbeitung-app-data-access-notiz.store';

describe('NotizStore', () => {
  let store: NotizStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [NotizStore, provideHttpClient()],
    });
    store = TestBed.inject(NotizStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.notizen()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
});
