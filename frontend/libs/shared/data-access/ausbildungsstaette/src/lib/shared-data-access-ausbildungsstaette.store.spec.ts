import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { AusbildungsstaetteStore } from './shared-data-access-ausbildungsstaette.store';

describe('AusbildungStore', () => {
  let store: AusbildungsstaetteStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AusbildungsstaetteStore, provideHttpClient()],
    });
    store = TestBed.inject(AusbildungsstaetteStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.ausbildungsstaetten()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
});
