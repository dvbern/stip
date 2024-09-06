import { getRouterSelectors } from '@ngrx/router-store';
import { createSelector } from '@ngrx/store';
import { IChange, diff } from 'json-diff-ts';

import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
import { CompileTimeConfig } from '@dv/shared/model/config';
import {
  SharedModelGesuch,
  SharedModelGesuchFormular,
  SharedModelGesuchFormularProps,
  Steuerdaten,
  SteuerdatenTyp,
  ValidationMessage,
} from '@dv/shared/model/gesuch';
import {
  ABSCHLUSS,
  AUSBILDUNG,
  AUSZAHLUNG,
  DOKUMENTE,
  EINNAHMEN_KOSTEN,
  ELTERN,
  ELTERN_STEUER_STEPS,
  FAMILIENSITUATION,
  GESCHWISTER,
  KINDER,
  LEBENSLAUF,
  PARTNER,
  PERSON,
  PROTOKOLL,
  RETURN_TO_HOME,
  SPECIAL_VALIDATION_ERRORS,
  SharedModelGesuchFormStep,
  TRANCHE,
  isSpecialValidationError,
} from '@dv/shared/model/gesuch-form';
import {
  isGesuchReadonly,
  isTrancheReadonly,
} from '@dv/shared/util/readonly-state';
import { isDefined, type } from '@dv/shared/util-fn/type-guards';

import { sharedDataAccessGesuchsFeature } from './shared-data-access-gesuch.feature';

