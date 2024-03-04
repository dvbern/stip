import { Injectable } from '@angular/core';

type FormFeatures = {
  digitaleKommunikation: boolean;
};

export abstract class AppSettings {
  protected formFeatures: FormFeatures = {
    digitaleKommunikation: false,
  };

  protected setFormFeatures(features: Partial<FormFeatures>): void {
    this.formFeatures = {
      ...this.formFeatures,
      ...features,
    };
  }

  public hasFormFeature(feature: keyof FormFeatures): boolean {
    return this.formFeatures[feature];
  }
}

@Injectable()
export class AppSettingsGesuchApp extends AppSettings {}

@Injectable()
export class AppSettingsSachbearbeitungApp extends AppSettings {
  constructor() {
    super();
    this.setFormFeatures({
      digitaleKommunikation: true,
    });
  }
}
