import { differenceInYears } from 'date-fns';

import { AppType } from '@dv/shared/model/config';
import {
  DokumentTyp,
  SharedModelGesuch,
  SharedModelGesuchFormular,
  SharedModelGesuchFormularPropsSteuerdatenSteps,
  SteuerdatenSteps,
  SteuerdatenTyp,
  Zivilstand,
} from '@dv/shared/model/gesuch';
import { getGesuchPermissions } from '@dv/shared/model/permission-state';

import {
  SharedModelGesuchFormStep,
  StepState,
  StepValidation,
} from './shared-model-gesuch-form';

export const TRANCHE: SharedModelGesuchFormStep = {
  route: 'info',
  translationKey: 'shared.tranche.title',
  titleTranslationKey: 'shared.nothing',
  iconSymbolName: 'info',
} satisfies SharedModelGesuchFormStep;

export const PERSON = {
  route: 'person',
  translationKey: 'shared.person.title',
  titleTranslationKey: 'shared.person.title',
  iconSymbolName: 'person',
} satisfies SharedModelGesuchFormStep;

export const AUSBILDUNG: SharedModelGesuchFormStep = {
  route: 'ausbildung',
  translationKey: 'shared.ausbildung.title',
  titleTranslationKey: 'shared.ausbildung.title',
  iconSymbolName: 'school',
} satisfies SharedModelGesuchFormStep;

export const LEBENSLAUF: SharedModelGesuchFormStep = {
  route: 'lebenslauf',
  translationKey: 'shared.lebenslauf.title',
  titleTranslationKey: 'shared.lebenslauf.title',
  iconSymbolName: 'news',
} satisfies SharedModelGesuchFormStep;

export const FAMILIENSITUATION: SharedModelGesuchFormStep = {
  route: 'familiensituation',
  translationKey: 'shared.familiensituation.title',
  titleTranslationKey: 'shared.familiensituation.title',
  iconSymbolName: 'family_restroom',
} satisfies SharedModelGesuchFormStep;

export const ELTERN: SharedModelGesuchFormStep = {
  route: 'eltern',
  translationKey: 'shared.eltern.title',
  titleTranslationKey: 'shared.eltern.title',
  iconSymbolName: 'escalator_warning',
} satisfies SharedModelGesuchFormStep;

export const GESCHWISTER: SharedModelGesuchFormStep = {
  route: 'geschwister',
  translationKey: 'shared.geschwister.title',
  titleTranslationKey: 'shared.geschwister.title',
  iconSymbolName: 'group',
} satisfies SharedModelGesuchFormStep;

export const PARTNER: SharedModelGesuchFormStep = {
  route: 'partner',
  translationKey: 'shared.partner.title',
  titleTranslationKey: 'shared.partner.page.title',
  iconSymbolName: 'favorite',
} satisfies SharedModelGesuchFormStep;

export const KINDER: SharedModelGesuchFormStep = {
  route: 'kinder',
  translationKey: 'shared.kinder.title',
  titleTranslationKey: 'shared.kinder.title',
  iconSymbolName: 'emoji_people',
} satisfies SharedModelGesuchFormStep;

export const AUSZAHLUNG: SharedModelGesuchFormStep = {
  route: 'auszahlungen',
  translationKey: 'shared.auszahlung.title',
  titleTranslationKey: 'shared.auszahlung.title',
  iconSymbolName: 'payments',
} satisfies SharedModelGesuchFormStep;

export const EINNAHMEN_KOSTEN: SharedModelGesuchFormStep = {
  route: 'einnahmenkosten',
  translationKey: 'shared.einnahmenkosten.title',
  titleTranslationKey: 'shared.einnahmenkosten.page.title',
  iconSymbolName: 'call_missed_outgoing',
} satisfies SharedModelGesuchFormStep;

export const DOKUMENTE: SharedModelGesuchFormStep = {
  route: 'dokumente',
  translationKey: 'shared.dokumente.title',
  titleTranslationKey: 'shared.dokumente.title',
  iconSymbolName: 'description',
} satisfies SharedModelGesuchFormStep;

export const ABSCHLUSS: SharedModelGesuchFormStep = {
  route: 'abschluss',
  translationKey: 'shared.abschluss.title',
  titleTranslationKey: 'shared.abschluss.title',
  iconSymbolName: 'check_circle',
} satisfies SharedModelGesuchFormStep;

