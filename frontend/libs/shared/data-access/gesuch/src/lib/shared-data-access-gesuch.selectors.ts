import { getRouterSelectors } from '@ngrx/router-store';
import { createSelector } from '@ngrx/store';

import {
  Gesuchstatus,
  SharedModelGesuchFormular,
  SharedModelGesuchFormularProps,
} from '@dv/shared/model/gesuch';
import { isDefined } from '@dv/shared/util-fn/type-guards';

import { sharedDataAccessGesuchsFeature } from './shared-data-access-gesuch.feature';

const { selectRouteParam } = getRouterSelectors();

export const selectRouteId = selectRouteParam('id');

export const selectSharedDataAccessGesuchsView = createSelector(
  sharedDataAccessGesuchsFeature.selectGesuchsState,
  (state) => {
    const currentForm = state.gesuchFormular ?? state.cache.gesuchFormular;
    return {
      ...state,
      readonly: state.gesuch?.gesuchStatus === Gesuchstatus.FEHLERHAFT,
      trancheId: state.gesuch?.gesuchTrancheToWorkWith.id,
      cachedGesuchFormular: currentForm,
      invalidFormularProps: {
        lastUpdate: state.lastUpdate,
        validations: (state.validations ?? [])
          .map(getFormPropertyFromPath)
          .filter(isDefined)
          .filter(isFormularProp(currentForm)),
      },
    };
  },
);

/**
 * Returns true if the gesuchFormular has the given property
 */
export const isFormularProp =
  (gesuchFormular: SharedModelGesuchFormular | null) =>
  (prop: string): prop is SharedModelGesuchFormularProps => {
    if (!gesuchFormular) return false;
    return Object.keys(gesuchFormular).includes(prop);
  };

/**
 * Tries to get the form property from the given validation error
 */
export const getFormPropertyFromPath = (validation: {
  propertyPath?: string;
}) => {
  return validation.propertyPath?.split('.')[1];
};
