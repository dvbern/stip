import { HttpHandlerFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { Store } from '@ngrx/store';
import { OAuthService } from 'angular-oauth2-oidc';
import { concatMap, filter, take } from 'rxjs';

import { sharedDataAccessConfigsFeature } from '@dv/shared/data-access/config';
import { SHARED_MODEL_CONFIG_RESOURCE } from '@dv/shared/model/config';
import { UNAUTHORIZED } from '@dv/shared/util/http';

export function SharedPatternInterceptorDeploymentConfig(
  req: HttpRequest<unknown>,
  next: HttpHandlerFn,
) {
  const oauthService = inject(OAuthService);
  const store = inject(Store);

  if (
    ['/assets/', SHARED_MODEL_CONFIG_RESOURCE].some((url) =>
      req.url.includes(url),
    )
  ) {
    return next(req);
  }

  return store
    .select(sharedDataAccessConfigsFeature.selectDeploymentConfig)
    .pipe(
      filter((deploymentConfig) => deploymentConfig !== undefined),
      take(1),
    )
    .pipe(
      concatMap((deploymentConfig) => {
        const { environment = '', version = '' } = deploymentConfig ?? {};
        let headers = req.headers;
        // Do not add custom headers to requests to the keycloak server
        if (
          req.url.startsWith('http') &&
          !new URL(req.url).pathname.startsWith('/realms') &&
          !new URL(req.url).pathname.startsWith('/admin/realms')
        ) {
          headers = headers
            .append('environment', environment)
            .append('version', version);
        }
        if (!req.context.has(UNAUTHORIZED) && oauthService.hasValidIdToken()) {
          headers = headers.append(
            'Authorization',
            oauthService.authorizationHeader(),
          );
        }
        return next(req.clone({ headers }));
      }),
    );
}
