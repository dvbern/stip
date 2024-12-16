export type AppType = `${'gesuch' | 'sachbearbeitung'}-app`;
export type CompileTimeConfig = Pick<
  SharedModelCompileTimeConfig,
  'appType' | 'authClientId'
>;

export class SharedModelCompileTimeConfig {
  readonly authClientId: `stip-${AppType}`;
  readonly appType: AppType;

  isSachbearbeitungApp: boolean;
  isGesuchApp: boolean;

  constructor(config: CompileTimeConfig) {
    this.authClientId = config.authClientId;
    this.appType = config.appType;
    this.isSachbearbeitungApp = this.appType === 'sachbearbeitung-app';
    this.isGesuchApp = this.appType === 'gesuch-app';
  }
}

export const SHARED_MODEL_CONFIG_RESOURCE = `/config/deployment`;
