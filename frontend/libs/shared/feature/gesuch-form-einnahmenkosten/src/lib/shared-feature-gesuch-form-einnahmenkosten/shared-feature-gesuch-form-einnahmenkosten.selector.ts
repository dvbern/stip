import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { Wohnsitz } from '@dv/shared/model/gesuch';
import {
  getChangesForForm,
  selectChangeForView,
} from '@dv/shared/util-fn/gesuch-util';

export const selectSharedFeatureGesuchFormEinnahmenkostenView = createSelector(
  selectSharedDataAccessGesuchsView,
  (gesuchsView) => {
    const { current, previous } = selectChangeForView(
      gesuchsView,
      'einnahmenKosten',
    );

    const { current: currentPartner, previous: previousPartner } =
      selectChangeForView(gesuchsView, 'einnahmenKostenPartner');

    return {
      loading: gesuchsView.loading,
      gesuch: gesuchsView.gesuch,
      gesuchId: gesuchsView.gesuchId,
      trancheId: gesuchsView.trancheId,
      formChanges: getChangesForForm(current, previous),
      formChangesPartner: getChangesForForm(currentPartner, previousPartner),
      allowTypes: gesuchsView.allowTypes,
      gesuchFormular: gesuchsView.gesuchFormular,
      einnahmenKosten: gesuchsView.gesuchFormular?.einnahmenKosten,
      einnahmenKostenPartner:
        gesuchsView.gesuchFormular?.einnahmenKostenPartner,
      wohnsitzNotEigenerHaushalt:
        gesuchsView.gesuchFormular?.personInAusbildung?.wohnsitz !==
        Wohnsitz.EIGENER_HAUSHALT,
      readonly: gesuchsView.readonly,
      permissions: gesuchsView.permissions,
    };
  },
);
