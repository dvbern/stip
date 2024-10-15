import { createSelector } from '@ngrx/store';

import {
  selectSharedDataAccessGesuchCacheView,
  selectSharedDataAccessGesuchsView,
} from '@dv/shared/data-access/gesuch';

export const selectSharedFeatureGesuchFormTrancheView = createSelector(
  selectSharedDataAccessGesuchsView,
  selectSharedDataAccessGesuchCacheView,
  (gesuchsView, { cache }) => ({
    loading: gesuchsView.loading,
    gesuchstranche: cache.gesuch?.gesuchTrancheToWorkWith,
    gesuchId: cache.gesuch?.id,
    lastUpdate: gesuchsView.lastUpdate,
  }),
);
