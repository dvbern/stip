import { GSFormStepProps, SBFormStepProps } from '@dv/shared/model/gesuch';

export type StepValidation = {
  errors?: (GSFormStepProps | SBFormStepProps)[];
  warnings?: (GSFormStepProps | SBFormStepProps)[];
  hasDocuments: boolean | null;
};

export type StepState = 'VALID' | 'INVALID' | 'WARNING';

export interface GesuchFormStep {
  route: string;
  routes?: string[];
  translationKey: string;
  titleTranslationKey: string;
  iconSymbolName: string;
}

export type GesuchFormStepProgress = {
  step?: number;
  total: number;
  percentage?: number;
};

export interface GesuchFormStepView extends GesuchFormStep {
  nextStep?: GesuchFormStep;
  status?: StepState;
  disabled: boolean;
}

export type FormularChangeTypes = string | number | boolean | undefined | null;
