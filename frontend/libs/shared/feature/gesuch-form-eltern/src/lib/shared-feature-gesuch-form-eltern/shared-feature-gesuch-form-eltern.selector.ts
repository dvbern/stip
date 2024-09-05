import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { selectSharedDataAccessStammdatensView } from '@dv/shared/data-access/stammdaten';
import {
  calculateElternSituationGesuch,
  getChangesForList,
} from '@dv/shared/util-fn/gesuch-util';
import { isDefined } from '@dv/shared/util-fn/type-guards';

export const selectSharedFeatureGesuchFormElternView = createSelector(
  selectSharedDataAccessGesuchsView,
  selectSharedDataAccessStammdatensView,
  (gesuchView, stammdatenView) => {
    const elternSituation = calculateElternSituationGesuch(
      gesuchView.gesuchFormular,
    );
    const changes = getChangesForList(
      gesuchView.gesuchFormular?.elterns,
      gesuchView.tranchenChanges.original?.tranche.gesuchFormular?.elterns,
      (e) => e.elternTyp,
    );

    return {
      ...gesuchView,
      elterns: (gesuchView.gesuchFormular?.elterns ?? []).filter(isDefined),
      listChanges: {
        VATER: changes?.changes.find((c) => c.identifier === 'VATER'),
        MUTTER: changes?.changes.find((c) => c.identifier === 'MUTTER'),
      },
      expectVater: elternSituation.expectVater,
      expectMutter: elternSituation.expectMutter,
      vater: elternSituation.vater,
      mutter: elternSituation.mutter,
      laender: stammdatenView.laender,
    };
  },
);
