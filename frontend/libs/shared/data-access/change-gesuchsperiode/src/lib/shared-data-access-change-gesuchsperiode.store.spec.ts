import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { GesuchService, GesuchsperiodeService } from '@dv/shared/model/gesuch';
// eslint-disable-next-line @nx/enforce-module-boundaries
import { provideSharedPatternJestTestSetup } from '@dv/shared/pattern/jest-test-setup';

import { ChangeGesuchsperiodeStore } from './shared-data-access-change-gesuchsperiode.store';

describe('ChangeGesuchsperiodeStore', () => {
  let store: ChangeGesuchsperiodeStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        ChangeGesuchsperiodeStore,
        {
          provide: GesuchsperiodeService,
          useValue: {
            getAllAssignableGesuchsperiode$: jest.fn(),
          },
        },
        {
          provide: GesuchService,
          useValue: {
            setGesuchsperiodeForGesuch$: jest.fn(),
          },
        },
        provideHttpClient(),
        provideSharedPatternJestTestSetup(),
        provideHttpClient(),
      ],
    });
    store = TestBed.inject(ChangeGesuchsperiodeStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.changeGesuchsperiode()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
});
