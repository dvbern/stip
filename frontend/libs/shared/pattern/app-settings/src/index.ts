import { Provider } from '@angular/core';

import { AppType } from '@dv/shared/model/config';

import {
  AppSettings,
  AppSettingsGesuchApp,
  AppSettingsSachbearbeitungApp,
} from './lib/app-settings/app-settings';
export { AppSettings } from './lib/app-settings/app-settings';

export function provideSharedAppSettings(type: AppType): Provider[] {
  return [
    {
      provide: AppSettings,
      useClass:
        type === 'gesuch-app'
          ? AppSettingsGesuchApp
          : AppSettingsSachbearbeitungApp,
    },
  ];
}
