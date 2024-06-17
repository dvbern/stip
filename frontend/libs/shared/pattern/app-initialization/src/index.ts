import { APP_INITIALIZER } from '@angular/core';
import { Router } from '@angular/router';
import { OAuthService } from 'angular-oauth2-oidc';
import { switchMap } from 'rxjs/operators';

import { SharedModelCompiletimeConfig } from '@dv/shared/model/config';
import { TenantService } from '@dv/shared/model/gesuch';
import { shouldNotAuthorizeRequestIf } from '@dv/shared/util/http';

function goBackToPreviousUrlIfAvailable(
  oauthService: OAuthService,
  router: Router,
): void {
  const state = oauthService.state;
  if (state) {
    router.navigateByUrl(decodeURIComponent(state));
  }
}
function initializeOidc(
  router: Router,
  tenantService: TenantService,
  oauthService: OAuthService,
  compileTimeConfig: SharedModelCompiletimeConfig,
) {
  return () =>
    tenantService
      .getCurrentTenant$(undefined, undefined, {
        context: shouldNotAuthorizeRequestIf(true),
      })
      .pipe(
        switchMap(({ clientAuth }) => {
          oauthService.configure({
            issuer: `${clientAuth.authServerUrl}/realms/${clientAuth.realm}`,
            redirectUri: window.location.origin + '/',
            clientId: compileTimeConfig.authClientId,
            scope: 'openid profile email offline_access',
            responseType: 'code',
            showDebugInformation: false,
            silentRefreshRedirectUri:
              window.location.origin + '/assets/auth/silent-refresh.html',
            sessionChecksEnabled: true,
            clearHashAfterLogin: false,
            useSilentRefresh: true,
            nonceStateSeparator: 'semicolon',
          });
          return oauthService
            .loadDiscoveryDocumentAndTryLogin()
            .then((success) => {
              let nextStep: PromiseLike<unknown> = Promise.resolve(undefined);
              // perform a silent refresh when the access token is expired
              if (!oauthService.hasValidAccessToken()) {
                nextStep = oauthService.silentRefresh().catch(() => {
                  // if the silent refresh fails, redirect to the login page
                  oauthService.initLoginFlow();
                });
              }

              goBackToPreviousUrlIfAvailable(oauthService, router);
              oauthService.setupAutomaticSilentRefresh();

              return nextStep.then(() => success);
            });
        }),
      );
}

export const provideSharedPatternAppInitialization = () => {
  return [
    {
      provide: APP_INITIALIZER,
      useFactory: initializeOidc,
      multi: true,
      deps: [Router, TenantService, OAuthService, SharedModelCompiletimeConfig],
    },
  ];
};
