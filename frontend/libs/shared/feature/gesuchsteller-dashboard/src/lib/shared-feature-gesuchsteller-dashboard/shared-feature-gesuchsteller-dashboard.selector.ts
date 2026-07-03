import { createSelector } from '@ngrx/store';

import { selectVersion } from '@dv/shared/data-access/config';

export const selectSharedFeatureGesuchstellerDashboardView = createSelector(
  selectVersion,
  (version) => {
    return {
      version,
    };
  },
);
