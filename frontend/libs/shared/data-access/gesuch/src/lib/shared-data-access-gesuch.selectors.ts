import { getRouterSelectors } from '@ngrx/router-store';
import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessBenutzersView } from '@dv/shared/data-access/benutzer';
import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
import {
  GesuchTrancheTyp,
  GesuchUrlType,
  TRANCHE_TYPE_INITIAL,
} from '@dv/shared/model/gesuch';
import {
  BaseFormSteps,
  ELTERN,
  ELTERN_STEUERERKLAERUNG_STEPS,
  RETURN_TO_HOME,
} from '@dv/shared/model/gesuch-form';
import { preparePermissions } from '@dv/shared/model/permission-state';
import {
  addStepsByAppType,
  appendSteps,
  createTrancheSetting,
  prepareTranchenChanges,
} from '@dv/shared/util-fn/gesuch-util';

import { sharedDataAccessGesuchsFeature } from './shared-data-access-gesuch.feature';

const baseFormStepsArray = Object.values(BaseFormSteps);

const { selectRouteParam, selectQueryParam } = getRouterSelectors();

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
export const selectRevision = createSelector(
  selectQueryParam('revision'),
  (revision) => (revision ? +revision : undefined),
);

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

export const selectSharedDataAccessGesuchStepsView = createSelector(
  sharedDataAccessGesuchsFeature.selectGesuchsState,
  selectSharedDataAccessBenutzersView,
  selectSharedDataAccessConfigsView,
  (state, { rolesMap }, config) => {
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
      rolesMap,
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
