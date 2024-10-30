import { createSelector } from '@ngrx/store';
import { addMonths, compareDesc, format, startOfMonth } from 'date-fns';

import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { isDefined } from '@dv/shared/model/type-util';
import { parseDateForVariant } from '@dv/shared/util/validator-date';
import {
  getChangesForForm,
  selectChangeForView,
} from '@dv/shared/util-fn/gesuch-util';

export const selectSharedFeatureGesuchFormEducationView = createSelector(
  selectSharedDataAccessGesuchsView,
  (gesuchsView) => {
    const { current, previous } = selectChangeForView(
      gesuchsView,
      'ausbildung',
    );

    const lastLebenslaufDate = gesuchsView.gesuchFormular?.lebenslaufItems
      ?.slice()
      ?.filter((item) => !item.taetigkeitsart)
      ?.map((item) => parseDateForVariant(item.bis, new Date(), 'monthYear'))
      ?.filter(isDefined)
      ?.sort((dateA, dateB) => compareDesc(dateA, dateB))?.[0];
    const minEndDatum = startOfMonth(new Date());
    return {
      loading: gesuchsView.loading,
      gesuch: gesuchsView.gesuch,
      gesuchFormular: gesuchsView.gesuchFormular,
      formChanges: getChangesForForm(current, previous),
      minAusbildungBeginDate: lastLebenslaufDate
        ? addMonths(lastLebenslaufDate, 1)
        : undefined,
      ausbildung: gesuchsView.gesuchFormular?.ausbildung,
      minEndDatum: addMonths(minEndDatum, 1),
      minEndDatumFormatted: format(minEndDatum, 'MM.yyyy'),
      readonly: gesuchsView.readonly,
    };
  },
);
