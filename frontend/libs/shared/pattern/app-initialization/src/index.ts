import { APP_INITIALIZER, NgZone } from '@angular/core';
import { Router } from '@angular/router';
import { OAuthService } from 'angular-oauth2-oidc';
import { first, switchMap, take, tap } from 'rxjs/operators';

import { SharedModelCompiletimeConfig } from '@dv/shared/model/config';
import { TenantService } from '@dv/shared/model/gesuch';
import { shouldNotAuthorizeRequestIf } from '@dv/shared/util/http';

function registerTokenReceivedHandler(
  oauthService: OAuthService,
  router: Router,
): void {
  oauthService.events.pipe(
    first((e) => e.type === 'token_received'),
    tap(() => {
      if (oauthService.state) {
        router.navigate([oauthService.state]);
      }
    }),
    take(1),
  );
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

              registerTokenReceivedHandler(oauthService, router);
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
      deps: [NgZone, TenantService, OAuthService, SharedModelCompiletimeConfig],
    },
  ];
};
