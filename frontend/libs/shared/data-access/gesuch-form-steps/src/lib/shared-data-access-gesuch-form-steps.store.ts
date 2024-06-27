import { Injectable, Signal, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { signalStore, withState } from '@ngrx/signals';
import { Store } from '@ngrx/store';

import { selectSharedDataAccessGesuchSteuerdatenView } from '@dv/shared/data-access/gesuch';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import { SharedModelGesuchFormular } from '@dv/shared/model/gesuch';
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
  RETURN_TO_HOME,
  SharedModelGesuchFormStep,
  SharedModelGesuchFormStepProgress,
  StepValidation,
  createElternSteuerStep,
  isStepDisabled,
  isStepValid,
} from '@dv/shared/model/gesuch-form';

type GesuchFormStepsState = {
  baseSteps: SharedModelGesuchFormStep[];
};

const initialState: GesuchFormStepsState = {
  baseSteps: [
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
  ],
};

@Injectable()
export class GesuchFormStepsStore extends signalStore(
  withState(initialState),
  withDevtools('GesuchFormStepsStore'),
) {
  private compileTimeConfig = inject(SharedModelCompileTimeConfig);
  private steuerdatenViewSig = inject(Store).selectSignal(
    selectSharedDataAccessGesuchSteuerdatenView,
  );

  stepsSig = computed(() => {
    const steuerdaten = this.steuerdatenViewSig();
    const sharedSteps = prepareSteps(this.baseSteps(), [
      {
        after: ELTERN,
        steps: steuerdaten?.steuerdaten?.map(createElternSteuerStep) ?? [],
      },
    ]);
    return {
      'gesuch-app': [...sharedSteps, ABSCHLUSS],
      'sachbearbeitung-app': sharedSteps,
    };
  });

  stepFlowSig = computed(() => {
    const allSteps = this.stepsSig();
    return {
      'gesuch-app': [...allSteps['gesuch-app'], RETURN_TO_HOME],
      'sachbearbeitung-app': [
        ...allSteps['sachbearbeitung-app'],
        RETURN_TO_HOME,
      ],
    };
  });

  getStepProgressSig(
    step?: SharedModelGesuchFormStep,
  ): Signal<SharedModelGesuchFormStepProgress> {
    return computed(() => {
      const allSteps = this.stepsSig()[this.compileTimeConfig.appType];
      if (!step) {
        return {
          step: undefined,
          total: allSteps.length,
          percentage: undefined,
        };
      }
      const stepIndex = allSteps.findIndex((s) => s.route === step.route);
      return {
        step: stepIndex + 1,
        total: allSteps.length,
        percentage: ((stepIndex + 1) / allSteps.length) * 100,
      };
    });
  }

  getValidatedStepsSig(
    gesuchFormular: SharedModelGesuchFormular | null,
    invalidProps?: StepValidation,
  ): Signal<GesuchFormStepView[]> {
    return computed(() => {
      const steps = this.stepsSig()[this.compileTimeConfig.appType];
      return steps.map((step, index) => ({
        ...step,
        nextStep: steps[index + 1],
        status: isStepValid(step, gesuchFormular, invalidProps),
        disabled: isStepDisabled(step, gesuchFormular),
      }));
    });
  }

  compareStepsByFlow(
    a: SharedModelGesuchFormStep,
    b: SharedModelGesuchFormStep,
    onEqual?: (
      a: SharedModelGesuchFormStep,
      b: SharedModelGesuchFormStep,
    ) => number,
  ) {
    const steps = this.stepFlowSig()[this.compileTimeConfig.appType];
    const aIndex = steps.findIndex((step) => step.route === a.route);
    const bIndex = steps.findIndex((step) => step.route === b.route);
    if (aIndex === -1 || bIndex === -1) {
      return 1;
    }
    if (aIndex === bIndex) {
      return onEqual?.(a, b) ?? 0;
    }
    return aIndex - bIndex;
  }
}

type AdditionalSteps = {
  after: SharedModelGesuchFormStep;
  steps: SharedModelGesuchFormStep[];
};
const prepareSteps = (
  steps: SharedModelGesuchFormStep[],
  additionalSteps: AdditionalSteps[],
) => {
  const afterMap = additionalSteps.reduce(
    (acc, { after, steps }) => {
      if (steps.length > 0) {
        acc[after.route] = steps;
      }
      return acc;
    },
    {} as Record<string, SharedModelGesuchFormStep[]>,
  );
  return steps.reduce((acc, step) => {
    if (afterMap[step.route]) {
      return [...acc, step, ...afterMap[step.route]];
    }
    return [...acc, step];
  }, [] as SharedModelGesuchFormStep[]);
};
