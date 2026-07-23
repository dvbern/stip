import { Signal, computed, effect } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import {
  FormControl,
  NonNullableFormBuilder,
  ValidatorFn,
  Validators,
} from '@angular/forms';

import { ElternTyp, GesuchFormular, Wohnsitz } from '@dv/shared/model/gesuch';
import { capitalized, lowercased } from '@dv/shared/model/type-util';
import {
  isVerstorbenUnbekannt,
  numberToPercentString,
  percentStringToNumber,
} from '@dv/shared/util/form';

type WohnsitzAnteile<T> = {
  wohnsitzAnteilVater: T;
  wohnsitzAnteilMutter: T;
};

type WohnsitzValuesGetter = (
  gesuchFormular?: GesuchFormular | null,
) => Partial<WohnsitzAnteile<number>> | undefined;

export const prepareWohnsitzForm = (payload: {
  projector: WohnsitzValuesGetter;
  viewSig: Signal<{
    gesuchFormular: GesuchFormular | null;
    readonly: boolean;
    allowOnlyOne?: boolean;
  }>;
  form: WohnsitzAnteile<FormControl<string | undefined>> & {
    wohnsitz: FormControl<Wohnsitz>;
  };
  refreshSig: Signal<unknown>;
}) => {
  const { projector, viewSig, form, refreshSig } = payload;

  const wohnsitzChangedSig = toSignal(form.wohnsitz.valueChanges);
  const wohnsitzValuesSig = computed(() => {
    const familiensituation = viewSig().gesuchFormular?.familiensituation;
    let availableWohnsitz = Object.values(Wohnsitz);

    if (!familiensituation) {
      return availableWohnsitz;
    }

    if (familiensituation.elternVerheiratetZusammen) {
      availableWohnsitz = availableWohnsitz.filter((v) => v !== 'MUTTER_VATER');
    } else {
      availableWohnsitz = availableWohnsitz.filter((v) => v !== 'FAMILIE');
    }

    if (
      isVerstorbenUnbekannt('MUTTER', familiensituation) &&
      isVerstorbenUnbekannt('VATER', familiensituation)
    ) {
      availableWohnsitz = availableWohnsitz.filter((v) => v !== 'MUTTER_VATER');
    }

    return availableWohnsitz;
  });

  const wohnsitzAnteileFromNumber = () => {
    return {
      wohnsitzAnteilMutter: percentStringToNumber(
        form.wohnsitzAnteilMutter.getRawValue(),
      ),
      wohnsitzAnteilVater: percentStringToNumber(
        form.wohnsitzAnteilVater.getRawValue(),
      ),
    };
  };

  const wohnsitzAnteileAsString = () => {
    const formular = viewSig().gesuchFormular;
    const familiensituation = formular?.familiensituation;
    const mutterMissing = isVerstorbenUnbekannt('MUTTER', familiensituation);
    const vaterMissing = isVerstorbenUnbekannt('VATER', familiensituation);
    const getAnteil = (elternTyp: ElternTyp, missing: boolean) => {
      const missingPercentage = missing
        ? // If the current eltenteil is unknown or dead, we set the Anteil to 0%.
          '0%'
        : // Otherwise we set the anteil to 100% as the other parent is unknown or dead.
          '100%';
      return familiensituation?.elternteilUnbekanntVerstorben
        ? missingPercentage
        : // If no elternteil is unknown or dead, we set the anteil to the value.
          numberToPercentString(
            projector(formular)?.[
              `wohnsitzAnteil${capitalized(lowercased(elternTyp))}`
            ],
          );
    };
    return mutterMissing && vaterMissing
      ? {
          wohnsitzAnteilMutter: undefined,
          wohnsitzAnteilVater: undefined,
        }
      : {
          wohnsitzAnteilMutter: getAnteil('MUTTER', mutterMissing),
          wohnsitzAnteilVater: getAnteil('VATER', vaterMissing),
        };
  };

  const showWohnsitzSplitterSig = computed(() => {
    return (
      wohnsitzChangedSig() === 'MUTTER_VATER' &&
      wohnsitzValuesSig().includes('MUTTER_VATER')
    );
  });

  const minOneRequiredValidator = minOneRequired(form);
  effect(() => {
    refreshSig();
    const { gesuchFormular, allowOnlyOne } = viewSig();
    const { elternteilUnbekanntVerstorben } =
      viewSig().gesuchFormular?.familiensituation ?? {};
    const wohnsitzNotMutterVater =
      wohnsitzChangedSig() !== Wohnsitz.MUTTER_VATER;

    updateWohnsitzControlsState(
      form,
      wohnsitzNotMutterVater ||
        viewSig().readonly ||
        !showWohnsitzSplitterSig() ||
        !!elternteilUnbekanntVerstorben,
    );

    if (wohnsitzNotMutterVater) {
      form.wohnsitzAnteilMutter.reset();
      form.wohnsitzAnteilVater.reset();
    } else if (gesuchFormular) {
      const anteile = wohnsitzAnteileAsString();
      form.wohnsitzAnteilMutter.patchValue(anteile.wohnsitzAnteilMutter);
      form.wohnsitzAnteilVater.patchValue(anteile.wohnsitzAnteilVater);
    }

    if (allowOnlyOne) {
      form.wohnsitzAnteilMutter.removeValidators(Validators.required);
      form.wohnsitzAnteilVater.removeValidators(Validators.required);
      form.wohnsitzAnteilMutter.addValidators(minOneRequiredValidator);
      form.wohnsitzAnteilVater.addValidators(minOneRequiredValidator);
    } else {
      form.wohnsitzAnteilMutter.addValidators(Validators.required);
      form.wohnsitzAnteilVater.addValidators(Validators.required);
      form.wohnsitzAnteilMutter.removeValidators(minOneRequiredValidator);
      form.wohnsitzAnteilVater.removeValidators(minOneRequiredValidator);
    }
  });

  return {
    wohnsitzAnteileFromNumber,
    wohnsitzAnteileAsString,
    wohnsitzValuesSig,
    showWohnsitzSplitterSig,
  };
};

export const addWohnsitzControls = (formBuilder: NonNullableFormBuilder) => {
  return {
    wohnsitz: formBuilder.control<Wohnsitz>('' as Wohnsitz, [
      Validators.required,
    ]),
    wohnsitzAnteilMutter: [<string | undefined>undefined],
    wohnsitzAnteilVater: [<string | undefined>undefined],
  };
};

export function updateWohnsitzControlsState(
  form: WohnsitzAnteile<FormControl>,
  disable: boolean,
) {
  if (disable) {
    form.wohnsitzAnteilMutter.disable();
    form.wohnsitzAnteilVater.disable();
  } else {
    form.wohnsitzAnteilMutter.enable();
    form.wohnsitzAnteilVater.enable();
  }
}

export const minOneRequired =
  (controls: WohnsitzAnteile<FormControl<string | undefined>>): ValidatorFn =>
  () => {
    const hasNoAnteile = (['Mutter', 'Vater'] as const).every(
      (elternteil) =>
        !percentStringToNumber(controls[`wohnsitzAnteil${elternteil}`].value),
    );

    return hasNoAnteile
      ? {
          atLeastOneValue: true,
        }
      : {};
  };
