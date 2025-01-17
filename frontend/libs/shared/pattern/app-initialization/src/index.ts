import { APP_INITIALIZER, NgZone } from '@angular/core';
import { Router } from '@angular/router';
import { OAuthService } from 'angular-oauth2-oidc';
import { lastValueFrom, of } from 'rxjs';
import { delay, switchMap, tap } from 'rxjs/operators';

import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
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

const TIME_TO_WAIT_BEFORE_RELOAD = 5000;
function initializeOidc(
  router: Router,
  tenantService: TenantService,
  oauthService: OAuthService,
  compileTimeConfig: SharedModelCompileTimeConfig,
  zone: NgZone,
) {
  return () =>
    lastValueFrom(
      tenantService
        .getCurrentTenant$(undefined, undefined, {
          context: shouldNotAuthorizeRequestIf(true),
        })
        .pipe(
          switchMap(({ clientAuth }) => {
            // It is important to run the configuration outside of the Angular zone
            // to prevent Angular Application Ref from always switching between ready and not ready
            return zone.runOutsideAngular(() => {
              oauthService.configure({
                issuer: `${clientAuth.authServerUrl}/realms/${clientAuth.realm}`,
                redirectUri: window.location.origin + window.location.pathname,
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
                  let nextStep = Promise.resolve(true);
                  // perform a silent refresh when the access token is expired
                  if (!oauthService.hasValidAccessToken()) {
                    nextStep = oauthService
                      .silentRefresh()
                      .then(() => true)
                      .catch(() => {
                        // if the silent refresh fails, redirect to the login page
                        oauthService.initLoginFlow();
                        return false;
                      });
                  }

                  goBackToPreviousUrlIfAvailable(oauthService, router);
                  oauthService.setupAutomaticSilentRefresh();

                  return nextStep.then(
                    (nextStepSuccess) => success && nextStepSuccess,
                  );
                });
            });
          }),
          switchMap((success) =>
            !success
              ? // If the login check fails and the silent refresh also failed
                of().pipe(
                  // wait for the oauthService.initLoginFlow to redirect to the login page
                  delay(TIME_TO_WAIT_BEFORE_RELOAD),
                  tap(() => {
                    // reload the page if everything failed after the delay
                    window.location.reload();
                  }),
                )
              : [success],
          ),
        ),
    );
}

export const provideSharedPatternAppInitialization = () => {
  return [
    {
      provide: APP_INITIALIZER,
      useFactory: initializeOidc,
      multi: true,
      deps: [
        Router,
        TenantService,
        OAuthService,
        SharedModelCompileTimeConfig,
        NgZone,
      ],
    },
  ];
};
