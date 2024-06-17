import { createSelector } from '@ngrx/store';

import { sharedDataAccessBenutzersFeature } from './shared-data-access-benutzer.feature';

export const selectSharedDataAccessBenutzersView = createSelector(
  sharedDataAccessBenutzersFeature.selectBenutzersState,
  (state) => ({ ...state }),
);

export const selectSharedDataAccessBenutzer = createSelector(
  sharedDataAccessBenutzersFeature.selectCurrentBenutzerRd,
  (benutzerRd) => benutzerRd?.data,
);
