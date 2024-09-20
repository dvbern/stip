import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { selectSharedDataAccessStammdatensView } from '@dv/shared/data-access/stammdaten';
import {
  getChangesForForm,
  selectChanges,
} from '@dv/shared/util-fn/gesuch-util';

export const selectSharedFeatureGesuchFormPartnerView = createSelector(
  selectSharedDataAccessGesuchsView,
  selectSharedDataAccessStammdatensView,
  (gesuchsView, stammdatenView) => {
    const { changed, original } = selectChanges(gesuchsView, 'partner');

    return {
      loading: gesuchsView.loading || stammdatenView.loading,
      gesuch: gesuchsView.gesuch,
      gesuchId: gesuchsView.gesuchId,
      trancheId: gesuchsView.trancheId,
      formChanges: getChangesForForm(changed, original),
      allowTypes: gesuchsView.allowTypes,
      gesuchFormular: gesuchsView.gesuchFormular,
      laender: stammdatenView.laender,
      readonly: gesuchsView.readonly,
    };
  },
);
