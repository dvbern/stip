import { getRouterSelectors } from '@ngrx/router-store';
import { createSelector } from '@ngrx/store';
import { IChange, diff } from 'json-diff-ts';

import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
import { CompileTimeConfig } from '@dv/shared/model/config';
import {
  SharedModelGesuch,
  SharedModelGesuchFormular,
  SharedModelGesuchFormularProps,
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
import { capitalized } from '@dv/shared/util-fn/string-helper';
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
      return [...sharedSteps];
    default:
      return [];
  }
}
/**
 * Calculates the changes between the gesuchTrancheToWorkWith and previous tranches
 *
 * Returns:
 * - hasChanges: true if there are changes
 * - tranche: the tranche containing the previous gesuchFormular
 * - affectedSteps: the steps that have changed
 */
export function prepareTranchenChanges(gesuch: SharedModelGesuch | null) {
  if (!gesuch) {
    return null;
  }
  const allChanges = gesuch.changes?.map((tranche) => {
    const changes = diff(
      gesuch.gesuchTrancheToWorkWith.gesuchFormular,
      tranche.gesuchFormular,
      {
        keysToSkip: ['id'],
        embeddedObjKeys: {
          /** Used to have a more accurate diff for steuerdaten in {@link hasSteuerdatenChanges} */
          ['steuerdaten']: 'steuerdatenTyp',
        },
      },
    );
    return {
      hasChanges: changes.length > 0,
      tranche,
      affectedSteps: [
        ...changes
          .filter(
            (c) =>
              // Ignore steuerdaten changes, they are handled separately
              c.key !== type<SharedModelGesuchFormularProps>('steuerdaten') &&
              ((c.changes?.length ?? 0) > 0 ||
                // Also mark the step as affected if a new entry has been added or removed
                c.type !== 'UPDATE'),
          )
          .map((c) => c.key),
        ...hasSteuerdatenChanges(changes),
      ],
    };
  });
  if (!allChanges || allChanges.length <= 0) {
    return null;
  }
  return allChanges[allChanges.length - 1];
}

/**
 * Used to mark steuerdatenVater/Mutter Tabs as affected if steuerdatenTyp has changed to FAMILIE
 * or back to individual
 */
export const hasSteuerdatenChanges = (
  changes: IChange[],
): SharedModelGesuchFormularProps[] => {
  const steuerdatenChange = changes.find(
    (c) =>
      c.key === type<SharedModelGesuchFormularProps>('steuerdaten') &&
      c.type === 'UPDATE',
  );
  const affectedSteps = new Set<SharedModelGesuchFormularProps>();

  // Check if steuerdaten have changed
  (['MUTTER', 'VATER', 'FAMILIE'] satisfies SteuerdatenTyp[]).forEach(
    (steuerdatenTyp) => {
      const steuerdatenTypChange = steuerdatenChange?.changes?.find(
        (c) => c.key === steuerdatenTyp,
      );
      if (
        steuerdatenTypChange &&
        ((steuerdatenTypChange.changes ?? []).length > 0 ||
          steuerdatenTypChange.type !== 'UPDATE')
      ) {
        affectedSteps.add(
          `steuerdaten${steuerdatenTyp === 'FAMILIE' ? '' : capitalized(steuerdatenTyp)}`,
        );
      }
    },
  );

  return Array.from(affectedSteps);
};
