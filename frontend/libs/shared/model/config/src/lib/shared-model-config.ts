export type AppType = `${'gesuch' | 'sachbearbeitung'}-app`;
export type CompiletimeConfig = Pick<
  SharedModelCompiletimeConfig,
  'appType' | 'authClientId'
>;

export class SharedModelCompiletimeConfig {
  readonly authClientId: `stip-${AppType}`;
  readonly appType: AppType;

  isSachbearbeitungApp: boolean;
  isGesuchApp: boolean;

  constructor(config: CompiletimeConfig) {
    this.authClientId = config.authClientId;
    this.appType = config.appType;
    this.isSachbearbeitungApp = this.appType === 'sachbearbeitung-app';
    this.isGesuchApp = this.appType === 'gesuch-app';
  }
}

// TODO extract to env or generate with OpenAPI?
export const SHARED_MODEL_CONFIG_RESOURCE = `/config/deployment`;
