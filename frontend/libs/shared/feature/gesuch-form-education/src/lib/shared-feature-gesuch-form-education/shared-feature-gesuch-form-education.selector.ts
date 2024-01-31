import { createSelector } from '@ngrx/store';
import { format, subMonths } from 'date-fns';

import { selectSharedDataAccessAusbildungsstaettesView } from '@dv/shared/data-access/ausbildungsstaette';
import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';

export const selectSharedFeatureGesuchFormEducationView = createSelector(
  selectSharedDataAccessGesuchsView,
  selectSharedDataAccessAusbildungsstaettesView,
  (gesuchsView, ausbildungsstaettesView) => {
    const gesuchsPeriodenStart = gesuchsView.gesuch
      ? new Date(gesuchsView.gesuch.gesuchsperiode.gueltigAb)
      : null;
    return {
      loading: gesuchsView.loading || ausbildungsstaettesView.loading,
      gesuch: gesuchsView.gesuch,
      gesuchFormular: gesuchsView.gesuchFormular,
      ausbildung: gesuchsView.gesuchFormular?.ausbildung,
      ausbildungsstaettes: ausbildungsstaettesView.ausbildungsstaettes,
      ausbildungsstaetteByLand:
        ausbildungsstaettesView.ausbildungsstaetteByLand,
      gesuchsPeriodenStart: gesuchsPeriodenStart
        ? subMonths(gesuchsPeriodenStart, 1)
        : null,
      gesuchsPeriodenStartFormatted: gesuchsPeriodenStart
        ? format(gesuchsPeriodenStart, 'MM.yyyy')
        : null,
      readonly: gesuchsView.readonly,
    };
  },
);
