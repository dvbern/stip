import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import {
  getChangesForList,
  selectChangeForView,
} from '@dv/shared/util-fn/gesuch-util';

export const selectSharedFeatureGesuchFormKinderView = createSelector(
  selectSharedDataAccessGesuchsView,
  (gesuchsView) => {
    const { current, previous } = selectChangeForView(gesuchsView, 'kinds');

    return {
      ...gesuchsView,
      kinds: [...(gesuchsView.gesuchFormular?.kinds ?? [])].sort(
        (a, b) =>
          a.nachname.localeCompare(b.nachname) ||
          a.vorname.localeCompare(b.vorname) ||
          a.geburtsdatum.localeCompare(b.geburtsdatum),
      ),
      listChanges: getChangesForList(
        current,
        previous,
        (e) => e.entryId,
        'entryId',
      ),
    };
  },
);
