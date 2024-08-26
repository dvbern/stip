import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { selectSharedDataAccessStammdatensView } from '@dv/shared/data-access/stammdaten';

export const selectSharedFeatureGesuchFormPartnerView = createSelector(
  selectSharedDataAccessGesuchsView,
  selectSharedDataAccessStammdatensView,
  (gesuchsView, stammdatenView) => ({
    loading: gesuchsView.loading || stammdatenView.loading,
    gesuch: gesuchsView.gesuch,
    gesuchId: gesuchsView.gesuchId,
    trancheId: gesuchsView.trancheId,
    allowTypes: gesuchsView.allowTypes,
    gesuchFormular: gesuchsView.gesuchFormular,
    laender: stammdatenView.laender,
    readonly: gesuchsView.readonly,
  }),
);
