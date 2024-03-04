import { getRouterSelectors } from '@ngrx/router-store';
import { createSelector } from '@ngrx/store';

import { Gesuchstatus } from '@dv/shared/model/gesuch';

import { sharedDataAccessGesuchsFeature } from './shared-data-access-gesuch.feature';

const { selectRouteParam } = getRouterSelectors();

export const selectRouteId = selectRouteParam('id');

export const selectSharedDataAccessGesuchsView = createSelector(
  sharedDataAccessGesuchsFeature.selectGesuchsState,
  (state) => {
    return {
      ...state,
      readonly: state.gesuch?.gesuchStatus === Gesuchstatus.FEHLERHAFT,
      trancheId: state.gesuch?.gesuchTrancheToWorkWith.id,
      cachedGesuchFormular: state.gesuchFormular ?? state.cache.gesuchFormular,
    };
  },
);
