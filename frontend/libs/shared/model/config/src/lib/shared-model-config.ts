export type BusinessAppConfig =
  | {
      type: 'gesuch-app';
      view: 'gesuchsteller';
      keyPrefix: 'gesuch-app';
    }
  | {
      type: 'sozialdienst-app';
      view: 'gesuchsteller';
      keyPrefix: 'gesuch-app';
    }
  | {
      type: 'sachbearbeitung-app';
      view: 'sachbearbeiter';
      keyPrefix: 'sachbearbeitung-app';
    };

export type AppConfig =
  | BusinessAppConfig
  | {
      type: 'demo-data-app';
      view: 'demo';
      keyPrefix: 'demo-data-app';
    };

export type AppView = BusinessAppConfig['view'];

export function ensureIsBusinessAppConfig(
  appConfig: AppConfig,
): asserts appConfig is Exclude<AppConfig, { view: 'demo' }> {
  if (appConfig.view === 'demo') {
    throw new Error('Current app is not a business app');
  }
}

export class SharedModelCompileTimeConfig {
  readonly authClientId: `stip-${AppConfig['type']}`;
  readonly app: Readonly<AppConfig>;

  constructor(appConfig: AppConfig) {
    this.authClientId = `stip-${appConfig.type}`;
    this.app = appConfig;
  }
}

export const SHARED_MODEL_CONFIG_RESOURCE = `/config/deployment`;

export const tenantKeys = ['bern', 'dv'] as const;
export type TenantKey = (typeof tenantKeys)[number];
export const isTenantKey = (key: string): key is TenantKey => {
  return tenantKeys.includes(key as TenantKey);
};
