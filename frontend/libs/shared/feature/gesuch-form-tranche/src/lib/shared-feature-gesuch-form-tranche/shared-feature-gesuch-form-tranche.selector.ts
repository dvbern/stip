import { createSelector } from '@ngrx/store';

import {
  selectSharedDataAccessGesuchCacheView,
  selectSharedDataAccessGesuchsView,
} from '@dv/shared/data-access/gesuch';

export const selectSharedFeatureGesuchFormTrancheView = createSelector(
  selectSharedDataAccessGesuchsView,
  selectSharedDataAccessGesuchCacheView,
  (gesuchsView, { cache }) => ({
    isEditingTranche: gesuchsView.isEditingTranche,
    loading: gesuchsView.loading,
    tranche: cache.gesuch?.gesuchTrancheToWorkWith,
    gesuch: cache.gesuch,
    gesuchId: cache.gesuch?.id,
    fallNummer: cache.gesuch?.fall.fallNummer,
    gesuchsNummer: cache.gesuch?.gesuchNummer,
    sachbearbeiter: cache.gesuch?.bearbeiter,
    lastUpdate: gesuchsView.lastUpdate,
  }),
);
