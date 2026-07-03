import { Provider } from '@angular/core';

import { AppConfig } from '@dv/shared/model/config';

import {
  AppSettings,
  AppSettingsGesuchApp,
  AppSettingsSachbearbeitungApp,
} from './lib/app-settings/app-settings';
export { AppSettings } from './lib/app-settings/app-settings';

export function provideSharedAppSettings(config: AppConfig): Provider[] {
  return [
    {
      provide: AppSettings,
      useClass:
        config.view === 'sachbearbeiter'
          ? AppSettingsSachbearbeitungApp
          : AppSettingsGesuchApp,
    },
  ];
}
