import { Injectable, inject } from '@angular/core';

import { RolesMap } from '@dv/shared/model/benutzer';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import {
  GesuchUrlType,
  SharedModelGesuch,
  Steuerdaten,
} from '@dv/shared/model/gesuch';
import {
  GesuchFormStep,
  GesuchFormStepProgress,
  GesuchFormStepView,
  RETURN_TO_HOME,
  StepValidation,
  findStepIndex,
  isStepDisabled,
  isStepValid,
} from '@dv/shared/model/gesuch-form';
import { preparePermissions } from '@dv/shared/model/permission-state';

@Injectable({
  providedIn: 'root',
})
export class SharedUtilGesuchFormStepManagerService {
  private appType = inject(SharedModelCompileTimeConfig).appType;

  /**
   * Returns the progress of the current step compared to the total steps
   */
  getStepProgress(
    steps: GesuchFormStep[],
    step?: GesuchFormStep,
  ): GesuchFormStepProgress {
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
    steps: GesuchFormStep[],
    trancheTyp: GesuchUrlType | null,
    gesuch: SharedModelGesuch | null,
    rolesMap: RolesMap,
    steuerdaten?: Steuerdaten[],
    invalidProps?: StepValidation,
  ): GesuchFormStepView[] {
    const gesuchFormular =
      gesuch?.gesuchTrancheToWorkWith.gesuchFormular ?? null;
    const { permissions } = preparePermissions(
      trancheTyp,
      gesuch,
      this.appType,
      rolesMap,
    );
    return steps.map((step, index) => ({
      ...step,
      nextStep: steps[index + 1],
      status: isStepValid(
        step,
        gesuchFormular,
        this.appType,
        steuerdaten,
        invalidProps,
      ),
      disabled: isStepDisabled(step, gesuch, permissions),
    }));
  }

  /**
   * Returns the next step depending on the origin step
   */
  getNextStepOf(
    stepsFlow: GesuchFormStep[],
    trancheTyp: GesuchUrlType | null,
    step: GesuchFormStep,
    gesuch: SharedModelGesuch,
    rolesMap: RolesMap,
  ): GesuchFormStep {
    const currentIndex = findStepIndex(step, stepsFlow);

    if (currentIndex === -1 || !stepsFlow[currentIndex + 1]) {
      return RETURN_TO_HOME;
    }

    let nextIndex = 0;
    const { permissions } = preparePermissions(
      trancheTyp,
      gesuch,
      this.appType,
      rolesMap,
    );

    for (let i = currentIndex + 1; i < stepsFlow.length; i++) {
      if (!isStepDisabled(stepsFlow[i], gesuch, permissions)) {
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
    stepsFlow: GesuchFormStep[],
    a: GesuchFormStep,
    b: GesuchFormStep,
    onEqual?: (a: GesuchFormStep, b: GesuchFormStep) => number,
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