export const RETURN_TO_HOME: SharedModelGesuchFormStep = {
  route: '/',
  translationKey: '',
  titleTranslationKey: '',
  iconSymbolName: '',
} satisfies SharedModelGesuchFormStep;

export const PROTOKOLL: SharedModelGesuchFormStep = {
  route: 'protokoll',
  translationKey: 'shared.protokoll.title',
  titleTranslationKey: 'shared.protokoll.title',
  iconSymbolName: 'history',
} satisfies SharedModelGesuchFormStep;

export const DARLEHEN: SharedModelGesuchFormStep = {
  route: 'darlehen',
  translationKey: 'shared.darlehen.title',
  titleTranslationKey: 'shared.darlehen.title',
  iconSymbolName: 'account_balance',
} satisfies SharedModelGesuchFormStep;

// Dynamic steps
export const ELTERN_STEUERDATEN_ROUTE = 'eltern-steuerdaten';
const steuerTypeIconMap: Record<SteuerdatenTyp, string> = {
  FAMILIE: 'people',
  MUTTER: 'woman',
  VATER: 'man',
};
const createElternSteuerStep = (
  steuerdatenTyp: SteuerdatenTyp,
): SharedModelGesuchFormStep & { type: SteuerdatenTyp } => {
  return {
    type: steuerdatenTyp,
    route: `eltern-steuerdaten/${steuerdatenTyp}`,
    translationKey: `shared.eltern-steuer.title.${steuerdatenTyp}`,
    titleTranslationKey: `shared.eltern-steuer.title.${steuerdatenTyp}`,
    iconSymbolName: steuerTypeIconMap[steuerdatenTyp],
  };
};
export const ELTERN_STEUER_FAMILIE = createElternSteuerStep('FAMILIE');
export const ELTERN_STEUER_MUTTER = createElternSteuerStep('MUTTER');
export const ELTERN_STEUER_VATER = createElternSteuerStep('VATER');
export const ELTERN_STEUER_STEPS: Record<
  SteuerdatenTyp,
  SharedModelGesuchFormStep
> = {
  FAMILIE: ELTERN_STEUER_FAMILIE,
  MUTTER: ELTERN_STEUER_MUTTER,
  VATER: ELTERN_STEUER_VATER,
};

export const gesuchFormBaseSteps = {
  AUSBILDUNG,
  PERSON,
  LEBENSLAUF,
  FAMILIENSITUATION,
  ELTERN,
  GESCHWISTER,
  PARTNER,
  KINDER,
  AUSZAHLUNG,
  EINNAHMEN_KOSTEN,
  DARLEHEN,
  DOKUMENTE,
};
export type GesuchFormBaseStepKeys = keyof typeof gesuchFormBaseSteps;

export const gesuchFormSteps = {
  ...gesuchFormBaseSteps,
  ABSCHLUSS,
};
export type GesuchFormStepKeys = keyof typeof gesuchFormSteps;

export const gesuchPropFormStepsMap: Record<
  SharedModelGesuchFormularPropsSteuerdatenSteps,
  SharedModelGesuchFormStep
> = {
  ausbildung: AUSBILDUNG,
  familiensituation: FAMILIENSITUATION,
  partner: PARTNER,
  personInAusbildung: PERSON,
  auszahlung: AUSZAHLUNG,
  elterns: ELTERN,
  geschwisters: GESCHWISTER,
  lebenslaufItems: LEBENSLAUF,
  kinds: KINDER,
  einnahmenKosten: EINNAHMEN_KOSTEN,
  darlehen: DARLEHEN,
  dokuments: DOKUMENTE,
  steuerdaten: ELTERN_STEUER_FAMILIE,
  steuerdatenMutter: ELTERN_STEUER_MUTTER,
  steuerdatenVater: ELTERN_STEUER_VATER,
};

export const gesuchFormStepsFieldMap: Record<
  string,
  SharedModelGesuchFormularPropsSteuerdatenSteps
