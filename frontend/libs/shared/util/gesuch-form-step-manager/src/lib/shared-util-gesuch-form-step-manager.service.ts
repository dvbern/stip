import { Injectable } from '@angular/core';

import { SharedModelGesuchFormular } from '@dv/shared/model/gesuch';
import {
  GesuchFormStepView,
  RETURN_TO_HOME,
  SharedModelGesuchFormStep,
  SharedModelGesuchFormStepProgress,
  StepValidation,
  findStepIndex,
  isStepDisabled,
  isStepValid,
} from '@dv/shared/model/gesuch-form';

@Injectable({
  providedIn: 'root',
})
export class SharedUtilGesuchFormStepManagerService {
  /**
   * Returns the progress of the current step compared to the total steps
   */
  getStepProgress(
    steps: SharedModelGesuchFormStep[],
    step?: SharedModelGesuchFormStep,
  ): SharedModelGesuchFormStepProgress {
    if (!step) {
      return {
        step: undefined,
        total: steps.length,
        percentage: undefined,
      };
    }
    const stepIndex = findStepIndex(step, steps);
    return {
      step: stepIndex + 1,
      total: steps.length,
      percentage: ((stepIndex + 1) / steps.length) * 100,
    };
  }

  /**
   * Returns all steps for the current app type
   *
   * Adds valid and disabled properties to the steps depending on the formular state
   */
  getValidatedSteps(
    steps: SharedModelGesuchFormStep[],
    gesuchFormular: SharedModelGesuchFormular | null,
    invalidProps?: StepValidation,
    readonly = false,
  ): GesuchFormStepView[] {
    return steps.map((step, index) => ({
      ...step,
      nextStep: steps[index + 1],
      status: isStepValid(step, gesuchFormular, invalidProps),
      disabled: isStepDisabled(step, gesuchFormular, readonly),
    }));
  }

  /**
   * Returns the next step depending on the origin step
   */
  getNextStepOf(
    stepsFlow: SharedModelGesuchFormStep[],
    step: SharedModelGesuchFormStep,
    gesuchFormular: SharedModelGesuchFormular | null,
    readonly = false,
  ): SharedModelGesuchFormStep {
    const currentIndex = findStepIndex(step, stepsFlow);

    if (currentIndex === -1 || !stepsFlow[currentIndex + 1]) {
      return RETURN_TO_HOME;
    }

    let nextIndex = 0;

    for (let i = currentIndex + 1; i < stepsFlow.length; i++) {
      if (!isStepDisabled(stepsFlow[i], gesuchFormular, readonly)) {
        nextIndex = i;
        break;
      }
    }

    return stepsFlow[nextIndex];
  }

  /**
   * Compares two steps by their position in the flow
   */
  compareStepsByFlow(
    stepsFlow: SharedModelGesuchFormStep[],
    a: SharedModelGesuchFormStep,
    b: SharedModelGesuchFormStep,
    onEqual?: (
      a: SharedModelGesuchFormStep,
      b: SharedModelGesuchFormStep,
    ) => number,
  ) {
    const aIndex = findStepIndex(a, stepsFlow);
    const bIndex = findStepIndex(b, stepsFlow);
    if (aIndex === -1 || bIndex === -1) {
      return 1;
    }
    if (aIndex === bIndex) {
      return onEqual?.(a, b) ?? 0;
    }
    return aIndex - bIndex;
  }
}
