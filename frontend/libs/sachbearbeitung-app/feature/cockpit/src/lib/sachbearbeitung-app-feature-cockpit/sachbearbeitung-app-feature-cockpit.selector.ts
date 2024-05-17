import { createSelector } from '@ngrx/store';

import { selectVersion } from '@dv/shared/data-access/config';
import { sharedDataAccessGesuchsFeature } from '@dv/shared/data-access/gesuch';

export const selectSachbearbeitungAppFeatureCockpitView = createSelector(
  sharedDataAccessGesuchsFeature.selectGesuchs,
  sharedDataAccessGesuchsFeature.selectLoading,
  selectVersion,
  (gesuche, gesucheLoading, version) => ({
    gesuche,
    gesucheLoading,
    version,
  }),
);
