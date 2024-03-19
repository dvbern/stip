import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessBenutzersView } from '@dv/shared/data-access/benutzer';
import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { selectSharedDataAccessStammdatensView } from '@dv/shared/data-access/stammdaten';

export const selectSharedFeatureGesuchFormPersonView = createSelector(
  selectSharedDataAccessConfigsView,
  selectSharedDataAccessGesuchsView,
  selectSharedDataAccessBenutzersView,
  selectSharedDataAccessStammdatensView,
  (config, gesuchsView, benutzerView, stammdatenView) => ({
    loading: gesuchsView.loading || stammdatenView.loading,
    gesuchId: gesuchsView.gesuch?.id,
    allowTypes: config.deploymentConfig?.allowedMimeTypes?.join(','),
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
