import { importProvidersFrom } from '@angular/core';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { provideTranslateService } from '@ngx-translate/core';
import { provideOAuthClient } from 'angular-oauth2-oidc';
import { of } from 'rxjs';

import {
  CompileTimeConfig,
  SharedModelCompileTimeConfig,
} from '@dv/shared/model/config';
import {
  Ausbildungsstaette,
  AusbildungsstaetteService,
  GesuchTranche,
  SharedModelGesuch,
  SharedModelGesuchFormular,
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
  formular?: DeepPartial<SharedModelGesuchFormular>;
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

export function provideSharedPatternJestTestAusbildungstaetten() {
  return {
    provide: AusbildungsstaetteService,
    useValue: {
      getAusbildungsstaetten$: () => {
        return of([
          {
            nameDe: 'staette1',
            nameFr: 'staette1',
            id: '1',
            ausbildungsgaenge: [
              {
                bildungskategorie: {
                  id: '',
                  bfs: -1,
                  bezeichnungDe: '',
                  bezeichnungFr: '',
                  bildungsstufe: 'SEKUNDAR_2',
                },
                bezeichnungDe: 'gang1',
                bezeichnungFr: 'gang1',
                ausbildungsstaetteId: '1',
                id: '1',
              },
            ],
          },
        ] satisfies Ausbildungsstaette[]);
      },
    },
  };
}
