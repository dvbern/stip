import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import {
  getChangesForList,
  selectChangeForView,
} from '@dv/shared/util-fn/gesuch-util';

// @scph rename to selectSharedFeatureFormSteuererklaerungView
export const selectSharedFeatureGesuchFormSteuerdatenView = createSelector(
  selectSharedDataAccessGesuchsView,
  (gesuchView) => {
    const { current, previous } = selectChangeForView(
      gesuchView,
      'steuererklaerung',
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
