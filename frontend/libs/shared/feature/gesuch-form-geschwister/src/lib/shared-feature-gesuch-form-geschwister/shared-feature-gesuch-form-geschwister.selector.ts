import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import {
  getChangesForList,
  selectChanges,
} from '@dv/shared/util-fn/gesuch-util';

export const selectSharedFeatureGesuchFormGeschwisterView = createSelector(
  selectSharedDataAccessGesuchsView,
  (gesuchsView) => {
    const { changed, original } = selectChanges(gesuchsView, 'geschwisters');

    return {
      ...gesuchsView,
      listChanges: getChangesForList(changed, original, (g) => g.id),
    };
  },
);