const baseSteps = [
  TRANCHE,
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

const { selectRouteParam } = getRouterSelectors();

export const selectRouteId = selectRouteParam('id');

export const selectRouteTrancheId = selectRouteParam('trancheId');

export const selectSharedDataAccessCachedGesuchChanges = createSelector(
  sharedDataAccessGesuchsFeature.selectCache,
  ({ gesuch }) => {
    return {
      tranchenChanges: prepareTranchenChanges(gesuch),
    };
  },
);

export const selectSharedDataAccessGesuchsView = createSelector(
  selectSharedDataAccessConfigsView,
  selectSharedDataAccessCachedGesuchChanges,
  sharedDataAccessGesuchsFeature.selectLastUpdate,
  sharedDataAccessGesuchsFeature.selectLoading,
  sharedDataAccessGesuchsFeature.selectGesuch,
  sharedDataAccessGesuchsFeature.selectGesuchFormular,
  sharedDataAccessGesuchsFeature.selectSpecificTrancheId,
  (
    config,
    { tranchenChanges },
    lastUpdate,
    loading,
    gesuch,
    gesuchFormular,
    specificTrancheId,
  ) => {
    return {
      config,
      lastUpdate,
      loading,
      gesuch,
      gesuchFormular,
      tranchenChanges,
      readonly: specificTrancheId
        ? isTrancheReadonly(
            gesuch?.gesuchTrancheToWorkWith ?? null,
            config.compileTimeConfig?.appType,
          )
        : isGesuchReadonly(gesuch, config.compileTimeConfig?.appType),
      trancheId: gesuch?.gesuchTrancheToWorkWith.id,
      specificTrancheId,
      gesuchId: gesuch?.id,
      allowTypes: config.deploymentConfig?.allowedMimeTypes?.join(','),
    };
  },
);

export const selectSharedDataAccessGesuchValidationView = createSelector(
  selectSharedDataAccessCachedGesuchChanges,
  sharedDataAccessGesuchsFeature.selectGesuchsState,
  ({ tranchenChanges }, state) => {
    const currentForm = state.gesuchFormular ?? state.cache.gesuchFormular;
    return {
      specificTrancheId: state.specificTrancheId,
      cachedGesuchId: state.cache.gesuchId,
      cachedGesuchFormular: currentForm,
      tranchenChanges,
      invalidFormularProps: {
        lastUpdate: state.lastUpdate,
        validations: {
          errors: transformValidationMessagesToFormKeys(
            state.validations?.errors,
            currentForm,
          ),
          warnings: transformValidationMessagesToFormKeys(
            state.validations?.warnings,
            currentForm,
          ),
          hasDocuments: state.validations?.hasDocuments ?? null,
        },
        specialValidationErrors: state.validations?.errors
          .filter(isSpecialValidationError)
          .map((error) => SPECIAL_VALIDATION_ERRORS[error.messageTemplate]),
      },
    };
  },
);

export const selectSharedDataAccessGesuchStepsView = createSelector(
  sharedDataAccessGesuchsFeature.selectGesuchsState,
  selectSharedDataAccessConfigsView,
  (state, config) => {
    const sharedSteps = state.steuerdatenTabs.data
      ? appendSteps(baseSteps, [
          {
            after: ELTERN,
            steps: state.steuerdatenTabs.data?.map(
              (typ) => ELTERN_STEUER_STEPS[typ],
            ),
          },
        ])
      : baseSteps;

    const steps = getStepsByAppType(sharedSteps, config?.compileTimeConfig);
    return {
      steps,
      stepsFlow: [...steps, RETURN_TO_HOME],
    };
  },
);

export const selectSharedDataAccessGesuchSteuerdatenView = createSelector(
  sharedDataAccessGesuchsFeature.selectGesuchsState,
  (state) => state.steuerdatenTabs,
);

export const selectSharedDataAccessGesuchCache = createSelector(
  sharedDataAccessGesuchsFeature.selectCache,
  sharedDataAccessGesuchsFeature.selectSpecificTrancheId,
  (cache, specificTrancheId) => ({ ...cache, specificTrancheId }),
);
export const selectSharedDataAccessGesuchCacheView = createSelector(
  selectSharedDataAccessGesuchCache,
  selectSharedDataAccessConfigsView,
  ({ specificTrancheId, ...cache }, config) => {
    return {
      cache,
      readonly: specificTrancheId
        ? isTrancheReadonly(
            cache.gesuch?.gesuchTrancheToWorkWith ?? null,
            config.compileTimeConfig?.appType,
          )
        : isGesuchReadonly(cache.gesuch, config.compileTimeConfig?.appType),
    };
  },
);

const transformValidationMessagesToFormKeys = (
  messages?: ValidationMessage[],
  currentForm?: SharedModelGesuchFormular | null,
) => {
  const formKeys: SharedModelGesuchFormularProps[] = [
    ...(Object.keys(currentForm ?? {}) as SharedModelGesuchFormularProps[]),
    'steuerdaten',
    'steuerdatenMutter',
    'steuerdatenVater',
    'dokuments',
  ];

  return messages
    ?.filter(isDefined)
    .filter(
      (m) =>
        isGesuchFormularProp(formKeys)(m.propertyPath) ||
        isSpecialValidationError(m),
    )
    .map((m) => {
      if (m.messageTemplate?.includes('documents.required')) {
        return [{ ...m, propertyPath: 'dokuments' }, { ...m }];
      }
      return m;
    })
    .flat()
    .map((m) => m.propertyPath)
    .filter(isDefined)
    .filter(isGesuchFormularProp(formKeys));
};

/**
 * Returns true if the gesuchFormular has the given property
 */
export const isGesuchFormularProp =
  (formKeys: string[]) =>
  (prop?: string): prop is SharedModelGesuchFormularProps => {
    if (!prop) return false;
    return formKeys.includes(prop);
  };

type AdditionalSteps = {
  after: SharedModelGesuchFormStep;
  steps: SharedModelGesuchFormStep[];
};
/**
 * Append additional steps after the given step
 */
const appendSteps = (
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

function getStepsByAppType(
  sharedSteps: SharedModelGesuchFormStep[],
  compileTimeConfig?: CompileTimeConfig,
) {
  switch (compileTimeConfig?.appType) {
    case 'gesuch-app':
      return [...sharedSteps, ABSCHLUSS];
    case 'sachbearbeitung-app':
      return [...sharedSteps, PROTOKOLL];
    default:
      return [];
  }
}

function prepareTranchenChanges(gesuch: SharedModelGesuch | null) {
  if (!gesuch) {
    return {
      original: null,
    };
  }
  const allChanges = gesuch.changes?.map((tranche) => {
    const changes = diff(
      tranche.gesuchFormular,
      gesuch.gesuchTrancheToWorkWith.gesuchFormular,
      {
        keysToSkip: ['id'],
      },
    );
    return {
      hasChanges: changes.length > 0,
      tranche,
      affectedSteps: [
        ...changes
          .filter((c) => (c.changes?.length ?? 0) > 0)
          .map((c) => c.key),
        ...hasSteuerdatenFamilyChange(changes),
      ],
    };
  });
  if (!allChanges || allChanges.length <= 0) {
    return {
      original: null,
    };
  }
  return {
    original: allChanges[allChanges.length - 1],
  };
}

/**
 * Used to mark steuerdatenVater/Mutter Tabs as affected if steuerdatenTyp has changed to FAMILIE
 * or back to individual
 */
const hasSteuerdatenFamilyChange = (
  changes: IChange[],
): SharedModelGesuchFormularProps[] => {
  const steuerdatenChange = changes.find(
    (c) =>
      c.key === type<SharedModelGesuchFormularProps>('steuerdaten') &&
      c.type === 'UPDATE',
  );

  // Check if steuerdaten have changed
  for (const change of steuerdatenChange?.changes ?? []) {
    const steuerdatenTypChange = change.changes?.find(
      (c) => c.key === type<keyof Steuerdaten>('steuerdatenTyp'),
    );
    // Check if steuerdatenTyp has changed
    if (steuerdatenTypChange && steuerdatenTypChange.type === 'UPDATE') {
      // If steuerdatenTyp has changed from FAMILIE to steuerdatenMutter/Vater, mark them as affected
      if (
        [steuerdatenTypChange.oldValue, steuerdatenTypChange.value].includes(
          type<SteuerdatenTyp>('FAMILIE'),
        )
      ) {
        return ['steuerdatenMutter', 'steuerdatenVater'];
      }
      // else if steuerdatenTyp has changed from steuerdatenMutter/Vater to FAMILIE, mark it as affected
      else {
        return ['steuerdaten'];
      }
    }
  }
  return [];
};
