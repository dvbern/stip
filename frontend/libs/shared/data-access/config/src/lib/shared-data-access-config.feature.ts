import { createFeature, createReducer, createSelector, on } from '@ngrx/store';

import { CompileTimeConfig } from '@dv/shared/model/config';
import { SharedModelError } from '@dv/shared/model/error';
import { DeploymentConfig } from '@dv/shared/model/gesuch';
import { VERSION } from '@dv/shared/model/version';

import { SharedDataAccessConfigEvents } from './shared-data-access-config.events';

export interface State {
  deploymentConfig: DeploymentConfig | undefined;
  compileTimeConfig: CompileTimeConfig | undefined;
  loading: boolean;
  error: SharedModelError | undefined;
}

const initialState: State = {
  deploymentConfig: undefined,
  compileTimeConfig: undefined,
  loading: false,
  error: undefined,
};

export const sharedDataAccessConfigsFeature = createFeature({
  name: 'configs',
  reducer: createReducer(
    initialState,
    on(
      SharedDataAccessConfigEvents.appInit,
      (state, { compileTimeConfig }): State => ({
        ...state,
        compileTimeConfig,
        loading: true,
        error: undefined,
      }),
    ),

    on(
      SharedDataAccessConfigEvents.deploymentConfigLoadedSuccess,
      (state, { deploymentConfig }): State => ({
        ...state,
        deploymentConfig,
        loading: false,
        error: undefined,
      }),
    ),
    on(
      SharedDataAccessConfigEvents.deploymentConfigLoadedFailure,
      // add other failure actions here (if handled the same way)
      (state, { error }): State => ({
        ...state,
        deploymentConfig: undefined,
        loading: false,
        error,
      }),
    ),
  ),
});

const selectVersion = createSelector(
  sharedDataAccessConfigsFeature.selectDeploymentConfig,
  (config) => ({
    frontend: VERSION,
    backend: config?.version,
    sameVersion: config?.version === VERSION,
  }),
);

export { selectVersion };
