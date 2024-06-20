import {
  SharedModelGesuchFormular,
  SharedModelGesuchFormularProps,
  SteuerdatenTab,
  Zivilstand,
} from '@dv/shared/model/gesuch';

import {
  SharedModelGesuchFormStep,
  StepState,
  StepValidation,
} from './shared-model-gesuch-form';

export const PERSON = {
  route: 'person',
  translationKey: 'shared.person.title',
  iconSymbolName: 'person',
} satisfies SharedModelGesuchFormStep;

export const AUSBILDUNG = {
  route: 'education',
  translationKey: 'shared.education.title',
  iconSymbolName: 'school',
} satisfies SharedModelGesuchFormStep;

export const LEBENSLAUF = {
  route: 'lebenslauf',
  translationKey: 'shared.lebenslauf.title',
  iconSymbolName: 'news',
} satisfies SharedModelGesuchFormStep;

export const FAMILIENSITUATION = {
  route: 'familiensituation',
  translationKey: 'shared.familiensituation.title',
  iconSymbolName: 'family_restroom',
} satisfies SharedModelGesuchFormStep;

export const ELTERN = {
  route: 'eltern',
  translationKey: 'shared.eltern.title',
  iconSymbolName: 'escalator_warning',
} satisfies SharedModelGesuchFormStep;

export const GESCHWISTER = {
  route: 'geschwister',
  translationKey: 'shared.geschwister.title',
  iconSymbolName: 'group',
} satisfies SharedModelGesuchFormStep;

export const PARTNER = {
  route: 'partner',
  translationKey: 'shared.partner.title',
  iconSymbolName: 'favorite',
} satisfies SharedModelGesuchFormStep;

export const KINDER = {
  route: 'kinder',
  translationKey: 'shared.kinder.title',
  iconSymbolName: 'emoji_people',
} satisfies SharedModelGesuchFormStep;

export const AUSZAHLUNG = {
  route: 'auszahlungen',
  translationKey: 'shared.auszahlung.title',
  iconSymbolName: 'payments',
} satisfies SharedModelGesuchFormStep;

export const EINNAHMEN_KOSTEN = {
  route: 'einnahmenkosten',
  translationKey: 'shared.einnahmenkosten.title',
  iconSymbolName: 'call_missed_outgoing',
} satisfies SharedModelGesuchFormStep;

export const DOKUMENTE = {
  route: 'dokumente',
  translationKey: 'shared.dokumente.title',
  iconSymbolName: 'description',
} satisfies SharedModelGesuchFormStep;

export const ABSCHLUSS = {
  route: 'abschluss',
  translationKey: 'shared.abschluss.title',
  iconSymbolName: 'check_circle',
} satisfies SharedModelGesuchFormStep;

// TODO use correct steuernTab type
const steuerTypeIconMap: Record<string, string> = {
  family: 'people',
  mutter: 'woman',
  vater: 'man',
};
export const createElternSteuerStep = (
  steuernTab: SteuerdatenTab,
): SharedModelGesuchFormStep => {
  return {
    route: `eltern-steuer-${steuernTab.type}`,
    translationKey: 'shared.eltern-steuer.title',
    iconSymbolName: steuerTypeIconMap[steuernTab.type],
  };
};

export const gesuchFormSteps = {
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
  ABSCHLUSS,
};

export const isStepDisabled = (
  step: SharedModelGesuchFormStep,
  formular: SharedModelGesuchFormular | null,
) => {
  if (step === PARTNER) {
    const zivilstand = formular?.personInAusbildung?.zivilstand;
    return (
      !zivilstand ||
      ![
        Zivilstand.VERHEIRATET,
        Zivilstand.KONKUBINAT,
        Zivilstand.EINGETRAGENE_PARTNERSCHAFT,
      ].includes(zivilstand)
    );
  }
  if (step === ELTERN) {
    const werZahltAlimente = formular?.familiensituation?.werZahltAlimente;
    const mutterUnbekanntVerstorben =
      formular?.familiensituation?.mutterUnbekanntVerstorben;
    const vaterUnbekanntVerstorben =
      formular?.familiensituation?.vaterUnbekanntVerstorben;
    return (
      werZahltAlimente === 'GEMEINSAM' ||
      ((mutterUnbekanntVerstorben === 'VERSTORBEN' ||
        mutterUnbekanntVerstorben === 'UNBEKANNT') &&
        (vaterUnbekanntVerstorben === 'VERSTORBEN' ||
          vaterUnbekanntVerstorben === 'UNBEKANNT'))
    );
  }
  return false;
};

export const isStepValid = (
  step: SharedModelGesuchFormStep,
  formular: SharedModelGesuchFormular | null,
  invalidProps?: StepValidation,
) => {
  const stepFieldMap: Record<string, SharedModelGesuchFormularProps> = {
    [PERSON.route]: 'personInAusbildung',
    [AUSBILDUNG.route]: 'ausbildung',
    [LEBENSLAUF.route]: 'lebenslaufItems',
    [FAMILIENSITUATION.route]: 'familiensituation',
    [ELTERN.route]: 'elterns',
    [GESCHWISTER.route]: 'geschwisters',
    [PARTNER.route]: 'partner',
    [KINDER.route]: 'kinds',
    [AUSZAHLUNG.route]: 'auszahlung',
    [EINNAHMEN_KOSTEN.route]: 'einnahmenKosten',
    [DOKUMENTE.route]: 'dokuments',
  };

  const field = stepFieldMap[step.route];

  if (field === 'dokuments') {
    return toDocumentStepState(invalidProps);
  }

  return formular?.[field] ? toStepState(field, invalidProps) : undefined;
};

const toDocumentStepState = (
  invalidProps?: StepValidation,
): StepState | undefined => {
  if (invalidProps?.errors?.includes('dokuments')) {
    return 'INVALID';
  }
  if (invalidProps?.warnings?.includes('dokuments')) {
    return 'WARNING';
  }
  if (invalidProps?.hasDocuments === true) {
    return 'VALID';
  }
  return undefined;
};

const toStepState = (
  field: SharedModelGesuchFormularProps,
  invalidProps?: StepValidation,
): StepState | undefined => {
  if (invalidProps?.errors?.includes(field)) {
    return 'INVALID';
  }
  if (invalidProps?.warnings?.includes(field)) {
    return 'WARNING';
  }
  // If the error list is still empty then no status is identifiable
  if (invalidProps?.errors === undefined) {
    return undefined;
  }
  return 'VALID';
};
