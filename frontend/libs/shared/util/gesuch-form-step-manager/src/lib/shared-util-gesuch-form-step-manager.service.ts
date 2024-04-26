import { Injectable, inject } from '@angular/core';

import { AppType, SharedModelCompiletimeConfig } from '@dv/shared/model/config';
import { GesuchFormularUpdate } from '@dv/shared/model/gesuch';
import {
  ABSCHLUSS,
  AUSBILDUNG,
  AUSZAHLUNG,
  DOKUMENTE,
  EINNAHMEN_KOSTEN,
  ELTERN,
  FAMILIENSITUATION,
  GESCHWISTER,
  GesuchFormStepView,
  KINDER,
  LEBENSLAUF,
  PARTNER,
  PERSON,
  SharedModelGesuchFormStep,
  StepValidation,
  isStepDisabled,
  isStepValid,
} from '@dv/shared/model/gesuch-form';
import { isDefined } from '@dv/shared/util-fn/type-guards';

const RETURN_TO_COCKPIT: SharedModelGesuchFormStep = {
  route: '/',
  translationKey: '',
  currentStepNumber: Number.MAX_SAFE_INTEGER,
  iconSymbolName: '',
};

const BaseSteps = [
  PERSON,
  AUSBILDUNG,
  LEBENSLAUF,
  FAMILIENSITUATION,
  ELTERN,
  GESCHWISTER,
  PARTNER,
  KINDER,
  AUSZAHLUNG,
  EINNAHMEN_KOSTEN,
  DOKUMENTE,
];

const StepFlow: Record<AppType, SharedModelGesuchFormStep[]> = {
  'gesuch-app': [...BaseSteps, ABSCHLUSS, RETURN_TO_COCKPIT],
  'sachbearbeitung-app': [...BaseSteps, RETURN_TO_COCKPIT],
};

@Injectable({
  providedIn: 'root',
})
export class SharedUtilGesuchFormStepManagerService {
  private compiletimeConfig = inject(SharedModelCompiletimeConfig);
  /**
   * Returns all steps for the current app type
   *
   * Adds valid and disabled properties to the steps depending on the formular state
   */
  getAllSteps(
    gesuchFormular: GesuchFormularUpdate | null,
    invalidProps?: StepValidation,
  ): GesuchFormStepView[] {
    const steps: Record<AppType, SharedModelGesuchFormStep[]> = {
      'sachbearbeitung-app': BaseSteps,
      'gesuch-app': [...BaseSteps, ABSCHLUSS],
    };
    return steps[this.compiletimeConfig.appType].map((step) => ({
      ...step,
      status: isStepValid(step, gesuchFormular, invalidProps),
      disabled: isStepDisabled(step, gesuchFormular),
    }));
  }

  /**
   * Returns the total number of steps
   */
  getTotalSteps(gesuchFormular: GesuchFormularUpdate | null): number {
    return this.getAllSteps(gesuchFormular).length;
  }

  /**
   * Returns the next step depending on the origin step
   */
  getNext(origin?: SharedModelGesuchFormStep): SharedModelGesuchFormStep {
    const steps = [...StepFlow[this.compiletimeConfig.appType]].sort(
      (s1, s2) => s1.currentStepNumber - s2.currentStepNumber,
    );
    const currentIndex = steps.findIndex(
      (step) => step.currentStepNumber === origin?.currentStepNumber,
    );
    if (currentIndex === -1 || !isDefined(steps[currentIndex + 1])) {
      throw new Error('Step not defined');
    }
    return steps[currentIndex + 1];
  }
}
