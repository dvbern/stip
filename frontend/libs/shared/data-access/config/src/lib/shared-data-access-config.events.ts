import { createActionGroup, props } from '@ngrx/store';

import { AppConfig } from '@dv/shared/model/config';
import { SharedModelError } from '@dv/shared/model/error';
import { DeploymentConfig } from '@dv/shared/model/gesuch';

export const SharedDataAccessConfigEvents = createActionGroup({
  source: 'Config API',
  events: {
    appInit: props<{ appConfig: AppConfig }>(),
    deploymentConfigLoadedSuccess: props<{
      deploymentConfig: DeploymentConfig;
    }>(),
    deploymentConfigLoadedFailure: props<{ error: SharedModelError }>(),
  },
});
