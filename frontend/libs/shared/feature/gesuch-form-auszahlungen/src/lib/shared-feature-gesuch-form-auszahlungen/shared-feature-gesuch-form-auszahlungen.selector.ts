import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { selectSharedDataAccessStammdatensView } from '@dv/shared/data-access/stammdaten';
import {
  Kontoinhaber,
  SharedModelGesuchFormular,
} from '@dv/shared/model/gesuch';
import {
  ElternSituation,
  calculateElternSituationGesuch,
  getChangesForForm,
} from '@dv/shared/util-fn/gesuch-util';

export const selectSharedFeatureGesuchFormAuszahlungenView = createSelector(
  selectSharedDataAccessGesuchsView,
  selectSharedDataAccessStammdatensView,
  (gesuchsView, stammdatenView) => {
    return {
      loading: gesuchsView.loading || stammdatenView.loading,
      gesuch: gesuchsView.gesuch,
      allowTypes: gesuchsView.allowTypes,
      gesuchId: gesuchsView.gesuchId,
      trancheId: gesuchsView.trancheId,
      formChanges: getChangesForForm(
        gesuchsView.gesuchFormular?.auszahlung,
        gesuchsView.tranchenChanges?.tranche.gesuchFormular?.auszahlung,
      ),
      gesuchFormular: gesuchsView.gesuchFormular,
      laender: stammdatenView.laender,
      kontoinhaberValues: calculateKontoinhaberValuesGesuch(
        gesuchsView.gesuchFormular,
      ),
      hasNecessaryPreSteps: calculateHasNecessaryPreStepsGesuch(
        gesuchsView.gesuchFormular,
      ),
      readonly: gesuchsView.readonly,
    };
  },
);

function calculateHasNecessaryPreStepsGesuch(
  formular: SharedModelGesuchFormular | null,
) {
  if (!formular?.familiensituation) {
    return false;
  }
  const elternSituation = calculateElternSituationGesuch(formular);
  return calculateHasNecessaryPreSteps(elternSituation);
}
export function calculateHasNecessaryPreSteps(
  elternSituation: ElternSituation,
) {
  if (elternSituation.expectMutter && !elternSituation.mutter) {
    return false;
  }
  if (elternSituation.expectVater && !elternSituation.vater) {
    return false;
  }
  return true;
}

function calculateKontoinhaberValuesGesuch(
  formular: SharedModelGesuchFormular | null,
) {
  const elternSituation = calculateElternSituationGesuch(formular);
  return calculateKontoinhaberValues(elternSituation);
}
export function calculateKontoinhaberValues(elternSituation: ElternSituation) {
  let kontoinhaberValues = Object.values(Kontoinhaber);

  if (!elternSituation.expectVater) {
    kontoinhaberValues = kontoinhaberValues.filter((each) => each !== 'VATER');
  }
  if (!elternSituation.expectMutter) {
    kontoinhaberValues = kontoinhaberValues.filter((each) => each !== 'MUTTER');
  }
  return kontoinhaberValues;
}
