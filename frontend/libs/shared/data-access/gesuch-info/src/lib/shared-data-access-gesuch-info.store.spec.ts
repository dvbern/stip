import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { GesuchInfoStore } from './shared-data-access-gesuch-info.store';

describe('GesuchInfoStore', () => {
  let store: GesuchInfoStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [GesuchInfoStore, provideHttpClient()],
    });
    store = TestBed.inject(GesuchInfoStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.gesuchInfo()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
});
