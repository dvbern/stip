import { inject, provideAppInitializer } from '@angular/core';
import { Router } from '@angular/router';
import { OAuthService, OAuthStorage } from 'angular-oauth2-oidc';
import { lastValueFrom } from 'rxjs';
import { switchMap } from 'rxjs/operators';

import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import { TenantService } from '@dv/shared/model/gesuch';
import { shouldNotAuthorizeRequestIf } from '@dv/shared/util/http';
import { SharedUtilTenantConfigService } from '@dv/shared/util/tenant-config';

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
  tenantConfigService: SharedUtilTenantConfigService,
  tenantService: TenantService,
  oauthService: OAuthService,
  compileTimeConfig: SharedModelCompileTimeConfig,
) {
  return () =>
    lastValueFrom(
      tenantService
        .getCurrentTenant$(undefined, undefined, {
          context: shouldNotAuthorizeRequestIf(true),
        })
        .pipe(
          switchMap((tenantInfo) => {
            tenantConfigService.setTenantInfo(tenantInfo);
            const { clientAuth } = tenantInfo;
            oauthService.setStorage(sessionStorage);
            oauthService.configure({
              issuer: `${clientAuth.authServerUrl}/realms/${clientAuth.realm}`,
              redirectUri: window.location.origin + window.location.pathname,
              postLogoutRedirectUri: window.location.origin + '/',
              clientId: compileTimeConfig.authClientId,
              scope: 'openid profile email offline_access',
              responseType: 'code',
              disablePKCE: false,
              showDebugInformation: false,
              sessionChecksEnabled: false,
              clearHashAfterLogin: false,
              useSilentRefresh: false,
              nonceStateSeparator: 'semicolon',
            });
            return oauthService
              .loadDiscoveryDocumentAndTryLogin()
              .then((success) => {
                const nextStep = Promise.resolve(true);
                // perform a silent refresh when the access token is expired
                if (!oauthService.hasValidAccessToken()) {
                  oauthService.setupAutomaticSilentRefresh();
                  return false;
                }

                goBackToPreviousUrlIfAvailable(oauthService, router);
                oauthService.setupAutomaticSilentRefresh(
                  undefined,
                  'access_token',
                );

                return nextStep.then(
                  (nextStepSuccess) => success && nextStepSuccess,
                );
              });
          }),
        ),
    );
}

export const provideSharedPatternAppInitialization = () => {
  return [
    {
      provide: OAuthStorage,
      useFactory: oauthStorageFactory,
    },
    provideAppInitializer(() => {
      const initializerFn = initializeOidc(
        inject(Router),
        inject(SharedUtilTenantConfigService),
        inject(TenantService),
        inject(OAuthService),
        inject(SharedModelCompileTimeConfig),
      );
      return initializerFn();
    }),
  ];
};

export function oauthStorageFactory(): OAuthStorage {
  return sessionStorage;
}
