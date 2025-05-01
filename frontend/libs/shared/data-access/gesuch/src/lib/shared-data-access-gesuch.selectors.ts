import { getRouterSelectors } from '@ngrx/router-store';
import { createSelector } from '@ngrx/store';
import { IChange, diff } from 'json-diff-ts';

import { selectSharedDataAccessBenutzersView } from '@dv/shared/data-access/benutzer';
import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
import { CompileTimeConfig } from '@dv/shared/model/config';
import {
  AppTrancheChange,
  GSFormStepProps,
  GesuchTranche,
  GesuchTrancheTyp,
  GesuchUrlType,
  SBFormStepProps,
  SharedModelGesuch,
  SteuerdatenTyp,
  TRANCHE_TYPE_INITIAL,
  TrancheSetting,
} from '@dv/shared/model/gesuch';
import {
  ABSCHLUSS,
  BaseFormSteps,
  ELTERN,
  ELTERN_STEUERDATEN_STEPS,
  ELTERN_STEUERERKLAERUNG_STEPS,
  GesuchFormStep,
  RETURN_TO_HOME,
  isSteuererklaerungStep,
} from '@dv/shared/model/gesuch-form';
import { preparePermissions } from '@dv/shared/model/permission-state';
import { capitalized, lowercased } from '@dv/shared/model/type-util';

import { sharedDataAccessGesuchsFeature } from './shared-data-access-gesuch.feature';

const baseFormStepsArray = Object.values(BaseFormSteps);

const { selectRouteParam } = getRouterSelectors();

export const selectRouteId = selectRouteParam('id');

const isExistingTrancheTyp = (
  trancheTyp: string | undefined,
): trancheTyp is GesuchUrlType => {
  return (
    Object.keys(GesuchTrancheTyp).includes(trancheTyp ?? '') ||
    trancheTyp === TRANCHE_TYPE_INITIAL
  );
};
export const selectTrancheTyp = createSelector(
  selectRouteParam('trancheTyp'),
  (trancheTyp) => {
    const typ = trancheTyp?.toUpperCase();
    return isExistingTrancheTyp(typ) ? typ : undefined;
  },
);

export const selectRouteTrancheId = selectRouteParam('trancheId');

export const selectSharedDataAccessCachedGesuchChanges = createSelector(
  sharedDataAccessGesuchsFeature.selectCache,
  ({ gesuch }) => {
    return {
      tranchenChanges: prepareTranchenChanges(gesuch),
    };
  },
);

export const selectSharedGesuchAndGesuchFromular = createSelector(
  sharedDataAccessGesuchsFeature.selectGesuchsState,
  ({ gesuch, gesuchFormular }) => ({ gesuch, gesuchFormular }),
);

export const selectSharedDataAccessGesuchsView = createSelector(
  selectSharedDataAccessConfigsView,
  selectSharedDataAccessCachedGesuchChanges,
  sharedDataAccessGesuchsFeature.selectLastUpdate,
  sharedDataAccessGesuchsFeature.selectLoading,
  selectSharedGesuchAndGesuchFromular,
  sharedDataAccessGesuchsFeature.selectIsEditingAenderung,
  sharedDataAccessGesuchsFeature.selectTrancheTyp,
  selectSharedDataAccessBenutzersView,
  (
    config,
    { tranchenChanges },
    lastUpdate,
    loading,
    { gesuch, gesuchFormular },
    isEditingAenderung,
    trancheTyp,
    { rolesMap },
  ) => {
    const gesuchTranche = gesuch?.gesuchTrancheToWorkWith;
    const trancheSetting = createTrancheSetting(trancheTyp, gesuchTranche);

    return {
      config,
      lastUpdate,
      loading,
      gesuch,
      gesuchFormular,
      tranchenChanges,
      isEditingAenderung,
      ...preparePermissions(
        trancheTyp,
        gesuch,
        config.compileTimeConfig?.appType,
        rolesMap,
      ),
      trancheId: gesuch?.gesuchTrancheToWorkWith.id,
      trancheSetting,
      gesuchId: gesuch?.id,
      allowTypes: config.deploymentConfig?.allowedMimeTypes?.join(','),
    };
  },
);

