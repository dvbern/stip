import { importProvidersFrom } from '@angular/core';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { TranslateModule } from '@ngx-translate/core';
import { provideOAuthClient } from 'angular-oauth2-oidc';

import {
  CompileTimeConfig,
  SharedModelCompileTimeConfig,
} from '@dv/shared/model/config';
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
