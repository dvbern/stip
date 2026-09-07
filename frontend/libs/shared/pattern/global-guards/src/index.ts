import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { OAuthService } from 'angular-oauth2-oidc';
import { from, map } from 'rxjs';

export const hasBenutzer: CanActivateFn = () => {
  const oauthService = inject(OAuthService);
  if (oauthService.hasValidAccessToken()) {
    return true;
  } else {
    oauthService.initLoginFlow();
    return from(oauthService.tryLoginCodeFlow()).pipe(map(() => false));
  }
};
