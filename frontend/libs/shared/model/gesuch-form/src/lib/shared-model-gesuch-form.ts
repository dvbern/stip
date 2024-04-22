import { SharedModelGesuchFormularProps } from '@dv/shared/model/gesuch';

export type StepValidation = {
  errors?: SharedModelGesuchFormularProps[];
  warnings?: SharedModelGesuchFormularProps[];
};

export type StepState = 'VALID' | 'INVALID' | 'WARNING';

export interface SharedModelGesuchFormStep {
  route: string;
  translationKey: string;
  currentStepNumber: number;
  iconSymbolName: string;
}

export interface GesuchFormStepView extends SharedModelGesuchFormStep {
  status?: StepState;
  disabled: boolean;
}
