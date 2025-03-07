import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import {
  getChangesForList,
  selectChangeForView,
} from '@dv/shared/util-fn/gesuch-util';

export const selectSharedFeatureGesuchFormGeschwisterView = createSelector(
  selectSharedDataAccessGesuchsView,
  (gesuchsView) => {
    const { current, previous } = selectChangeForView(
      gesuchsView,
      'geschwisters',
    );

    return {
      ...gesuchsView,
      listChanges: getChangesForList(current, previous),
    };
  },
);
