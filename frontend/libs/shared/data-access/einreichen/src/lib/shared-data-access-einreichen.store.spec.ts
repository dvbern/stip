import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { patchState } from '@ngrx/signals';
import { provideMockStore } from '@ngrx/store/testing';

import { State as GesuchState } from '@dv/shared/data-access/gesuch';
// eslint-disable-next-line @nx/enforce-module-boundaries
import {
  DeepPartial,
  provideSharedPatternJestTestSetup,
} from '@dv/shared/pattern/jest-test-setup';
import { success } from '@dv/shared/util/remote-data';

import { EinreichenStore } from './shared-data-access-einreichen.store';

describe('EinreichenStore', () => {
  it('should select correct invalidFormularProps', () => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideSharedPatternJestTestSetup(),
        provideMockStore({
          initialState: {
            gesuchs: {
              cache: {
                gesuch: {
                  gesuchTrancheToWorkWith: {
                    gesuchFormular: {
                      personInAusbildung: {} as any,
                      partner: {} as any,
                      kinds: [],
                    },
                  },
                },
              },
            } satisfies DeepPartial<GesuchState>,
            globalNotifications: { globalNotificationsById: {} },
            configs: {},
          },
        }),
      ],
    });

    const einreichenStore = TestBed.inject(EinreichenStore);
    patchState(einreichenStore, {
      validationResult: success({
        validationErrors: [
          { message: '', messageTemplate: '', propertyPath: 'partner' },
          { message: '', messageTemplate: '', propertyPath: 'kinds' },
          { message: '', messageTemplate: '', propertyPath: 'invalid' },
        ],
        validationWarnings: [],
        hasDocuments: undefined,
      }),
    });
    expect(
      einreichenStore.validationViewSig().invalidFormularProps.validations,
    ).toEqual({
      errors: ['partner', 'kinds'],
      warnings: [],
      hasDocuments: null,
    });
  });
});
