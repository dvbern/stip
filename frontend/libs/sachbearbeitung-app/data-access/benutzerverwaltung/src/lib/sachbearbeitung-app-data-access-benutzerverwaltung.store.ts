import { DOCUMENT } from '@angular/common';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { OAuthService } from 'angular-oauth2-oidc';
import {
  EMPTY,
  Observable,
  catchError,
  combineLatestWith,
  exhaustMap,
  filter,
  forkJoin,
  map,
  of,
  pipe,
  switchMap,
  tap,
  throwIfEmpty,
} from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/data-access/global-notification';
import {
  SACHBEARBEITER_APP_ROLES,
  SharedModelBenutzer,
  SharedModelBenutzerApi,
  SharedModelBenutzerList,
  SharedModelBenutzerRole,
  SharedModelBenutzerWithRoles,
  SharedModelModelMappingsRepresentation,
  SharedModelRole,
  SharedModelRoleList,
  SharedModelUserAdminError,
  UsableRole,
} from '@dv/shared/model/benutzer';
import { SharedModelError } from '@dv/shared/model/error';
import { BenutzerService, MailService } from '@dv/shared/model/gesuch';
import { SharedModelState } from '@dv/shared/model/state-colors';
import {
  noGlobalErrorsIf,
  shouldIgnoreNotFoundErrorsIf,
} from '@dv/shared/util/http';
import {
  CachedRemoteData,
  RemoteData,
  cachedPending,
  failure,
  handleApiResponse,
  initial,
  pending,
  success,
} from '@dv/shared/util/remote-data';

type HttpResponseWithLocation = HttpResponse<unknown> & {
  headers: { get: (header: 'Location') => string };
};
type BenutzerverwaltungState = {
  benutzers: CachedRemoteData<SharedModelBenutzer[]>;
  benutzer: RemoteData<SharedModelBenutzerWithRoles>;
  availableRoles: CachedRemoteData<SharedModelRoleList>;
};

const initialState: BenutzerverwaltungState = {
  benutzers: initial(),
  benutzer: initial(),
  availableRoles: initial(),
};

