import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { OAuthService } from 'angular-oauth2-oidc';

export const hasBenutzer: CanActivateFn = () => {
  const oauthService = inject(OAuthService);
  // TODO: show landing page if not logged in
  return oauthService.hasValidAccessToken();
};
