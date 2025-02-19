import { createSelector } from '@ngrx/store';
import { format, getMonth } from 'date-fns';

import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
import {
  selectSharedDataAccessGesuchCacheView,
  selectSharedDataAccessGesuchsView,
} from '@dv/shared/data-access/gesuch';

export const selectSharedFeatureGesuchFormTrancheView = createSelector(
  selectSharedDataAccessGesuchsView,
  selectSharedDataAccessGesuchCacheView,
  selectSharedDataAccessConfigsView,
  (gesuchsView, { cache }, config) => {
    const periode = cache.gesuch?.gesuchsperiode;
    const einreichedatum = cache.gesuch?.einreichedatum;
    const minEinreichedatum = cache.gesuch?.gesuchTrancheToWorkWith?.gueltigAb;
    return {
      isEditingAenderung: gesuchsView.isEditingAenderung,
      loading: gesuchsView.loading,
      tranche: cache.gesuch?.gesuchTrancheToWorkWith,
      einreichedatum:
        minEinreichedatum && einreichedatum
          ? {
              current: einreichedatum,
              min: minEinreichedatum,
              max: new Date().toISOString(),
            }
          : undefined,
      periode: periode
        ? {
            semester:
              getMonth(Date.parse(periode?.gesuchsperiodeStart)) < 6
                ? ('fruehling' as const)
                : ('herbst' as const),
            year: format(Date.parse(periode?.gesuchsperiodeStart), 'yy'),
            einreichefrist: periode?.einreichefristNormal,
          }
        : undefined,
      gesuch: cache.gesuch,
      gesuchId: cache.gesuch?.id,
      fallNummer: cache.gesuch?.fallNummer,
      gesuchsNummer: cache.gesuch?.gesuchNummer,
      sachbearbeiter: cache.gesuch?.bearbeiter,
      lastUpdate: gesuchsView.lastUpdate,
      appType: config.compileTimeConfig?.appType,
    };
  },
);
