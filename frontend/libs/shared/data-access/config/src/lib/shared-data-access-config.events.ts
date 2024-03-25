import { createActionGroup, emptyProps, props } from '@ngrx/store';

import { SharedModelError } from '@dv/shared/model/error';
import { DeploymentConfig } from '@dv/shared/model/gesuch';

export const SharedDataAccessConfigEvents = createActionGroup({
  source: 'Config API',
  events: {
    appInit: emptyProps(),
    deploymentConfigLoadedSuccess: props<{
      deploymentConfig: DeploymentConfig;
    }>(),
    deploymentConfigLoadedFailure: props<{ error: SharedModelError }>(),
  },
});