@Injectable()
export class BenutzerverwaltungStore extends signalStore(
  withState(initialState),
  withDevtools('BenutzerverwaltungStore'),
) {
  private http = inject(HttpClient);
  private document = inject(DOCUMENT);
  private authService = inject(OAuthService);
  private benutzerService = inject(BenutzerService);
  private mailService = inject(MailService);
  private globalNotificationStore = inject(GlobalNotificationStore);

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

  loadAllSbAppBenutzers$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          benutzers: cachedPending(state.benutzers),
        }));
      }),
      exhaustMap(() =>
        // Load all users for each role that we currently use in the SB app
        // sadly Keycloak does not support loading all users and their roles in one request
        // so we load the user list for every known role and merge them afterwards
        forkJoin(
          SACHBEARBEITER_APP_ROLES.map((role) => {
            return this.loadBenutzersWithRole$(role);
          }),
        ),
      ),
      map((benutzersByRole) =>
        createBenutzerListFromRoleLookup(benutzersByRole),
      ),
      handleApiResponse((benutzers) => patchState(this, { benutzers }), {
        onFailure: (error) => {
          const parsedError = SharedModelError.parse(error);
          if (parsedError.type === 'zodError') {
            console.error(parsedError.message, parsedError.errors);
          }
          this.globalNotificationStore.handleHttpRequestFailed([parsedError]);
        },
      }),
    ),
  );

  loadBenutzerWithRoles$ = rxMethod<string>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          benutzer: pending(),
        }));
      }),
      switchMap((userId) => this.getUserWithRoleMappings$(userId)),
    ),
  );

  updateBenutzer$ = rxMethod<{
    user: SharedModelBenutzerApi;
    roles: SharedModelRoleList;
  }>(
    pipe(
      map(({ user, roles }) => {
        const rolesToRemove = this.benutzer().data?.roles.filter(
          (r) => !roles.some((role) => role.name === r.name),
        );

        const oldUser = {
          // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
          ...this.benutzer().data!,
          roles: [...(this.benutzer().data?.roles ?? [])],
        };

        return { user, roles, rolesToRemove, oldUser };
      }),
      tap(() => {
        patchState(this, {
          benutzer: pending(),
        });
      }),
      exhaustMap(({ user, roles, rolesToRemove, oldUser }) =>
        this.http
          .put(
            `${this.oauthParams.url}/admin/realms/${this.oauthParams.realm}/users/${user.id}`,
            user,
            {
              context: noGlobalErrorsIf(true),
            },
          )
          .pipe(
            switchMap(() => this.assignRoles$(user, roles)), // interceptError is handled in assignRoles$
            switchMap(() => {
              if (!rolesToRemove || rolesToRemove.length === 0) {
                return of(user);
              }

              return this.removeRoles$(user, rolesToRemove); // interceptError is handled in removeRoles$
            }),
            switchMap(() => this.getUserWithRoleMappings$(user.id)),
            catchError(() => {
              patchState(this, { benutzer: success(oldUser) });
              return EMPTY;
            }),
          ),
      ),
    ),
  );

  resetBenutzer() {
    patchState(this, { benutzer: initial() });
  }

  deleteBenutzer$ = rxMethod<{ benutzerId: string }>(
    pipe(
      tap(() => {
        patchState(this, (s) => ({
          benutzers: cachedPending(s.benutzers),
        }));
      }),
      switchMap(({ benutzerId }) =>
        this.deleteBenutzerFromBothApis$(benutzerId).pipe(
          handleApiResponse(
            () => {
              patchState(this, { benutzer: initial() });
            },
            {
              onSuccess: () => {
                this.globalNotificationStore.createSuccessNotification({
                  messageKey:
                    'sachbearbeitung-app.admin.benutzerverwaltung.benutzerGeloescht',
                });
                this.loadAllSbAppBenutzers$();
              },
              onFailure: () => {
                this.loadAllSbAppBenutzers$();
              },
            },
          ),
        ),
      ),
    ),
  );

  loadAvailableRoles$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          availableRoles: cachedPending(state.availableRoles),
        }));
      }),
      switchMap(() =>
        this.http
          .get<SharedModelRoleList>(
            `${this.oauthParams.url}/admin/realms/${this.oauthParams.realm}/roles`,
          )
          .pipe(
            map((roles) =>
              SharedModelRoleList.parse(roles).filter(byUsableRoles),
            ),
            handleApiResponse((availableRoles) =>
              patchState(this, { availableRoles }),
            ),
          ),
      ),
    ),
  );

  registerUser$ = rxMethod<{
    name: string;
    vorname: string;
    email: string;
    roles: SharedModelRoleList;
    onAfterSave?: (userId: string) => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, {
          benutzer: pending(),
        });
      }),
      exhaustMap(({ name, vorname, email, roles, onAfterSave }) =>
        this.createUser$(vorname, name, email).pipe(
          filter(hasLocationHeader),
          throwIfEmpty(() => new Error('User creation failed')),
          switchMap((response) => this.loadUserByLocation$(response)),
          switchMap((user) => this.assignRoles$(user, roles)),
          switchMap((user) =>
            this.notifyUser$(user).pipe(
              handleApiResponse(
                () => {
                  // roles are set optimistically on the user object
                  patchState(this, { benutzer: success({ ...user, roles }) });
                },
                {
                  onSuccess: (wasSuccessfull) => {
                    if (wasSuccessfull) {
                      this.globalNotificationStore.createSuccessNotification({
                        messageKey:
                          'sachbearbeitung-app.admin.benutzerverwaltung.benutzerErstellt',
                      });
                      onAfterSave?.(user.id);
                    }
                  },
                },
              ),
            ),
          ),
          catchError((error) => {
            patchState(this, { benutzer: failure(error) });
            return EMPTY;
          }),
        ),
      ),
    ),
  );

  private loadBenutzersWithRole$(role: UsableRole) {
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

  private createUser$(vorname: string, name: string, email: string) {
    return this.http
      .post(
        `${this.oauthParams.url}/admin/realms/${this.oauthParams.realm}/users`,
        {
          enabled: true,
          firstName: vorname,
          lastName: name,
          username: email,
          email,
          emailVerified: true,
        },
        {
          observe: 'response',
          context: noGlobalErrorsIf(true),
        },
      )
      .pipe(this.interceptError('erstellen'));
  }

  private deleteBenutzerFromBothApis$(benutzerId: string) {
    return this.benutzerService
      .deleteBenutzer$({ benutzerId }, undefined, undefined, {
        context: noGlobalErrorsIf(true, shouldIgnoreNotFoundErrorsIf(true)),
      })
      .pipe(
        switchMap(() =>
          this.http.delete(
            `${this.oauthParams.url}/admin/realms/${this.oauthParams.realm}/users/${benutzerId}`,
            {
              context: noGlobalErrorsIf(true),
            },
          ),
        ),
        this.interceptError('loeschen'),
      );
  }

  private loadUserByLocation$(response: HttpResponseWithLocation) {
    return this.http
      .get<SharedModelBenutzerApi>(response.headers.get('Location'), {
        context: noGlobalErrorsIf(true),
      })
      .pipe(
        map((user) => SharedModelBenutzerApi.parse(user)),
        this.interceptError('laden'),
      );
  }

  private loadUser$(userId: string) {
    return this.http
      .get<SharedModelBenutzerApi>(
        `${this.oauthParams.url}/admin/realms/${this.oauthParams.realm}/users/${userId}`,
        {
          context: noGlobalErrorsIf(true),
        },
      )
      .pipe(
        map((user) => SharedModelBenutzerApi.parse(user)),
        this.interceptError('laden'),
      );
  }

  private getUserWithRoleMappings$(userId: string) {
    return this.loadUser$(userId).pipe(
      combineLatestWith(this.getRoleMappings$(userId)),
      map(([user, rm]) => {
        return {
          ...user,
          roles: rm ?? [],
        };
      }),
      handleApiResponse((benutzer) => patchState(this, { benutzer })),
    );
  }

  private getRoleMappings$(userId: string) {
    return this.http
      .get<SharedModelModelMappingsRepresentation>(
        `${this.oauthParams.url}/admin/realms/${this.oauthParams.realm}/users/${userId}/role-mappings`,
        {
          context: noGlobalErrorsIf(true),
        },
      )
      .pipe(
        map((rm) => {
          return SharedModelModelMappingsRepresentation.parse(
            rm,
          ).realmMappings?.filter(byUsableRoles);
        }),
        this.interceptError('roleMapping'),
      );
  }

  private assignRoles$(
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

  private removeRoles$(user: SharedModelBenutzerApi, roles: SharedModelRole[]) {
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

  private notifyUser$(user: SharedModelBenutzerApi) {
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

export const getCurrentUrl = (document: Document) => {
  return document.location.origin;
};

export const hasLocationHeader = (
  response: HttpResponse<unknown>,
): response is HttpResponseWithLocation => {
  return response.headers.has('Location');
};

export const byUsableRoles = (role: SharedModelRole) => {
  return SACHBEARBEITER_APP_ROLES.some((r) => r === role.name);
};

export const toKnownUserErrorType = (error: unknown, fallbackType: string) => {
  const parsed = SharedModelUserAdminError.parse(error);
  return parsed.type ?? fallbackType;
};

export const roleToStateColor = (role: UsableRole): SharedModelState => {
  switch (role) {
    case 'Sachbearbeiter':
      return 'info';
    case 'Admin':
      return 'success';
    case 'Jurist':
      return 'warning';
    default:
      return 'danger';
  }
};

/**
 * Joins the user lists from different roles into one list and adds the roles as a property to the Benutzer object.
 */
export const createBenutzerListFromRoleLookup = (
  benutzersByRole: {
    benutzers: SharedModelBenutzerApi[];
    role: 'Sachbearbeiter' | 'Admin' | 'Jurist';
  }[],
): SharedModelBenutzer[] => {
  return Object.values(
    benutzersByRole.reduce<
      Record<
        string,
        SharedModelBenutzerApi & {
          name: string;
          roles: SharedModelBenutzerRole[];
        }
      >
    >(
      (allById, { role, benutzers }) =>
        benutzers.reduce(
          (all, benutzer) => ({
            ...all,
            [benutzer.id]: {
              ...benutzer,
              name: `${benutzer.firstName} ${benutzer.lastName}`,
              roles: [
                ...(all[benutzer.id]?.roles ?? []),
                { name: role, color: roleToStateColor(role) },
              ],
            },
          }),
          allById,
        ),
      {},
    ),
  ).map((benutzer) => ({
    ...benutzer,
    roles: {
      extraAmount:
        benutzer.roles.length > 2 ? benutzer.roles.length - 2 : undefined,
      compact: benutzer.roles.slice(0, 2),
      full: benutzer.roles,
    },
  }));
};
