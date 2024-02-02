import { inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { catchError, map, switchMap } from 'rxjs';

import { ConfigurationService } from '@dv/shared/model/gesuch';
import { sharedUtilFnErrorTransformer } from '@dv/shared/util-fn/error-transformer';

import { SharedDataAccessConfigEvents } from './shared-data-access-config.events';

export const loadDeploymentConfig = createEffect(
  (
    actions$ = inject(Actions),
    configurationService = inject(ConfigurationService),
  ) => {
    return actions$.pipe(
      ofType(SharedDataAccessConfigEvents.appInit),
      switchMap(() =>
        configurationService.getDeploymentConfig$().pipe(
          map((deploymentConfig) =>
            SharedDataAccessConfigEvents.deploymentConfigLoadedSuccess({
              deploymentConfig,
            }),
          ),
          catchError((error) => [
            SharedDataAccessConfigEvents.deploymentConfigLoadedFailure({
              error: sharedUtilFnErrorTransformer(error),
            }),
          ]),
        ),
      ),
    );
  },
  { functional: true },
);

// add effects here
export const sharedDataAccessConfigEffects = { loadDeploymentConfig };