export const selectSharedDataAccessGesuchTrancheSettingsView = createSelector(
  selectSharedDataAccessCachedGesuchChanges,
  sharedDataAccessGesuchsFeature.selectGesuchsState,
  ({ tranchenChanges }, state) => {
    const currentForm = state.gesuchFormular ?? state.cache.gesuchFormular;
    const cachedGesuch = state.gesuch ?? state.cache.gesuch;

    return {
      trancheSetting: createTrancheSetting(
        state.trancheTyp,
        cachedGesuch?.gesuchTrancheToWorkWith,
      ),
      cachedGesuch,
      cachedGesuchId: cachedGesuch?.id ?? null,
      cachedGesuchFormular: currentForm,
      tranchenChanges,
    };
  },
);

const createTrancheSetting = (
  gesuchUrlTyp: GesuchUrlType | null,
  gesuchTranche: GesuchTranche | undefined,
): TrancheSetting | null => {
  return gesuchTranche && gesuchUrlTyp
    ? ({
        type: gesuchTranche.typ,
        gesuchUrlTyp,
        routesSuffix: [lowercased(gesuchUrlTyp), gesuchTranche.id],
      } as const)
    : null;
};

export const selectSharedDataAccessGesuchStepsView = createSelector(
  sharedDataAccessGesuchsFeature.selectGesuchsState,
  selectSharedDataAccessConfigsView,
  (state, config) => {
    const sharedSteps = state.steuerdatenTabs.data
      ? appendSteps(baseFormStepsArray, [
          {
            after: ELTERN,
            steps: state.steuerdatenTabs.data?.map(
              (typ) => ELTERN_STEUERERKLAERUNG_STEPS[typ],
            ),
          },
        ])
      : baseFormStepsArray;

    const steps = addStepsByAppType(
      sharedSteps,
      state.steuerdatenTabs.data,
      config?.compileTimeConfig,
    );
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
  sharedDataAccessGesuchsFeature.selectIsEditingAenderung,
  sharedDataAccessGesuchsFeature.selectTrancheTyp,
  (cache, isEditingAenderung, trancheTyp) => ({
    ...cache,
    isEditingAenderung,
    trancheTyp,
  }),
);
export const selectSharedDataAccessGesuchCacheView = createSelector(
  selectSharedDataAccessGesuchCache,
  selectSharedDataAccessConfigsView,
  selectSharedDataAccessBenutzersView,
  ({ trancheTyp, ...cache }, config, { rolesMap }) => {
    return {
      cache,
      trancheTyp,
      ...preparePermissions(
        trancheTyp,
        cache.gesuch,
        config.compileTimeConfig?.appType,
        rolesMap,
      ),
    };
  },
);

/**
 * Returns true if the gesuchFormular has the given property
 */
export const isGesuchFormularProp =
  (formKeys: string[]) =>
  (prop?: string): prop is GSFormStepProps => {
    if (!prop) return false;
    return formKeys.includes(prop);
  };

type AdditionalSteps = {
  after: GesuchFormStep;
  steps: GesuchFormStep[];
};

/**
 * Append additional steps after the given step
 */
const appendSteps = (
  steps: GesuchFormStep[],
  additionalSteps: AdditionalSteps[],
) => {
  const afterMap = additionalSteps.reduce(
    (acc, { after, steps }) => {
      if (steps.length > 0) {
        acc[after.route] = steps;
      }
      return acc;
    },
    {} as Record<string, GesuchFormStep[]>,
  );
  return steps.reduce((acc, step) => {
    if (afterMap[step.route]) {
      return [...acc, step, ...afterMap[step.route]];
    }
    return [...acc, step];
  }, [] as GesuchFormStep[]);
};

function addStepsByAppType(
  sharedSteps: GesuchFormStep[],
  steuerdatenTabs: SteuerdatenTyp[] | undefined,
  compileTimeConfig?: CompileTimeConfig,
) {
  switch (compileTimeConfig?.appType) {
    case 'gesuch-app':
      return [...sharedSteps, ABSCHLUSS];
    case 'sachbearbeitung-app': {
      const steuerdatenSteps = steuerdatenTabs?.map((typ) => ({
        step: ELTERN_STEUERDATEN_STEPS[typ],
        type: typ,
      }));
      return steuerdatenSteps
        ? appendSteps(
            sharedSteps,
            steuerdatenSteps.map((s) => {
              return {
                after: ELTERN_STEUERERKLAERUNG_STEPS[s.type],
                steps: [s.step],
              };
            }),
          )
        : sharedSteps;
    }

    default:
      return [];
  }
}

/**
 * Calculates the changes between the gesuchTrancheToWorkWith and previous tranches
 *
 * Returns an object of changes for GS and SB:
 * - tranche: the tranche containing the previous gesuchFormular
 * - affectedSteps: the steps that have changed
 */
export function prepareTranchenChanges(
  gesuch: SharedModelGesuch | null,
): AppTrancheChange | null {
  if (!gesuch) {
    return null;
  }
  /**
   * Changes have Zero, one or max two tranches
   * - Zero: No changes
   * - One: GS erstellt einen Antrag. Changes should be calculated between gesuchTrancheToWorkWith and changes[0]
   * - Two: SB bearbeitet einen Antrag (As soon as he changes status). Changes should be calculated between changes[1] and gesuchTrancheToWorkWith
   */
  const allChanges = gesuch.changes?.map((tranche) => {
    const changes = diff(
      tranche.gesuchFormular,
      gesuch.gesuchTrancheToWorkWith.gesuchFormular,
      {
        keysToSkip: ['id'],
        embeddedObjKeys: {
          /** Used to have a more accurate diff for steuerdaten in {@link hasSteuererklaerungChanges} */
          ['steuererklaerung']: 'steuerdatenTyp',
        },
      },
    );
    return {
      tranche,
      affectedSteps: [
        ...changes
          .filter(
            (c) =>
              // Ignore steuerdaten changes, they are handled separately
              !isSteuererklaerungStep(
                c.key as GSFormStepProps | SBFormStepProps,
              ) &&
              ((c.changes?.length ?? 0) > 0 ||
                // Also mark the step as affected if a new entry has been added or removed
                c.type !== 'UPDATE'),
          )
          .map((c) => c.key),
        ...hasSteuererklaerungChanges(changes),
      ],
    };
  });

  if (!allChanges || allChanges.length <= 0) {
    return null;
  }

  return {
    gs: allChanges[0],
    sb: allChanges[1],
  };
}

/**
 * Used to mark steuerdatenVater/Mutter Tabs as affected if steuerdatenTyp has changed to FAMILIE
 * or back to individual
 */
export const hasSteuererklaerungChanges = (
  changes: IChange[],
): GSFormStepProps[] => {
  const steuererklaerungChange = changes.find(
    (c) =>
      isSteuererklaerungStep(c.key as GSFormStepProps | SBFormStepProps) &&
      c.type === 'UPDATE',
  );
  const affectedSteps = new Set<GSFormStepProps>();

  // Check if steuerdaten have changed
  (['MUTTER', 'VATER', 'FAMILIE'] satisfies SteuerdatenTyp[]).forEach(
    (steuerdatenTyp) => {
      const steuerdatenTypChange = steuererklaerungChange?.changes?.find(
        (c) => c.key === steuerdatenTyp,
      );
      if (
        steuerdatenTypChange &&
        ((steuerdatenTypChange.changes ?? []).length > 0 ||
          steuerdatenTypChange.type !== 'UPDATE')
      ) {
        affectedSteps.add(
          `steuererklaerung${capitalized(lowercased(steuerdatenTyp))}`,
        );
      }
    },
  );

  return Array.from(affectedSteps);
};
