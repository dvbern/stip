import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { getChangesForList } from '@dv/shared/util-fn/gesuch-util';

export const selectSharedFeatureGesuchFormSteuerdatenView = createSelector(
  selectSharedDataAccessGesuchsView,
  (gesuchView) => {
    return {
      ...gesuchView,
      listChanges: getChangesForList(
        gesuchView.gesuchFormular?.steuerdaten,
        gesuchView.tranchenChanges?.tranche.gesuchFormular?.steuerdaten,
        (e) => e.steuerdatenTyp,
      ),
    };
  },
);
