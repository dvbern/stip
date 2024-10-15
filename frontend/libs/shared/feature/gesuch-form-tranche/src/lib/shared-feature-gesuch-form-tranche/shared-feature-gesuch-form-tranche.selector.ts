import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';

export const selectSharedFeatureGesuchFormTrancheView = createSelector(
  selectSharedDataAccessGesuchsView,
  (gesuchsView) => ({
    loading: gesuchsView.loading,
    tranche: gesuchsView.gesuch?.gesuchTrancheToWorkWith,
    gesuchId: gesuchsView.gesuch?.id,
    fallNummer: gesuchsView.gesuch?.fall.fallNummer,
    gesuchsNummer: gesuchsView.gesuch?.gesuchNummer,
    sachbearbeiter: gesuchsView.gesuch?.bearbeiter,
    lastUpdate: gesuchsView.lastUpdate,
  }),
);
