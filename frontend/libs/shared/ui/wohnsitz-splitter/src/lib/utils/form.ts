import {
  FormControl,
  NonNullableFormBuilder,
  Validators,
} from '@angular/forms';

import {
  ElternTyp,
  Familiensituation,
  FamiliensituationUpdate,
  Wohnsitz,
} from '@dv/shared/model/gesuch';
import {
  isVerstorbenOrUnbekannt,
  numberToPercentString,
  percentStringToNumber,
} from '@dv/shared/util/form';
import { capitalized } from '@dv/shared/util-fn/string-helper';

type WohnsitzAnteile<T> = {
  wohnsitzAnteilVater: T;
  wohnsitzAnteilMutter: T;
};

export const addWohnsitzControls = (fb: NonNullableFormBuilder) => {
  return {
    wohnsitz: fb.control<Wohnsitz>('' as Wohnsitz, [Validators.required]),
    wohnsitzAnteilMutter: [
      <string | undefined>undefined,
      [Validators.required],
    ],
    wohnsitzAnteilVater: [<string | undefined>undefined, [Validators.required]],
  };
};

export function wohnsitzAnteileNumber(
  anteile: Partial<WohnsitzAnteile<string>>,
) {
  return {
    wohnsitzAnteilMutter: percentStringToNumber(anteile.wohnsitzAnteilMutter),
    wohnsitzAnteilVater: percentStringToNumber(anteile.wohnsitzAnteilVater),
  };
}

export function prepareWohnsitzValues(
  familiensituation?: FamiliensituationUpdate,
) {
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
    isVerstorbenOrUnbekannt(familiensituation.mutterUnbekanntVerstorben) &&
    isVerstorbenOrUnbekannt(familiensituation.vaterUnbekanntVerstorben)
  ) {
    availableWohnsitz = availableWohnsitz.filter((v) => v !== 'MUTTER_VATER');
  }

  return availableWohnsitz;
}

export function wohnsitzAnteileString(
  anteile: Partial<WohnsitzAnteile<number>>,
  familiensituation: Familiensituation | undefined,
): WohnsitzAnteile<string | undefined> {
  const mutterMissing = isVerstorbenOrUnbekannt(
    familiensituation?.mutterUnbekanntVerstorben,
  );
  const vaterMissing = isVerstorbenOrUnbekannt(
    familiensituation?.vaterUnbekanntVerstorben,
  );
  const getAnteil = (elternTyp: ElternTyp, missing: boolean) =>
    familiensituation?.elternteilUnbekanntVerstorben
      ? missing
        ? // If the current eltenteil is unknown or dead, we set the Anteil to 0%.
          '0%'
        : // Otherwise we set the anteil to 100% as the other parent is unknown or dead.
          '100%'
      : // If no elternteil is unknown or dead, we set the anteil to the value.
        numberToPercentString(
          anteile[`wohnsitzAnteil${capitalized(elternTyp)}`],
        );
  return mutterMissing && vaterMissing
    ? {
        wohnsitzAnteilMutter: undefined,
        wohnsitzAnteilVater: undefined,
      }
    : {
        wohnsitzAnteilMutter: getAnteil('MUTTER', mutterMissing),
        wohnsitzAnteilVater: getAnteil('VATER', vaterMissing),
      };
}

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
