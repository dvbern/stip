import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import {
  getChangesForList,
  selectChangeForView,
} from '@dv/shared/util-fn/gesuch-util';

export const selectSharedFeatureGesuchFormSteuerdatenView = createSelector(
  selectSharedDataAccessGesuchsView,
  (gesuchView) => {
    const { current, previous } = selectChangeForView(
      gesuchView,
      'steuerdaten',
    );

    return {
      ...gesuchView,
      listChanges: getChangesForList(
        current,
        previous,
        (e) => e.steuerdatenTyp,
      ),
    };
  },
);
