import { Injectable, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import {
  EMPTY,
  catchError,
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

import { GlobalNotificationStore } from '@dv/shared/global/notification';
import {
  BENUTZER_ROLES,
  SharedModelBenutzer,
  SharedModelBenutzerApi,
  SharedModelBenutzerWithRoles,
  SharedModelRoleList,
  byBenutzertVerwaltungRoles,
} from '@dv/shared/model/benutzer';
import { SharedModelError } from '@dv/shared/model/error';
import { BenutzerService } from '@dv/shared/model/gesuch';
import {
  noGlobalErrorsIf,
  shouldIgnoreNotFoundErrorsIf,
} from '@dv/shared/util/http';
import { KeycloakHttpService } from '@dv/shared/util/keycloak-http';
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
import {
  createBenutzerListFromRoleLookup,
  hasLocationHeader,
} from '@dv/shared/util-fn/keycloak-helper';

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

const DEFAULT_ROLE = 'default-roles-bern';

@Injectable()
export class BenutzerverwaltungStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private benutzerService = inject(BenutzerService);
  private keycloak = inject(KeycloakHttpService);

  private globalNotificationStore = inject(GlobalNotificationStore);

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
          Object.values(BENUTZER_ROLES).map((role) => {
            return this.keycloak.loadBenutzersWithRole$(role);
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
      switchMap((userId) => this.keycloak.getUserWithRoleMappings$(userId)),
      handleApiResponse((benutzer) => patchState(this, { benutzer })),
    ),
  );

  updateBenutzer$ = rxMethod<{
    user: SharedModelBenutzerApi;
    roles: SharedModelRoleList;
  }>(
    pipe(
      map(({ user, roles }) => {
        const rolesToRemove = this.benutzer().data?.roles.filter(
          (r) =>
            !roles.some((role) => role.name === r.name) &&
            r.name !== DEFAULT_ROLE,
        );

        return { user, roles, rolesToRemove };
      }),
      tap(() => {
        patchState(this, {
          benutzer: pending(),
        });
      }),
      exhaustMap(({ user, roles, rolesToRemove }) =>
        this.keycloak.updateUser$(user).pipe(
          switchMap(() => this.keycloak.assignRoles$(user, roles)), // interceptError is handled in assignRoles$
          switchMap(() => {
            if (!rolesToRemove || rolesToRemove.length === 0) {
              return of(user);
            }

            return this.keycloak.removeRoles$(user, rolesToRemove); // interceptError is handled in removeRoles$
          }),
          switchMap(() =>
            this.keycloak.getUserWithRoleMappings$(
              user.id,
              byBenutzertVerwaltungRoles,
            ),
          ),
          handleApiResponse(() => undefined, {
            onSuccess: (benutzer) => {
              this.globalNotificationStore.createSuccessNotification({
                messageKey:
                  'sachbearbeitung-app.admin.benutzerverwaltung.benutzerBearbeitet',
              });
              patchState(this, { benutzer: success(benutzer) });
            },
            onFailure: () => {
              this.loadBenutzerWithRoles$(user.id);
            },
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
        this.keycloak
          .getRoles$(byBenutzertVerwaltungRoles)
          .pipe(
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
        this.keycloak.createUser$({ vorname, name, email }).pipe(
          filter(hasLocationHeader),
          throwIfEmpty(() => new Error('User creation failed')),
          switchMap((response) =>
            this.keycloak.loadUserByUrl$(response.headers.get('Location')),
          ),
          switchMap((user) => this.keycloak.assignRoles$(user, roles)),
          switchMap((user) =>
            this.keycloak
              .notifyUser$({
                name: user.lastName,
                vorname: user.firstName,
                email: user.email,
              })
              .pipe(
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

  private deleteBenutzerFromBothApis$(benutzerId: string) {
    return this.benutzerService
      .deleteBenutzer$({ benutzerId }, undefined, undefined, {
        context: noGlobalErrorsIf(true, shouldIgnoreNotFoundErrorsIf(true)),
      })
      .pipe(switchMap(() => this.keycloak.deleteUser$(benutzerId)));
  }
}
