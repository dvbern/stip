import { AppType } from '@dv/shared/model/config';
import {
  DokumentTyp,
  GSFormStepProps,
  GSSteuererklaerungSteps,
  GesuchFormularType,
  SBFormStepProps,
  SBSteuerdatenSteps,
  SharedModelGesuch,
  Steuerdaten,
  SteuerdatenTyp,
} from '@dv/shared/model/gesuch';
import { PermissionMap } from '@dv/shared/model/permission-state';
import { isDefined } from '@dv/shared/model/type-util';

import {
  GesuchFormStep,
  StepState,
  StepValidation,
} from './shared-model-gesuch-form';

export const TRANCHE: GesuchFormStep = {
  route: 'info',
  translationKey: 'shared.tranche.title',
  titleTranslationKey: 'shared.nothing',
  iconSymbolName: 'info',
} satisfies GesuchFormStep;

export const PERSON = {
  route: 'person',
  translationKey: 'shared.person.title',
  titleTranslationKey: 'shared.person.title',
  iconSymbolName: 'person',
} satisfies GesuchFormStep;

export const AUSBILDUNG: GesuchFormStep = {
  route: 'ausbildung',
  translationKey: 'shared.ausbildung.title',
  titleTranslationKey: 'shared.ausbildung.title',
  iconSymbolName: 'school',
} satisfies GesuchFormStep;

export const LEBENSLAUF: GesuchFormStep = {
  route: 'lebenslauf',
  translationKey: 'shared.lebenslauf.title',
  titleTranslationKey: 'shared.lebenslauf.title',
  iconSymbolName: 'news',
} satisfies GesuchFormStep;

export const FAMILIENSITUATION: GesuchFormStep = {
  route: 'familiensituation',
  translationKey: 'shared.familiensituation.title',
  titleTranslationKey: 'shared.familiensituation.title',
  iconSymbolName: 'family_restroom',
} satisfies GesuchFormStep;

export const ELTERN: GesuchFormStep = {
  route: 'eltern',
  translationKey: 'shared.eltern.title',
  titleTranslationKey: 'shared.eltern.title',
  iconSymbolName: 'escalator_warning',
} satisfies GesuchFormStep;

export const GESCHWISTER: GesuchFormStep = {
  route: 'geschwister',
  translationKey: 'shared.geschwister.title',
  titleTranslationKey: 'shared.geschwister.title',
  iconSymbolName: 'group',
} satisfies GesuchFormStep;

export const PARTNER: GesuchFormStep = {
  route: 'partner',
  translationKey: 'shared.partner.title',
  titleTranslationKey: 'shared.partner.page.title',
  iconSymbolName: 'favorite',
} satisfies GesuchFormStep;

export const KINDER: GesuchFormStep = {
  route: 'kinder',
  translationKey: 'shared.kinder.title',
  titleTranslationKey: 'shared.kinder.title',
  iconSymbolName: 'emoji_people',
} satisfies GesuchFormStep;

export const AUSZAHLUNG: GesuchFormStep = {
  route: 'auszahlungen',
  translationKey: 'shared.auszahlung.title',
  titleTranslationKey: 'shared.auszahlung.title',
  iconSymbolName: 'payments',
} satisfies GesuchFormStep;

export const EINNAHMEN_KOSTEN: GesuchFormStep = {
  route: 'einnahmenkosten',
  translationKey: 'shared.einnahmenkosten.title',
  titleTranslationKey: 'shared.einnahmenkosten.page.title',
  iconSymbolName: 'call_missed_outgoing',
} satisfies GesuchFormStep;

export const EINNAHMEN_KOSTEN_PARTNER: GesuchFormStep = {
  route: 'einnahmenkosten-partner',
  translationKey: 'shared.einnahmenkosten-partner.title',
  titleTranslationKey: 'shared.einnahmenkosten-partner.page.title',
  iconSymbolName: 'credit_card_heart',
};

