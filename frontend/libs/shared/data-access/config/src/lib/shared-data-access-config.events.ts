import { createActionGroup, props } from '@ngrx/store';

import { CompileTimeConfig } from '@dv/shared/model/config';
import { SharedModelError } from '@dv/shared/model/error';
import { DeploymentConfig } from '@dv/shared/model/gesuch';

export const SharedDataAccessConfigEvents = createActionGroup({
  source: 'Config API',
  events: {
    appInit: props<{ compileTimeConfig: CompileTimeConfig }>(),
    deploymentConfigLoadedSuccess: props<{
      deploymentConfig: DeploymentConfig;
    }>(),
    deploymentConfigLoadedFailure: props<{ error: SharedModelError }>(),
  },
});
