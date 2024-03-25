import { createSelector } from '@ngrx/store';
import { addMonths, compareDesc, format, startOfMonth } from 'date-fns';

import { selectSharedDataAccessAusbildungsstaettesView } from '@dv/shared/data-access/ausbildungsstaette';
import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { parseDateForVariant } from '@dv/shared/util/validator-date';
import { isDefined } from '@dv/shared/util-fn/type-guards';

export const selectSharedFeatureGesuchFormEducationView = createSelector(
  selectSharedDataAccessConfigsView,
  selectSharedDataAccessGesuchsView,
  selectSharedDataAccessAusbildungsstaettesView,
  (config, gesuchsView, ausbildungsstaettesView) => {
    const lastLebenslaufDate = gesuchsView.gesuchFormular?.lebenslaufItems
      ?.slice()
      ?.filter((item) => !item.taetigskeitsart)
      ?.map((item) => parseDateForVariant(item.bis, new Date(), 'monthYear'))
      ?.filter(isDefined)
      ?.sort((dateA, dateB) => compareDesc(dateA, dateB))?.[0];
    const minEndDatum = startOfMonth(new Date());
    return {
      loading: gesuchsView.loading || ausbildungsstaettesView.loading,
      gesuch: gesuchsView.gesuch,
      gesuchId: gesuchsView.gesuch?.id,
      allowTypes: config.deploymentConfig?.allowedMimeTypes?.join(','),
      gesuchFormular: gesuchsView.gesuchFormular,
      minAusbildungBeginDate: lastLebenslaufDate
        ? addMonths(lastLebenslaufDate, 1)
        : undefined,
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
