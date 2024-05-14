import { createSelector } from '@ngrx/store';
import { addDays, isAfter, isBefore } from 'date-fns';

import { sharedDataAccessConfigsFeature } from '@dv/shared/data-access/config';
import { sharedDataAccessGesuchsFeature } from '@dv/shared/data-access/gesuch';
import { selectSharedDataAccessGesuchsperiodesView } from '@dv/shared/data-access/gesuchsperiode';
import { VERSION } from '@dv/shared/model/version';

export const selectGesuchAppFeatureCockpitView = createSelector(
  selectSharedDataAccessGesuchsperiodesView,
  sharedDataAccessGesuchsFeature.selectGesuchs,
  sharedDataAccessGesuchsFeature.selectLoading,
  sharedDataAccessConfigsFeature.selectDeploymentConfig,
  (gesuchsPerioden, gesuche, gesucheLoading, config) => ({
    ...gesuchsPerioden,

    gesuchsperiodes: gesuchsPerioden.gesuchsperiodes
      // TODO where should we put this logic
      .filter((p) => isBefore(Date.parse(p.aufschaltterminStart), new Date()))
      .filter((p) =>
        isAfter(Date.parse(p.gesuchsperiodeStopp), addDays(new Date(), 1)),
      )
      .map((p) => ({
        ...p,
        gesuchLoading: gesucheLoading,
        gesuchId: gesuche.find((gesuch) => p.id === gesuch.gesuchsperiode?.id)
          ?.id,
      })),
    version: {
      frontend: VERSION,
      backend: config?.version,
      sameVersion: config?.version === VERSION,
    },
  }),
);
