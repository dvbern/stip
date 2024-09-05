import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { getChangesForList } from '@dv/shared/util-fn/gesuch-util';

export const selectSharedFeatureGesuchFormSteuerdatenView = createSelector(
  selectSharedDataAccessGesuchsView,
  (gesuchView) => {
    const changes = getChangesForList(
      gesuchView.gesuchFormular?.steuerdaten,
      gesuchView.tranchenChanges.original?.tranche.gesuchFormular?.steuerdaten,
      (e) => e.steuerdatenTyp,
    );

    return {
      ...gesuchView,
      listChanges: {
        FAMILIE: changes?.changes.find((c) => c.identifier === 'FAMILIE'),
        VATER: changes?.changes.find((c) => c.identifier === 'VATER'),
        MUTTER: changes?.changes.find((c) => c.identifier === 'MUTTER'),
      },
    };
  },
);