> = {
  [AUSBILDUNG.route]: 'ausbildung',
  [PERSON.route]: 'personInAusbildung',
  [LEBENSLAUF.route]: 'lebenslaufItems',
  [FAMILIENSITUATION.route]: 'familiensituation',
  [ELTERN.route]: 'elterns',
  [ELTERN_STEUER_MUTTER.route]: 'steuerdatenMutter',
  [ELTERN_STEUER_VATER.route]: 'steuerdatenVater',
  [ELTERN_STEUER_FAMILIE.route]: 'steuerdaten',
  [GESCHWISTER.route]: 'geschwisters',
  [PARTNER.route]: 'partner',
  [KINDER.route]: 'kinds',
  [AUSZAHLUNG.route]: 'auszahlung',
  [EINNAHMEN_KOSTEN.route]: 'einnahmenKosten',
  [DARLEHEN.route]: 'darlehen',
  [DOKUMENTE.route]: 'dokuments',
};

export const findStepIndex = (
  step: SharedModelGesuchFormStep,
  steps: SharedModelGesuchFormStep[],
) => steps.findIndex((s) => s.route === step.route);

export const isStepDisabled = (
  step: SharedModelGesuchFormStep,
  gesuch: SharedModelGesuch | null,
  appType: AppType,
) => {
  const formular = gesuch?.gesuchTrancheToWorkWith.gesuchFormular ?? null;
  const gesuchPermissions = getGesuchPermissions(gesuch, appType);
  const readonly = !gesuchPermissions?.canWrite;

  switch (step) {
    case PARTNER: {
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
    case GESCHWISTER: {
      const geschwister = formular?.geschwisters;
      return readonly && (!geschwister || geschwister.length === 0);
    }
    case KINDER: {
      const kinder = formular?.kinds;
      return readonly && (!kinder || kinder.length === 0);
    }
    case ELTERN: {
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
    case DARLEHEN: {
      if (!formular?.personInAusbildung?.geburtsdatum) return true;

      const geburtsdatum = new Date(formular?.personInAusbildung?.geburtsdatum);
      const istErwachsen = differenceInYears(new Date(), geburtsdatum) >= 18;

      return !istErwachsen;
    }
    default:
      return false;
  }
};

export const isStepValid = (
  step: SharedModelGesuchFormStep,
  formular: SharedModelGesuchFormular | null,
  invalidProps?: StepValidation,
): StepState | undefined => {
  if (invalidProps?.errors === undefined) {
    return undefined;
  }

  const field = gesuchFormStepsFieldMap[step.route];

  if (!field) {
    return undefined;
  }

  const isDefined = (value: unknown) => value !== null && value !== undefined;

  // Todo: will probably need to be adjusted for GS and differently for SB
  if (isSteuerdatenStep(field)) {
    const [stepSteuerdatenTyp] =
      Object.entries(ELTERN_STEUER_STEPS).find(
        ([, s]) => s.route === step.route,
      ) ?? [];
    const currentHasDaten = formular?.steuererklaerung?.find(
      (s) => s.steuerdatenTyp === stepSteuerdatenTyp,
    );
    return toStepState(field, isDefined(currentHasDaten), invalidProps);
  }

  if (field === 'lebenslaufItems') {
    return toStepState(
      field,
      isDefined(formular?.personInAusbildung && formular.ausbildung),
      invalidProps,
    );
  }

  if (field === 'dokuments') {
    return toDocumentStepState(invalidProps);
  }

  return toStepState(field, isDefined(formular?.[field]), invalidProps);
};

export const getFormStepByDocumentType = (
  dokumentTyp: DokumentTyp,
): SharedModelGesuchFormStep => {
  switch (dokumentTyp) {
    case DokumentTyp.KINDER_UNTERHALTSVERTRAG_TRENNUNGSKONVENTION: {
      return gesuchFormSteps.DOKUMENTE;
    }
    default: {
      const step = (Object.keys(gesuchFormSteps) as GesuchFormStepKeys[]).find(
        (key) => {
          if (key === 'EINNAHMEN_KOSTEN') {
            return dokumentTyp.startsWith('EK');
          }
          return dokumentTyp.startsWith(key);
        },
      );
      if (!step) {
        console.error(`No step found for document type "${dokumentTyp}"`);
        return gesuchFormSteps.DOKUMENTE;
      }

      return gesuchFormSteps[step];
    }
  }
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
  field: SharedModelGesuchFormularPropsSteuerdatenSteps,
  isDefined: boolean,
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
  return isDefined ? 'VALID' : undefined;
};

const isSteuerdatenStep = (
  step: SharedModelGesuchFormularPropsSteuerdatenSteps,
): step is SteuerdatenSteps => step.startsWith('steuerdaten');
