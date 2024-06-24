import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { GesuchFormStepsStore } from './shared-data-access-gesuch-form-steps.store';

describe('GesuchFormStepsStore', () => {
  let store: GesuchFormStepsStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [GesuchFormStepsStore, provideHttpClient()],
    });
    store = TestBed.inject(GesuchFormStepsStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.gesuchFormSteps()).toEqual({
      data: null,
      loading: false,
      error: null,
    });
  });
});
