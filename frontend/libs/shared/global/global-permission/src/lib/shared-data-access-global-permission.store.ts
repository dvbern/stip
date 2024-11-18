import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { OAuthService } from 'angular-oauth2-oidc';

type PermissionState = {
  userRoles: string[] | null;
};

const initialState: PermissionState = {
  userRoles: null,
};

@Injectable({ providedIn: 'root' })
export class PermissionStore extends signalStore(
  { protectedState: false },
  withState(initialState),
  withDevtools('PermissionStore'),
) {
  authService = inject(OAuthService);

  userIsJuristSig = computed(() => {
    const userRoles = this.userRoles();

    return userRoles?.includes('Jurist') ?? false;
  });

  constructor() {
    super();
    this.setUserRoles();
  }

  setUserRoles(): void {
    const token = this.authService.getAccessToken();
    const payload = decodeJwt(token);

    if (payload && payload['realm_access']['roles']) {
      const userRoles = payload['realm_access']['roles'];
      patchState(this, { userRoles });
    }
  }
}

/**
 * Decodes a JWT token and returns the payload as a JSON object.
 * https://stackoverflow.com/questions/38552003/how-to-decode-jwt-token-in-javascript-without-using-a-libraryhttps://stackoverflow.com/questions/38552003/how-to-decode-jwt-token-in-javascript-without-using-a-library
 * @param token - The JWT token to decode.
 * @returns The decoded payload as an object, or null if decoding fails.
 */
function decodeJwt<T extends object = { [key: string]: any }>(
  token: string,
): T | null {
  try {
    // Split the token into its parts
    const parts = token.split('.');
    if (parts.length !== 3) {
      throw new Error('Invalid JWT format');
    }

    // Base64Url decode the payload
    const base64Url = parts[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join(''),
    );

    // Parse the JSON payload
    return JSON.parse(jsonPayload);
  } catch (error) {
    console.error('Failed to decode JWT:', error);
    return null;
  }
}
