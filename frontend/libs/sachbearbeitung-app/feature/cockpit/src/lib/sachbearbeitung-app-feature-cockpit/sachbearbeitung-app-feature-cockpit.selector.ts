import { createSelector } from '@ngrx/store';

import { sharedDataAccessConfigsFeature } from '@dv/shared/data-access/config';
import { sharedDataAccessGesuchsFeature } from '@dv/shared/data-access/gesuch';
import { VERSION } from '@dv/shared/model/version';

export const selectSachbearbeitungAppFeatureCockpitView = createSelector(
  sharedDataAccessGesuchsFeature.selectGesuchs,
  sharedDataAccessGesuchsFeature.selectLoading,
  sharedDataAccessConfigsFeature.selectDeploymentConfig,
  (gesuche, gesucheLoading, config) => ({
    gesuche,
    gesucheLoading,
    version: {
      frontend: VERSION,
      backend: config?.version,
      sameVersion: config?.version === VERSION,
    },
  }),
);
