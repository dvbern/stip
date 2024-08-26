import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessAusbildungsstaettesView } from '@dv/shared/data-access/ausbildungsstaette';
import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { Wohnsitz } from '@dv/shared/model/gesuch';

export const selectSharedFeatureGesuchFormEinnahmenkostenView = createSelector(
  selectSharedDataAccessGesuchsView,
  selectSharedDataAccessAusbildungsstaettesView,
  (gesuchsView, ausbildungsstaettesView) => ({
    loading: gesuchsView.loading,
    gesuch: gesuchsView.gesuch,
    gesuchId: gesuchsView.gesuchId,
    trancheId: gesuchsView.trancheId,
    allowTypes: gesuchsView.allowTypes,
    ausbildungsstaettes: ausbildungsstaettesView.ausbildungsstaettes,
    gesuchFormular: gesuchsView.gesuchFormular,
    einnahmenKosten: gesuchsView.gesuchFormular?.einnahmenKosten,
    wohnsitzNotEigenerHaushalt:
      gesuchsView.gesuchFormular?.personInAusbildung?.wohnsitz !==
      Wohnsitz.EIGENER_HAUSHALT,
    existiertGerichtlicheAlimentenregelung:
      gesuchsView.gesuchFormular?.familiensituation
        ?.gerichtlicheAlimentenregelung === true,
    readonly: gesuchsView.readonly,
  }),
);