export const DOKUMENTE: GesuchFormStep = {
  route: 'dokumente',
  translationKey: 'shared.dokumente.title',
  titleTranslationKey: 'shared.dokumente.title',
  iconSymbolName: 'description',
} satisfies GesuchFormStep;

export const ABSCHLUSS: GesuchFormStep = {
  route: 'abschluss',
  translationKey: 'shared.abschluss.title',
  titleTranslationKey: 'shared.abschluss.title',
  iconSymbolName: 'check_circle',
} satisfies GesuchFormStep;

export const RETURN_TO_HOME: GesuchFormStep = {
  route: '/',
  translationKey: 'shared.nothing',
  titleTranslationKey: 'shared.nothing',
  iconSymbolName: '',
} satisfies GesuchFormStep;

export const PROTOKOLL: GesuchFormStep = {
  route: 'protokoll',
  translationKey: 'shared.protokoll.title',
  titleTranslationKey: 'shared.protokoll.title',
  iconSymbolName: 'history',
} satisfies GesuchFormStep;

const steuerTypeIconMap: Record<SteuerdatenTyp, string> = {
  FAMILIE: 'people',
  MUTTER: 'woman',
  VATER: 'man',
};

// Dynamic steps

// for GS and SB
export const ELTERN_STEUERERKLAERUNG_ROUTE = 'eltern-steuererklaerung';
const createElternSteuererklaerungStep = (
  steuerdatenTyp: SteuerdatenTyp,
): GesuchFormStep & { type: SteuerdatenTyp } => {
  return {
    type: steuerdatenTyp,
    route: `${ELTERN_STEUERERKLAERUNG_ROUTE}/${steuerdatenTyp}`,
    translationKey: `shared.eltern-steuererklaerung.title.${steuerdatenTyp}`,
    titleTranslationKey: `shared.eltern-steuererklaerung.title.${steuerdatenTyp}`,
    iconSymbolName: steuerTypeIconMap[steuerdatenTyp],
  };
};
export const ELTERN_STEUERERKLAERUNG_FAMILIE =
  createElternSteuererklaerungStep('FAMILIE');
export const ELTERN_STEUERERKLAERUNG_MUTTER =
  createElternSteuererklaerungStep('MUTTER');
export const ELTERN_STEUERERKLAERUNG_VATER =
  createElternSteuererklaerungStep('VATER');
export const ELTERN_STEUERERKLAERUNG_STEPS: Record<
  SteuerdatenTyp,
  GesuchFormStep
> = {
  FAMILIE: ELTERN_STEUERERKLAERUNG_FAMILIE,
  MUTTER: ELTERN_STEUERERKLAERUNG_MUTTER,
  VATER: ELTERN_STEUERERKLAERUNG_VATER,
};

// Steuerdaten only for SB
export const ELTERN_STEUERDATEN_ROUTE = 'eltern-steuerdaten';
const createElternSteuerStep = (
  steuerdatenTyp: SteuerdatenTyp,
): GesuchFormStep & { type: SteuerdatenTyp } => {
  return {
    type: steuerdatenTyp,
    route: `${ELTERN_STEUERDATEN_ROUTE}/${steuerdatenTyp}`,
    translationKey: `shared.eltern-steuerdaten.title.${steuerdatenTyp}`,
    titleTranslationKey: `shared.eltern-steuerdaten.title.${steuerdatenTyp}`,
    iconSymbolName: steuerTypeIconMap[steuerdatenTyp],
  };
};
export const ELTERN_STEUERDATEN_FAMILIE = createElternSteuerStep('FAMILIE');
export const ELTERN_STEUERDATEN_MUTTER = createElternSteuerStep('MUTTER');
export const ELTERN_STEUERDATEN_VATER = createElternSteuerStep('VATER');
export const ELTERN_STEUERDATEN_STEPS: Record<SteuerdatenTyp, GesuchFormStep> =
  {
    FAMILIE: ELTERN_STEUERDATEN_FAMILIE,
    MUTTER: ELTERN_STEUERDATEN_MUTTER,
    VATER: ELTERN_STEUERDATEN_VATER,
  };

