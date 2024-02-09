import { createSelector } from '@ngrx/store';
import { addMonths, format, startOfMonth } from 'date-fns';

import { selectSharedDataAccessAusbildungsstaettesView } from '@dv/shared/data-access/ausbildungsstaette';
import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';

export const selectSharedFeatureGesuchFormEducationView = createSelector(
  selectSharedDataAccessGesuchsView,
  selectSharedDataAccessAusbildungsstaettesView,
  (gesuchsView, ausbildungsstaettesView) => {
    const minEndDatum = startOfMonth(new Date());
    return {
      loading: gesuchsView.loading || ausbildungsstaettesView.loading,
      gesuch: gesuchsView.gesuch,
      gesuchFormular: gesuchsView.gesuchFormular,
      ausbildung: gesuchsView.gesuchFormular?.ausbildung,
      ausbildungsstaettes: ausbildungsstaettesView.ausbildungsstaettes,
      ausbildungsstaetteByLand:
        ausbildungsstaettesView.ausbildungsstaetteByLand,
      minEndDatum: addMonths(minEndDatum, 1),
      minEndDatumFormatted: format(minEndDatum, 'MM.yyyy'),
      readonly: gesuchsView.readonly,
    };
  },
);
