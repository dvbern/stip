import { importProvidersFrom } from '@angular/core';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { TranslateModule } from '@ngx-translate/core';
import { provideOAuthClient } from 'angular-oauth2-oidc';

import {
  CompiletimeConfig,
  SharedModelCompiletimeConfig,
} from '@dv/shared/model/config';

export function provideSharedPatternJestTestSetup(
  compileTimeConfig: CompiletimeConfig = {
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
      provide: SharedModelCompiletimeConfig,
      useFactory: () => new SharedModelCompiletimeConfig(compileTimeConfig),
    },
  ];
}
