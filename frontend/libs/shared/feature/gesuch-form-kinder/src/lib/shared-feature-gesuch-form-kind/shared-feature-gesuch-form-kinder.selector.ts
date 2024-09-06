import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { getChangesForList } from '@dv/shared/util-fn/gesuch-util';

export const selectSharedFeatureGesuchFormKinderView = createSelector(
  selectSharedDataAccessGesuchsView,
  (gesuchsView) => ({
    ...gesuchsView,
    listChanges: getChangesForList(
      gesuchsView.gesuchFormular?.kinds,
      gesuchsView.tranchenChanges?.tranche.gesuchFormular?.kinds,
      (g) => g.id,
    ),
  }),
);
