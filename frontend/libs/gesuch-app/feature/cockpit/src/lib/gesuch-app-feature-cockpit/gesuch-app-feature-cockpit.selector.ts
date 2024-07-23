import { createSelector } from '@ngrx/store';

import { selectVersion } from '@dv/shared/data-access/config';
import { sharedDataAccessGesuchsFeature } from '@dv/shared/data-access/gesuch';
import { selectSharedDataAccessGesuchsperiodesView } from '@dv/shared/data-access/gesuchsperiode';

export const selectGesuchAppFeatureCockpitView = createSelector(
  selectSharedDataAccessGesuchsperiodesView,
  sharedDataAccessGesuchsFeature.selectGesuchs,
  sharedDataAccessGesuchsFeature.selectLoading,
  selectVersion,
  (gesuchsPerioden, gesuche, gesucheLoading, version) => ({
    ...gesuchsPerioden,

    gesuchsperiodes: gesuchsPerioden.gesuchsperiodes
      .filter((p) => p.erfassbar)
      .map((p) => ({
        ...p,
        gesuchLoading: gesucheLoading,
        gesuch: gesuche.find((gesuch) => p.id === gesuch.gesuchsperiode?.id),
      })),
    version,
  }),
);
