import { createSelector } from '@ngrx/store';
import { format } from 'date-fns';

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
      trancheId: gesuchsView.trancheId,
      trancheSetting: gesuchsView.trancheSetting,
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
            bezeichnungDe: periode.bezeichnungDe,
            bezeichnungFr: periode.bezeichnungFr,
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
