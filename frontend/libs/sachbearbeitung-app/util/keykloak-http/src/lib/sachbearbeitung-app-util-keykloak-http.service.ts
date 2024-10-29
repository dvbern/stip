import { DOCUMENT } from '@angular/common';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { Observable, catchError, combineLatestWith, map, of } from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/data-access/global-notification';
import {
  BenutzerCreateKeykloak,
  BenutzerVerwaltungRole,
  SharedModelBenutzerApi,
  SharedModelBenutzerList,
  SharedModelModelMappingsRepresentation,
  SharedModelRole,
  SharedModelRoleList,
  SharedModelUserAdminError,
} from '@dv/shared/model/benutzer';
import { MailService } from '@dv/shared/model/gesuch';
import { noGlobalErrorsIf } from '@dv/shared/util/http';

export const toKnownUserErrorType = (error: unknown, fallbackType: string) => {
  const parsed = SharedModelUserAdminError.parse(error);
  return parsed.type ?? fallbackType;
};

export const getCurrentUrl = (document: Document) => {
  return document.location.origin;
};

type HttpResponseWithLocation = HttpResponse<unknown> & {
  headers: { get: (header: 'Location') => string };
};

export const hasLocationHeader = (
  response: HttpResponse<unknown>,
): response is HttpResponseWithLocation => {
  return response.headers.has('Location');
};

@Injectable({
  providedIn: 'root',
})
export class KeykloakHttpService {
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

  notifyUser$(user: SharedModelBenutzerApi) {
    if (!user.email) return of(false);
    return this.mailService
      .sendWelcomeEmail$({
        welcomeMail: {
          vorname: user.firstName,
          name: user.lastName,
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

  createUser$(newUser: BenutzerCreateKeykloak) {
    return this.http
      .post(
        `${this.oauthParams.url}/admin/realms/${this.oauthParams.realm}/users`,
        {
          enabled: true,
          firstName: newUser.vorname,
          lastName: newUser.nachname,
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

  loadBenutzersWithRole$(role: BenutzerVerwaltungRole) {
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

  getRoles$(roleFilter?: (role: SharedModelRole) => boolean) {
    return this.http
      .get<SharedModelRoleList>(
        `${this.oauthParams.url}/admin/realms/${this.oauthParams.realm}/roles`,
      )
      .pipe(
        map((roles) => {
          // @scph: should we also zod parse the roles here?
          if (roleFilter) {
            return roles.filter(roleFilter);
          }

          return roles;
        }),
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
