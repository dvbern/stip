import { createSelector } from '@ngrx/store';
import { addMonths, compareDesc, format, subMonths } from 'date-fns';

import { selectSharedDataAccessAusbildungsstaettesView } from '@dv/shared/data-access/ausbildungsstaette';
import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { parseDateForVariant } from '@dv/shared/util/validator-date';
import { sharedUtilFnTypeGuardsIsDefined } from '@dv/shared/util-fn/type-guards';

export const selectSharedFeatureGesuchFormEducationView = createSelector(
  selectSharedDataAccessGesuchsView,
  selectSharedDataAccessAusbildungsstaettesView,
  (gesuchsView, ausbildungsstaettesView) => {
    const gesuchsPeriodenStart = gesuchsView.gesuch
      ? new Date(gesuchsView.gesuch.gesuchsperiode.gueltigAb)
      : null;
    const lastLebenslaufDate = gesuchsView.gesuchFormular?.lebenslaufItems
      ?.slice()
      ?.filter((item) => !item.taetigskeitsart)
      ?.map((item) => parseDateForVariant(item.bis, new Date(), 'monthYear'))
      ?.filter(sharedUtilFnTypeGuardsIsDefined)
      ?.sort((dateA, dateB) => compareDesc(dateA, dateB))?.[0];
    return {
      loading: gesuchsView.loading || ausbildungsstaettesView.loading,
      gesuch: gesuchsView.gesuch,
      gesuchFormular: gesuchsView.gesuchFormular,
      minAusbildungBeginDate: lastLebenslaufDate
        ? addMonths(lastLebenslaufDate, 1)
        : undefined,
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
