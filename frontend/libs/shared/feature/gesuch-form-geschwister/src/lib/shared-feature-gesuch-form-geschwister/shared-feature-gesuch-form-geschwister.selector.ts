import { createSelector } from '@ngrx/store';

import {
  selectSharedDataAccessGesuchCacheView,
  selectSharedDataAccessGesuchsView,
} from '@dv/shared/data-access/gesuch';
import {
  getChangesForList,
  selectChangeForView,
} from '@dv/shared/util-fn/gesuch-util';

export const selectSharedFeatureGesuchFormGeschwisterView = createSelector(
  selectSharedDataAccessGesuchsView,
  selectSharedDataAccessGesuchCacheView,
  (gesuchsView, { cache }) => {
    const { current, previous } = selectChangeForView(
      gesuchsView,
      'geschwisters',
    );

    return {
      ...gesuchsView,
      geschwisters: [...(cache.gesuchFormular?.geschwisters ?? [])].sort(
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
