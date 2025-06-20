import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessBenutzer } from '@dv/shared/data-access/benutzer';
import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import {
  getChangesForForm,
  selectChangeForView,
} from '@dv/shared/util-fn/gesuch-util';

export const selectSharedFeatureGesuchFormPersonView = createSelector(
  selectSharedDataAccessGesuchsView,
  selectSharedDataAccessBenutzer,
  (gesuchsView, benutzerView) => {
    const { current, previous } = selectChangeForView(
      gesuchsView,
      'personInAusbildung',
    );

    return {
      loading: gesuchsView.loading,
      gesuchId: gesuchsView.gesuchId,
      trancheId: gesuchsView.trancheId,
      formChanges: getChangesForForm(current, previous),
      allowTypes: gesuchsView.allowTypes,
      gesuch: gesuchsView.gesuch,
      gesuchFormular: gesuchsView.gesuchFormular,
      benutzerEinstellungen: {
        digitaleKommunikation:
          benutzerView?.benutzereinstellungen?.digitaleKommunikation,
      },
      readonly: gesuchsView.readonly,
      permissions: gesuchsView.permissions,
    };
  },
);
