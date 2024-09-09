import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessBenutzer } from '@dv/shared/data-access/benutzer';
import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { selectSharedDataAccessStammdatensView } from '@dv/shared/data-access/stammdaten';
import { getChangesForForm } from '@dv/shared/util-fn/gesuch-util';

export const selectSharedFeatureGesuchFormPersonView = createSelector(
  selectSharedDataAccessGesuchsView,
  selectSharedDataAccessBenutzer,
  selectSharedDataAccessStammdatensView,
  (gesuchsView, benutzerView, stammdatenView) => ({
    loading: gesuchsView.loading || stammdatenView.loading,
    gesuchId: gesuchsView.gesuchId,
    trancheId: gesuchsView.trancheId,
    formChanges: getChangesForForm(
      gesuchsView.gesuchFormular?.personInAusbildung,
      gesuchsView.tranchenChanges?.tranche.gesuchFormular?.personInAusbildung,
    ),
    allowTypes: gesuchsView.allowTypes,
    gesuch: gesuchsView.gesuch,
    gesuchFormular: gesuchsView.gesuchFormular,
    benutzerEinstellungen: {
      digitaleKommunikation:
        benutzerView?.benutzereinstellungen?.digitaleKommunikation,
    },
    laender: stammdatenView.laender,
    readonly: gesuchsView.readonly,
  }),
);
