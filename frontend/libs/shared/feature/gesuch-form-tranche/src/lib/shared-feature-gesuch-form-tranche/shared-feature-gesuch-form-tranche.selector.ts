import { createSelector } from '@ngrx/store';
import { endOfDay, format, getMonth, isAfter } from 'date-fns';

import {
  selectSharedDataAccessGesuchCacheView,
  selectSharedDataAccessGesuchsView,
} from '@dv/shared/data-access/gesuch';

export const selectSharedFeatureGesuchFormTrancheView = createSelector(
  selectSharedDataAccessGesuchsView,
  selectSharedDataAccessGesuchCacheView,
  (gesuchsView, { cache }) => {
    const periode = cache.gesuch?.gesuchsperiode;
    return {
      isEditingTranche: gesuchsView.isEditingTranche,
      loading: gesuchsView.loading,
      tranche: cache.gesuch?.gesuchTrancheToWorkWith,
      periode: periode
        ? {
            semester:
              getMonth(Date.parse(periode?.gesuchsperiodeStart)) < 6
                ? ('fruehling' as const)
                : ('herbst' as const),
            year: format(Date.parse(periode?.gesuchsperiodeStart), 'yy'),
            einreichefrist: isAfter(
              new Date(),
              endOfDay(new Date(periode?.einreichefristNormal)),
            )
              ? periode?.einreichefristReduziert
              : periode?.einreichefristNormal,
          }
        : undefined,
      gesuch: cache.gesuch,
      gesuchId: cache.gesuch?.id,
      fallNummer: cache.gesuch?.fallNummer,
      gesuchsNummer: cache.gesuch?.gesuchNummer,
      sachbearbeiter: cache.gesuch?.bearbeiter,
      lastUpdate: gesuchsView.lastUpdate,
    };
  },
);
