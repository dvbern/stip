import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { getChangesForList } from '@dv/shared/util-fn/gesuch-util';

export const selectSharedFeatureGesuchFormGeschwisterView = createSelector(
  selectSharedDataAccessGesuchsView,
  (gesuchsView) => ({
    ...gesuchsView,
    listChanges: getChangesForList(
      gesuchsView.gesuchFormular?.geschwisters,
      gesuchsView.tranchenChanges.original?.tranche.gesuchFormular
        ?.geschwisters,
      (g) => g.id,
    ),
  }),
);
