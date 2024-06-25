import { SharedModelGesuchFormularProps } from '@dv/shared/model/gesuch';

export type StepValidation = {
  errors?: SharedModelGesuchFormularProps[];
  warnings?: SharedModelGesuchFormularProps[];
  hasDocuments: boolean | null;
};

export type StepState = 'VALID' | 'INVALID' | 'WARNING';

export interface SharedModelGesuchFormStep {
  route: string;
  translationKey: string;
  iconSymbolName: string;
}

export type SharedModelGesuchFormStepProgress = {
  step?: number;
  total: number;
  percentage?: number;
};

export interface GesuchFormStepView extends SharedModelGesuchFormStep {
  nextStep?: SharedModelGesuchFormStep;
  status?: StepState;
  disabled: boolean;
}
