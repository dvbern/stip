import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { selectSharedDataAccessStammdatensView } from '@dv/shared/data-access/stammdaten';
import {
  calculateElternSituationGesuch,
  getChangesForList,
  selectChanges,
} from '@dv/shared/util-fn/gesuch-util';
import { isDefined } from '@dv/shared/util-fn/type-guards';

export const selectSharedFeatureGesuchFormElternView = createSelector(
  selectSharedDataAccessGesuchsView,
  selectSharedDataAccessStammdatensView,
  (gesuchsView, stammdatenView) => {
    const elternSituation = calculateElternSituationGesuch(
      gesuchsView.gesuchFormular,
    );

    const { changed, original } = selectChanges(gesuchsView, 'elterns');

    return {
      ...gesuchsView,
      elterns: (gesuchsView.gesuchFormular?.elterns ?? []).filter(isDefined),
      listChanges: getChangesForList(changed, original, (e) => e.elternTyp),
      expectVater: elternSituation.expectVater,
      expectMutter: elternSituation.expectMutter,
      vater: elternSituation.vater,
      mutter: elternSituation.mutter,
      laender: stammdatenView.laender,
    };
  },
);
