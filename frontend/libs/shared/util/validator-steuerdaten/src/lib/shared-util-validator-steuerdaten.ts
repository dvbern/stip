import { Signal, computed, effect } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';

import { SharedModelGesuch } from '@dv/shared/model/gesuch';
import { sharedUtilValidatorRange } from '@dv/shared/util/validator-range';

export const prepareSteuerjahrValidation = (
  steuerjahrControl: FormControl<number | null>,
  viewSig: Signal<{ gesuch: SharedModelGesuch | null }>,
) => {
  const steuerjahrPeriodeSig = computed(() => {
    const { gesuch } = viewSig();

    const technischesJahr = gesuch?.gesuchsperiode.gesuchsjahr.technischesJahr;

    if (!technischesJahr) {
      return 0;
    }

    return technischesJahr - 1;
  });

  const createEffect = () =>
    effect(() => {
      const steuerjahr = steuerjahrPeriodeSig();

      if (!steuerjahr) {
        return;
      }

      steuerjahrControl.setValidators([
        Validators.required,
        sharedUtilValidatorRange(1900, steuerjahr),
      ]);

      steuerjahrControl.updateValueAndValidity();
    });

  return {
    steuerjahrPeriodeSig,
    createEffect,
  };
};
