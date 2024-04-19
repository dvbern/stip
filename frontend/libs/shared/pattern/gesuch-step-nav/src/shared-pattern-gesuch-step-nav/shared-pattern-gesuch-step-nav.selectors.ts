import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessGesuchValidationView } from '@dv/shared/data-access/gesuch';

export const sharedPatternGesuchStepNavView = createSelector(
  selectSharedDataAccessGesuchValidationView,
  (gesuch) => ({ ...gesuch }),
);
