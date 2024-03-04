import { Injectable, inject } from '@angular/core';

import { AppType, SharedModelCompiletimeConfig } from '@dv/shared/model/config';
import { GesuchFormularUpdate } from '@dv/shared/model/gesuch';
import {
  ABSCHLUSS,
  AUSBILDUNG,
  AUSZAHLUNGEN,
  DOKUMENTE,
  EINNAHMEN_KOSTEN,
  ELTERN,
  FAMILIENSITUATION,
  GESCHWISTER,
  KINDER,
  LEBENSLAUF,
  PARTNER,
  PERSON,
  SharedModelGesuchFormStep,
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
  AUSZAHLUNGEN,
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
  getAllSteps(gesuchFormular: GesuchFormularUpdate | null) {
    const steps: Record<AppType, SharedModelGesuchFormStep[]> = {
      'sachbearbeitung-app': BaseSteps,
      'gesuch-app': [...BaseSteps, ABSCHLUSS],
    };
    return steps[this.compiletimeConfig.appType].map((step) => ({
      ...step,
      valid: isStepValid(step, gesuchFormular),
      disabled: isStepDisabled(step, gesuchFormular),
    }));
  }
  getTotalSteps(gesuchFormular: GesuchFormularUpdate | null): number {
    return this.getAllSteps(gesuchFormular).length;
  }
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
