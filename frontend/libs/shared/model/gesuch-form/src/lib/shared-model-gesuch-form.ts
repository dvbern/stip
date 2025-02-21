import { SharedModelGesuchFormularPropsSteuerdatenSteps } from '@dv/shared/model/gesuch';

export type StepValidation = {
  errors?: (
    | SharedModelGesuchFormularPropsSteuerdatenSteps
    | 'steuererklaerung'
  )[];
  warnings?: (
    | SharedModelGesuchFormularPropsSteuerdatenSteps
    | 'steuererklaerung'
  )[];
  hasDocuments: boolean | null;
};

export type StepState = 'VALID' | 'INVALID' | 'WARNING';

export interface SharedModelGesuchFormStep {
  route: string;
  translationKey: string;
  titleTranslationKey: string;
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

export type FormularChangeTypes = string | number | boolean | undefined | null;
