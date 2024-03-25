import { getRouterSelectors } from '@ngrx/router-store';
import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
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
  selectSharedDataAccessConfigsView,
  sharedDataAccessGesuchsFeature.selectGesuchsState,
  (config, state) => {
    const currentForm = state.gesuchFormular ?? state.cache.gesuchFormular;
    return {
      ...state,
      readonly: state.gesuch?.gesuchStatus === Gesuchstatus.FEHLERHAFT,
      trancheId: state.gesuch?.gesuchTrancheToWorkWith.id,
      gesuchId: state.gesuch?.id,
      allowTypes: config.deploymentConfig?.allowedMimeTypes?.join(','),
      cachedGesuchFormular: currentForm,
      invalidFormularProps: {
        lastUpdate: state.lastUpdate,
        validations: (state.validations ?? [])
          .map((v) => v.propertyPath)
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
