import { createSelector } from '@ngrx/store';
import { addMonths, compareDesc, format, startOfMonth } from 'date-fns';

import { selectSharedDataAccessAusbildungsstaettesView } from '@dv/shared/data-access/ausbildungsstaette';
import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { parseDateForVariant } from '@dv/shared/util/validator-date';
import { getChangesForForm } from '@dv/shared/util-fn/gesuch-util';
import { isDefined } from '@dv/shared/util-fn/type-guards';

export const selectSharedFeatureGesuchFormEducationView = createSelector(
  selectSharedDataAccessGesuchsView,
  selectSharedDataAccessAusbildungsstaettesView,
  (gesuchsView, ausbildungsstaettesView) => {
    const lastLebenslaufDate = gesuchsView.gesuchFormular?.lebenslaufItems
      ?.slice()
      ?.filter((item) => !item.taetigkeitsart)
      ?.map((item) => parseDateForVariant(item.bis, new Date(), 'monthYear'))
      ?.filter(isDefined)
      ?.sort((dateA, dateB) => compareDesc(dateA, dateB))?.[0];
    const minEndDatum = startOfMonth(new Date());
    return {
      loading: gesuchsView.loading || ausbildungsstaettesView.loading,
      gesuch: gesuchsView.gesuch,
      gesuchFormular: gesuchsView.gesuchFormular,
      formChanges: getChangesForForm(
        gesuchsView.gesuchFormular?.ausbildung,
        gesuchsView.tranchenChanges.original?.tranche.gesuchFormular
          ?.ausbildung,
      ),
      minAusbildungBeginDate: lastLebenslaufDate
        ? addMonths(lastLebenslaufDate, 1)
        : undefined,
      ausbildung: gesuchsView.gesuchFormular?.ausbildung,
      ausbildungsstaettes: ausbildungsstaettesView.ausbildungsstaettes,
      minEndDatum: addMonths(minEndDatum, 1),
      minEndDatumFormatted: format(minEndDatum, 'MM.yyyy'),
      readonly: gesuchsView.readonly,
    };
  },
);
