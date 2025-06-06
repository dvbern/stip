import { DOCUMENT } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import {
  Observable,
  catchError,
  combineLatestWith,
  filter,
  map,
  of,
  switchMap,
  throwIfEmpty,
} from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/global/notification';
import {
  BenutzerRole,
  KeycloakUserCreate,
  SharedModelBenutzerApi,
  SharedModelBenutzerList,
  SharedModelModelMappingsRepresentation,
  SharedModelRole,
  SharedModelRoleList,
  bySozialdienstAdminRole,
} from '@dv/shared/model/benutzer';
import { MailService } from '@dv/shared/model/gesuch';
import { noGlobalErrorsIf } from '@dv/shared/util/http';
import {
  getCurrentUrl,
  hasLocationHeader,
  toKnownUserErrorType,
} from '@dv/shared/util-fn/keycloak-helper';

@Injectable({
  providedIn: 'root',
})
export class KeycloakHttpService {
  private http = inject(HttpClient);
  private authService = inject(OAuthService);
  private globalNotificationStore = inject(GlobalNotificationStore);
  private mailService = inject(MailService);
  private document = inject(DOCUMENT);
  private _oauthParams?: { url: string; realm: string };
  private get oauthParams() {
    if (this._oauthParams) return this._oauthParams;

    if (!this.authService.issuer) throw new Error('No OAuth issuer found');

    const url = new URL(this.authService.issuer);
    const realm = /\/realms\/([^/]+)/.exec(url.pathname)?.[1];

    if (!realm) throw new Error('No realm found in OAuth issuer');

    this._oauthParams = {
      url: url.origin,
      realm,
    };

    return this._oauthParams;
  }

  getRoleMappings$(
    userId: string,
    roleFilter?: (role: SharedModelRole) => boolean,
  ) {
    return this.http
      .get<SharedModelModelMappingsRepresentation>(
        `${this.oauthParams.url}/admin/realms/${this.oauthParams.realm}/users/${userId}/role-mappings`,
        {
          context: noGlobalErrorsIf(true),
        },
      )
      .pipe(
        map((rm) => {
          const roles = SharedModelModelMappingsRepresentation.parse(rm);

          if (roleFilter) {
            return roles.realmMappings?.filter(roleFilter);
          }

          return roles.realmMappings;
        }),
        this.interceptError('roleMapping'),
      );
  }

  getUserWithRoleMappings$(
    userId: string,
    roleFilter?: (role: SharedModelRole) => boolean,
  ) {
    return this.loadUser$(userId).pipe(
      combineLatestWith(this.getRoleMappings$(userId, roleFilter)),
      map(([user, rm]) => {
        return {
          ...user,
          roles: rm ?? [],
        };
      }),
    );
  }

  loadUser$(userId: string) {
    return this.loadUserByUrl$(
      `${this.oauthParams.url}/admin/realms/${this.oauthParams.realm}/users/${userId}`,
    );
  }

  assignRoles$(
    user: SharedModelBenutzerApi,
    roles: { id: string; name: string }[],
  ) {
    return this.http
      .post(
        `${this.oauthParams.url}/admin/realms/${this.oauthParams.realm}/users/${user.id}/role-mappings/realm`,
        roles,
        {
          context: noGlobalErrorsIf(true),
        },
      )
      .pipe(
        map(() => user),
        this.interceptError('roleMapping'),
      );
  }

  notifyUser$(user: { name: string; vorname: string; email?: string }) {
    if (!user.email) return of(false);
    return this.mailService
      .sendWelcomeEmail$({
        welcomeMail: {
          ...user,
          email: user.email,
          redirectUri: getCurrentUrl(this.document),
        },
      })
      .pipe(
        this.interceptError('benachrichtigen'),
        map(() => true),
        catchError(() => [false]),
      );
  }

