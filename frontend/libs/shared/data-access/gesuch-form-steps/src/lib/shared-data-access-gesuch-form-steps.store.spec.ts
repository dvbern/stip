import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { provideMockStore } from '@ngrx/store/testing';

// eslint-disable-next-line @nx/enforce-module-boundaries
import { provideSharedPatternJestTestSetup } from '@dv/shared/pattern/jest-test-setup';

import { GesuchFormStepsStore } from './shared-data-access-gesuch-form-steps.store';

describe('GesuchFormStepsStore', () => {
  let store: GesuchFormStepsStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        GesuchFormStepsStore,
        provideHttpClient(),
        provideMockStore(),
        provideSharedPatternJestTestSetup(),
      ],
    });
    store = TestBed.inject(GesuchFormStepsStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.baseSteps().length).toBeGreaterThan(0);
  });
});
