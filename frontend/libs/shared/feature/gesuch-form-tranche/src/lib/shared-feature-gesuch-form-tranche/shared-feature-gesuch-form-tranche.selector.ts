import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';

export const selectSharedFeatureGesuchFormTrancheView = createSelector(
  selectSharedDataAccessGesuchsView,
  (gesuchsView) => ({
    loading: gesuchsView.loading,
    gesuchstranche: gesuchsView.gesuch?.gesuchTrancheToWorkWith,
    // position: gesuchTranche.service.ts -> getAllTranchenForGesuch -> tranchen.findIndex tranche.id
  }),
);
