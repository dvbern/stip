import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';

export const hasBenutzer: CanActivateFn = () => {
  const keycloakService = inject(KeycloakService);
  // TODO: show landing page if not logged in
  return keycloakService.isLoggedIn();
};