export const BaseFormSteps = {
  TRANCHE,
  AUSBILDUNG,
  PERSON,
  LEBENSLAUF,
  KINDER,
  EINNAHMEN_KOSTEN,
  FAMILIENSITUATION,
  ELTERN,
  GESCHWISTER,
  DOKUMENTE,
  AUSZAHLUNG,
};
export type BaseStepKeys = keyof typeof BaseFormSteps;

export const GSFormSteps = {
  ...BaseFormSteps,
  ABSCHLUSS,
};
export type GSFormStepKeys = keyof typeof GSFormSteps;

export const FormPropsToStepsMap: Record<
  GSFormStepProps | SBFormStepProps,
  GesuchFormStep
> = {
  ausbildung: AUSBILDUNG,
  familiensituation: FAMILIENSITUATION,
  partner: PARTNER,
  personInAusbildung: PERSON,
  auszahlung: AUSZAHLUNG,
  elterns: ELTERN,
  steuererklaerungFamilie: ELTERN_STEUERERKLAERUNG_FAMILIE,
  steuererklaerungMutter: ELTERN_STEUERERKLAERUNG_MUTTER,
  steuererklaerungVater: ELTERN_STEUERERKLAERUNG_VATER,
  steuerdatenFamilie: ELTERN_STEUERDATEN_FAMILIE,
  steuerdatenMutter: ELTERN_STEUERDATEN_MUTTER,
  steuerdatenVater: ELTERN_STEUERDATEN_VATER,
  geschwisters: GESCHWISTER,
  lebenslaufItems: LEBENSLAUF,
  kinds: KINDER,
  einnahmenKosten: EINNAHMEN_KOSTEN,
  einnahmenKostenPartner: EINNAHMEN_KOSTEN_PARTNER,
  dokuments: DOKUMENTE,
  abschluss: ABSCHLUSS,
};

export const FormRoutesToPropsMap: Record<
  string,
  GSFormStepProps | SBFormStepProps
> = {
  [AUSBILDUNG.route]: 'ausbildung',
  [PERSON.route]: 'personInAusbildung',
  [LEBENSLAUF.route]: 'lebenslaufItems',
  [FAMILIENSITUATION.route]: 'familiensituation',
  [ELTERN.route]: 'elterns',
  [ELTERN_STEUERERKLAERUNG_MUTTER.route]: 'steuererklaerungMutter',
  [ELTERN_STEUERERKLAERUNG_VATER.route]: 'steuererklaerungVater',
  [ELTERN_STEUERERKLAERUNG_FAMILIE.route]: 'steuererklaerungFamilie',
  [ELTERN_STEUERDATEN_FAMILIE.route]: 'steuerdatenFamilie',
  [ELTERN_STEUERDATEN_MUTTER.route]: 'steuerdatenMutter',
  [ELTERN_STEUERDATEN_VATER.route]: 'steuerdatenVater',
  [GESCHWISTER.route]: 'geschwisters',
  [PARTNER.route]: 'partner',
  [KINDER.route]: 'kinds',
  [AUSZAHLUNG.route]: 'auszahlung',
  [EINNAHMEN_KOSTEN.route]: 'einnahmenKosten',
  [EINNAHMEN_KOSTEN_PARTNER.route]: 'einnahmenKostenPartner',
  [DOKUMENTE.route]: 'dokuments',
};

export const findStepIndex = (step: GesuchFormStep, steps: GesuchFormStep[]) =>
  steps.findIndex((s) => s.route === step.route);

