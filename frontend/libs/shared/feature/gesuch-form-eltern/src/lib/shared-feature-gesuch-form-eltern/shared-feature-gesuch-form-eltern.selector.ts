import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { selectSharedDataAccessStammdatensView } from '@dv/shared/data-access/stammdaten';
import { isDefined } from '@dv/shared/model/type-util';
import {
  calculateElternSituationGesuch,
  getChangesForList,
  selectChangeForView,
} from '@dv/shared/util-fn/gesuch-util';

export const selectSharedFeatureGesuchFormElternView = createSelector(
  selectSharedDataAccessGesuchsView,
  selectSharedDataAccessStammdatensView,
  (gesuchsView, stammdatenView) => {
    const elternSituation = calculateElternSituationGesuch(
      gesuchsView.gesuchFormular,
    );

    const { current, previous } = selectChangeForView(gesuchsView, 'elterns');

    return {
      ...gesuchsView,
      elterns: (gesuchsView.gesuchFormular?.elterns ?? []).filter(isDefined),
      listChanges: getChangesForList(current, previous, (e) => e.elternTyp),
      expectVater: elternSituation.expectVater,
      expectMutter: elternSituation.expectMutter,
      vater: elternSituation.vater,
      mutter: elternSituation.mutter,
      laender: stammdatenView.laender,
    };
  },
);
