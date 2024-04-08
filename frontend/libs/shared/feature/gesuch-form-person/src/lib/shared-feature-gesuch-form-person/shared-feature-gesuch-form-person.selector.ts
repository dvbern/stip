import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessBenutzersView } from '@dv/shared/data-access/benutzer';
import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { selectSharedDataAccessStammdatensView } from '@dv/shared/data-access/stammdaten';

export const selectSharedFeatureGesuchFormPersonView = createSelector(
  selectSharedDataAccessGesuchsView,
  selectSharedDataAccessBenutzersView,
  selectSharedDataAccessStammdatensView,
  (gesuchsView, benutzerView, stammdatenView) => ({
    loading: gesuchsView.loading || stammdatenView.loading,
    gesuchId: gesuchsView.gesuch?.id,
    allowTypes: gesuchsView.allowTypes,
    gesuch: gesuchsView.gesuch,
    gesuchFormular: gesuchsView.gesuchFormular,
    benutzerEinstellungen: {
      digitaleKommunikation:
        benutzerView.currentBenutzer?.benutzereinstellungen
          ?.digitaleKommunikation,
    },
    laender: stammdatenView.laender,
    readonly: gesuchsView.readonly,
  }),
);
