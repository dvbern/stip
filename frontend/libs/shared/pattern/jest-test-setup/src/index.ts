import { importProvidersFrom } from '@angular/core';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { provideTranslateService } from '@ngx-translate/core';
import { OAuthService, provideOAuthClient } from 'angular-oauth2-oidc';
import { of } from 'rxjs';

import {
  CompileTimeConfig,
  SharedModelCompileTimeConfig,
} from '@dv/shared/model/config';
import {
  AusbildungsstaetteService,
  AusbildungsstaetteSlim,
  GesuchFormularType,
  GesuchTranche,
  SharedModelGesuch,
} from '@dv/shared/model/gesuch';
// eslint-disable-next-line @nx/enforce-module-boundaries
import { StoreUtilService } from '@dv/shared/util-data-access/store-util';

export type DeepPartial<T> = T extends object
  ? {
      [P in keyof T]?: DeepPartial<T[P]>;
    }
  : T;

export const mockedGesuchAppWritableGesuchState = (overrides?: {
  gesuch?: DeepPartial<SharedModelGesuch>;
  tranche?: DeepPartial<GesuchTranche>;
  formular?: DeepPartial<GesuchFormularType>;
}) => {
  const gesuch = {
    id: '123',
    ...(overrides?.gesuch ?? {}),
    gesuchTrancheToWorkWith: {
      ...(overrides?.tranche ?? {}),
      gesuchFormular: {
        ...(overrides?.formular ?? {}),
      },
      status: 'IN_BEARBEITUNG_GS',
    },
    gesuchStatus: 'IN_BEARBEITUNG_GS',
  } as SharedModelGesuch;
  return {
    gesuch,
    gesuchFormular: gesuch.gesuchTrancheToWorkWith.gesuchFormular,
    cache: {
      gesuch,
      gesuchId: '123',
      gesuchFormular: gesuch.gesuchTrancheToWorkWith.gesuchFormular,
    },
  };
};

const defaultCompileTimeConfig: CompileTimeConfig = {
  appType: 'gesuch-app',
  authClientId: 'stip-gesuch-app',
};

export const provideCompileTimeConfig = (
  compileTimeConfig: CompileTimeConfig = defaultCompileTimeConfig,
) => ({
  provide: SharedModelCompileTimeConfig,
  useFactory: () => new SharedModelCompileTimeConfig(compileTimeConfig),
});

export const mockConfigsState = (
  compileTimeConfig: CompileTimeConfig = defaultCompileTimeConfig,
) => ({ loading: false, error: undefined, compileTimeConfig });

export function provideSharedPatternJestTestSetup(
  compileTimeConfig: CompileTimeConfig = defaultCompileTimeConfig,
) {
  return [
    provideOAuthClient(),
    provideCompileTimeConfig(compileTimeConfig),
    provideTranslateService(),
    importProvidersFrom([RouterTestingModule, NoopAnimationsModule]),
    {
      provide: StoreUtilService,
      useValue: <{ [K in keyof StoreUtilService]: StoreUtilService[K] }>{
        waitForBenutzerData$: jest.fn(() => (s: unknown) => s) as unknown,
      },
    },
  ];
}

export function provideSharedOAuthServiceWithGesuchstellerJWT() {
  return {
    provide: OAuthService,
    useValue: {
      getAccessToken: () =>
        /**
         * {
         *   ...
         *   "name": "John Doe",
         *   "realm_access": {
         *     "roles": [
         *       "Gesuchsteller"
         *     ]
         *   }
         * }
         */
        'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiR2VzdWNoc3RlbGxlciJdfX0.pW71nQV6d_VLc0a8R-4WxVkXOmega_z2RFZo7nTyYJI',
    },
  };
}

export function provideSharedPatternJestTestAusbildungstaetten() {
  return {
    provide: AusbildungsstaetteService,
    useValue: {
      getAllAusbildungsstaetteForAuswahl$: () => {
        return of([
          {
            nameDe: 'staette1',
            nameFr: 'staette1',
            id: '1',
            ausbildungsgaenge: [
              {
                bezeichnungDe: 'gang1',
                bezeichnungFr: 'gang1',
                id: '1',
                bildungskategorie: 'SEKUNDARSTUFE_I',
                zusatzfrage: 'FACHRICHTUNG',
              },
            ],
          },
        ] satisfies AusbildungsstaetteSlim[]);
      },
    },
  };
}
