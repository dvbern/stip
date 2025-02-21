import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import {
  getChangesForForm,
  selectChangeForView,
} from '@dv/shared/util-fn/gesuch-util';

export const selectSharedFeatureGesuchFormDarlehenView = createSelector(
  selectSharedDataAccessGesuchsView,
  (gesuchsView) => {
    const { current, previous } = selectChangeForView(gesuchsView, 'darlehen');

    return {
      loading: gesuchsView.loading,
      gesuch: gesuchsView.gesuch,
      gesuchId: gesuchsView.gesuchId,
      trancheId: gesuchsView.trancheId,
      formChanges: getChangesForForm(current, previous),
      allowTypes: gesuchsView.allowTypes,
      gesuchFormular: gesuchsView.gesuchFormular,
      darlehen: gesuchsView.gesuchFormular?.darlehen,
      readonly: gesuchsView.readonly,
      permissions: gesuchsView.permissions,
    };
  },
);
