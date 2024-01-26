import { createSelector } from '@ngrx/store';
import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { selectSharedDataAccessStammdatensView } from '@dv/shared/data-access/stammdaten';
import { selectSharedDataAccessBenutzersView } from '@dv/shared/data-access/benutzer';

export const selectSharedFeatureGesuchFormEducationView = createSelector(
  selectSharedDataAccessGesuchsView,
  selectSharedDataAccessBenutzersView,
  selectSharedDataAccessStammdatensView,
  (gesuchsView, benutzerView, stammdatenView) => ({
    loading: gesuchsView.loading || stammdatenView.loading,
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
