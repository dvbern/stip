import { importProvidersFrom } from '@angular/core';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { TranslateModule } from '@ngx-translate/core';
import { provideOAuthClient } from 'angular-oauth2-oidc';
import { of } from 'rxjs';

import {
  CompileTimeConfig,
  SharedModelCompileTimeConfig,
} from '@dv/shared/model/config';
import {
  Ausbildungsstaette,
  AusbildungsstaetteService,
} from '@dv/shared/model/gesuch';
// eslint-disable-next-line @nx/enforce-module-boundaries
import { StoreUtilService } from '@dv/shared/util-data-access/store-util';

export function provideSharedPatternJestTestSetup(
  compileTimeConfig: CompileTimeConfig = {
    appType: 'gesuch-app',
    authClientId: 'stip-gesuch-app',
  },
) {
  return [
    provideOAuthClient(),
    importProvidersFrom([
      RouterTestingModule,
      TranslateModule.forRoot(),
      NoopAnimationsModule,
    ]),
    {
      provide: SharedModelCompileTimeConfig,
      useFactory: () => new SharedModelCompileTimeConfig(compileTimeConfig),
    },
    {
      provide: StoreUtilService,
      useValue: <{ [K in keyof StoreUtilService]: StoreUtilService[K] }>{
        waitForBenutzerData$: jest.fn(() => (s: unknown) => s) as unknown,
      },
    },
  ];
}

export type DeepPartial<T> = T extends object
  ? {
      [P in keyof T]?: DeepPartial<T[P]>;
    }
  : T;

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
