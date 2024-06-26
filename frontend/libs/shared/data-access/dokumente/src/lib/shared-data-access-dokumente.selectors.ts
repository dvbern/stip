import { createSelector } from '@ngrx/store';

import { sharedDataAccessDokumentesFeature } from './shared-data-access-dokumente.feature';

export const selectSharedDataAccessDokumentesView = createSelector(
  sharedDataAccessDokumentesFeature.selectDokumentesState,
  (state) => state,
);
