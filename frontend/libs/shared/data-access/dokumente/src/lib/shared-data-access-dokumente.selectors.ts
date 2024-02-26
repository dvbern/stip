import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';

import { sharedDataAccessDokumentesFeature } from './shared-data-access-dokumente.feature';

export const selectSharedDataAccessDokumentesView = createSelector(
  sharedDataAccessDokumentesFeature.selectDokumentesState,
  selectSharedDataAccessGesuchsView,
  (state, gesuch) => ({ ...state, gesuchId: gesuch.gesuch?.id }),
);
