import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessAusbildungsstaettesView } from '@dv/shared/data-access/ausbildungsstaette';
import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { Wohnsitz } from '@dv/shared/model/gesuch';
import {
  getChangesForForm,
  selectChangeForView,
} from '@dv/shared/util-fn/gesuch-util';

export const selectSharedFeatureGesuchFormEinnahmenkostenView = createSelector(
  selectSharedDataAccessGesuchsView,
  selectSharedDataAccessAusbildungsstaettesView,
  (gesuchsView, ausbildungsstaettesView) => {
    const { current, previous } = selectChangeForView(
      gesuchsView,
      'einnahmenKosten',
    );

    return {
      loading: gesuchsView.loading,
      gesuch: gesuchsView.gesuch,
      gesuchId: gesuchsView.gesuchId,
      trancheId: gesuchsView.trancheId,
      formChanges: getChangesForForm(current, previous),
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
    };
  },
);
