import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import {
  getChangesForList,
  selectChanges,
} from '@dv/shared/util-fn/gesuch-util';

export const selectSharedFeatureGesuchFormSteuerdatenView = createSelector(
  selectSharedDataAccessGesuchsView,
  (gesuchView) => {
    const { changed, original } = selectChanges(gesuchView, 'steuerdaten');

    return {
      ...gesuchView,
      listChanges: getChangesForList(
        changed,
        original,
        (e) => e.steuerdatenTyp,
      ),
    };
  },
);