  removeRoles$(user: SharedModelBenutzerApi, roles: SharedModelRole[]) {
    return this.http
      .delete(
        `${this.oauthParams.url}/admin/realms/${this.oauthParams.realm}/users/${user.id}/role-mappings/realm`,
        {
          context: noGlobalErrorsIf(true),
          body: roles,
        },
      )
      .pipe(
        map(() => user),
        this.interceptError('roleMapping'),
      );
  }

  createUser$(newUser: KeycloakUserCreate) {
    return this.http
      .post(
        `${this.oauthParams.url}/admin/realms/${this.oauthParams.realm}/users`,
        {
          enabled: true,
          firstName: newUser.vorname,
          lastName: newUser.name,
          username: newUser.email,
          email: newUser.email,
          emailVerified: true,
        },
        {
          observe: 'response',
          context: noGlobalErrorsIf(true),
        },
      )
      .pipe(this.interceptError('erstellen'));
  }

  getRoles$(roleFilter?: (role: SharedModelRole) => boolean) {
    return this.http
      .get<SharedModelRoleList>(
        `${this.oauthParams.url}/admin/realms/${this.oauthParams.realm}/roles`,
      )
      .pipe(
        map((roles) => {
          const parsed = SharedModelRoleList.safeParse(roles);

          if (!parsed.success) {
            throw new Error('Failed to parse roles');
          }

          if (roleFilter) {
            return parsed.data.filter(roleFilter);
          }

          return parsed.data;
        }),
      );
  }

  createUserWithSozialDienstAdminRole$(newUser: KeycloakUserCreate) {
    return this.createUser$(newUser).pipe(
      filter(hasLocationHeader),
      throwIfEmpty(() => new Error('User creation failed')),
      switchMap((response) =>
        this.loadUserByUrl$(response.headers.get('Location')),
      ),
      combineLatestWith(this.getRoles$(bySozialdienstAdminRole)),
      switchMap(([user, roles]) => {
        const adminRole = roles.find(
          (role) => role.name === 'V0_Sozialdienst-Admin',
        );

        if (!adminRole) {
          throw new Error('Admin Role not found');
        }

        return this.assignRoles$(user, [
          { id: adminRole.id, name: 'V0_Sozialdienst-Admin' },
        ]);
      }),
    );
  }

  updateUser$(user: SharedModelBenutzerApi) {
    return this.http.put(
      `${this.oauthParams.url}/admin/realms/${this.oauthParams.realm}/users/${user.id}`,
      user,
      {
        context: noGlobalErrorsIf(true),
      },
    );
  }

  deleteUser$(userId: string) {
    return this.http
      .delete(
        `${this.oauthParams.url}/admin/realms/${this.oauthParams.realm}/users/${userId}`,
        {
          context: noGlobalErrorsIf(true),
        },
      )
      .pipe(this.interceptError('loeschen'));
  }

  loadBenutzersWithRole$(role: BenutzerRole) {
    return this.http
      .get<SharedModelBenutzerList>(
        `${this.oauthParams.url}/admin/realms/${this.oauthParams.realm}/roles/${role}/users`,
      )
      .pipe(
        map((benutzers) => ({
          benutzers: SharedModelBenutzerList.parse(benutzers),
          role,
        })),
      );
  }

  loadUserByUrl$(url: string) {
    return this.http
      .get<SharedModelBenutzerApi>(url, {
        context: noGlobalErrorsIf(true),
      })
      .pipe(
        map((user) => SharedModelBenutzerApi.parse(user)),
        this.interceptError('laden'),
      );
  }

  private interceptError(fallbackErrorType: string) {
    return <T>(source: Observable<T>) => {
      return source.pipe(
        catchError((error) => {
          const parsedErrorType = toKnownUserErrorType(
            error,
            fallbackErrorType,
          );
          this.globalNotificationStore.createNotification({
            type: 'ERROR',
            messageKey: `sachbearbeitung-app.admin.benutzerverwaltung.benutzerErstellenFehler.${parsedErrorType}`,
          });
          throw error;
        }),
      );
    };
  }
}
