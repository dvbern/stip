import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessGesuchTrancheSettingsView } from '@dv/shared/data-access/gesuch';

export const sharedPatternGesuchStepNavView = createSelector(
  selectSharedDataAccessGesuchTrancheSettingsView,
  (gesuch) => ({ ...gesuch }),
);
