import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { selectSharedDataAccessStammdatensView } from '@dv/shared/data-access/stammdaten';
import { calculateElternSituationGesuch } from '@dv/shared/util-fn/gesuch-util';
import { sharedUtilFnTypeGuardsIsDefined } from '@dv/shared/util-fn/type-guards';

export const selectSharedFeatureGesuchFormElternView = createSelector(
  selectSharedDataAccessGesuchsView,
  selectSharedDataAccessStammdatensView,
  (gesuchView, stammdatenView) => {
    const elternSituation = calculateElternSituationGesuch(
      gesuchView.gesuchFormular,
    );

    return {
      ...gesuchView,
      elterns: (gesuchView.gesuchFormular?.elterns ?? []).filter(
        sharedUtilFnTypeGuardsIsDefined,
      ),
      expectVater: elternSituation.expectVater,
      expectMutter: elternSituation.expectMutter,
      vater: elternSituation.vater,
      mutter: elternSituation.mutter,
      laender: stammdatenView.laender,
    };
  },
);