export const isStepDisabled = (
  step: GesuchFormStep,
  gesuch: SharedModelGesuch | null,
  permissions: PermissionMap,
) => {
  const formular = gesuch?.gesuchTrancheToWorkWith.gesuchFormular ?? null;
  const readonly = !permissions.canWrite;

  switch (step) {
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
    // todo: ask MADA if the 18 years logic is still needed
    // case DARLEHEN: {
    //   if (!formular?.personInAusbildung?.geburtsdatum) return true;

    //   const geburtsdatum = new Date(formular?.personInAusbildung?.geburtsdatum);
    //   const istErwachsen = differenceInYears(new Date(), geburtsdatum) >= 18;

    //   return !istErwachsen;
    // }
    default:
      return false;
  }
};

export const isStepValid = (
  step: GesuchFormStep,
  formular: GesuchFormularType | null,
  appType: AppType,
  steuerdaten?: Steuerdaten[],
  invalidProps?: StepValidation,
): StepState | undefined => {
  if (invalidProps?.errors === undefined) {
    return undefined;
  }

  const field = FormRoutesToPropsMap[step.route];

  // 'abschluss' only exists as step in the Frontend
  if (!field || field === 'abschluss') {
    return undefined;
  }

  if (isSteuererklaerungStep(field)) {
    const [stepSteuerdatenTyp] =
      Object.entries(ELTERN_STEUERERKLAERUNG_STEPS).find(
        ([, s]) => s.route === step.route,
      ) ?? [];
    const currentHasDaten = formular?.steuererklaerung?.find(
      (s) => s.steuerdatenTyp === stepSteuerdatenTyp,
    );
    return toStepState(field, isDefined(currentHasDaten), invalidProps);
  }

  if (isSteuerdatenStep(field)) {
    if (appType === 'gesuch-app') {
      return undefined;
    }

    const [stepSteuerdatenTyp] =
      Object.entries(ELTERN_STEUERDATEN_STEPS).find(
        ([, s]) => s.route === step.route,
      ) ?? [];

    const currentHasDaten = !!steuerdaten?.find(
      (s) => s.steuerdatenTyp === stepSteuerdatenTyp,
    );

    return currentHasDaten ? 'VALID' : 'INVALID';
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

  if (field === 'auszahlung') {
    return undefined;
  }

  return toStepState(field, isDefined(formular?.[field]), invalidProps);
};

export const getFormStepByDocumentType = (
  dokumentTyp: DokumentTyp,
): GesuchFormStep => {
  if (dokumentTyp.startsWith('STEUERERKLAERUNG_')) {
    const type: SteuerdatenTyp = Object.keys(SteuerdatenTyp).find((t) =>
      dokumentTyp.endsWith(t),
    ) as SteuerdatenTyp;
    if (type) {
      return ELTERN_STEUERERKLAERUNG_STEPS[type];
    }
  }

  if (dokumentTyp.startsWith('EK_PARTNER')) {
    return EINNAHMEN_KOSTEN_PARTNER;
  }

  switch (dokumentTyp) {
    case DokumentTyp.KINDER_UNTERHALTSVERTRAG_TRENNUNGSKONVENTION: {
      return GSFormSteps.DOKUMENTE;
    }
    default: {
      const step = (Object.keys(GSFormSteps) as GSFormStepKeys[]).find(
        (key) => {
          if (key === 'EINNAHMEN_KOSTEN') {
            return dokumentTyp.startsWith('EK');
          }
          return dokumentTyp.startsWith(key);
        },
      );
      if (!step) {
        console.error(`No step found for document type "${dokumentTyp}"`);
        return GSFormSteps.DOKUMENTE;
      }

      return GSFormSteps[step];
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
  field: GSFormStepProps | SBFormStepProps,
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

export const isSteuererklaerungStep = (
  step: GSFormStepProps | SBFormStepProps,
): step is GSSteuererklaerungSteps => step.startsWith('steuererklaerung');

export const isSteuerdatenStep = (
  step: GSFormStepProps | SBFormStepProps,
): step is SBSteuerdatenSteps => step.startsWith('steuerdaten');
