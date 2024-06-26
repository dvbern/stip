import { createSelector } from '@ngrx/store';

import { sharedDataAccessConfigsFeature } from './shared-data-access-config.feature';

export const selectSharedDataAccessConfigsView = createSelector(
  sharedDataAccessConfigsFeature.selectConfigsState,
  (state) => ({
    ...state,
    isSachbearbeitungApp:
      state.compileTimeConfig?.appType === 'sachbearbeitung-app',
    isGesuchApp: state.compileTimeConfig?.appType === 'gesuch-app',
  }),
);
