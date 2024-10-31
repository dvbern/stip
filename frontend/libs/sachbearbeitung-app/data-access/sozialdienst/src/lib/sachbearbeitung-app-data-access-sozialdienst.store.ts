import { Injectable, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import {
  EMPTY,
  catchError,
  combineLatestWith,
  exhaustMap,
  filter,
  map,
  pipe,
  switchMap,
  tap,
  throwError,
  throwIfEmpty,
} from 'rxjs';

import { KeycloakHttpService } from '@dv/sachbearbeitung-app/util/keycloak-http';
import { hasLocationHeader } from '@dv/sachbearbeitung-app/util-fn/keycloak-helper';
import { GlobalNotificationStore } from '@dv/shared/global/notification';
import { bySozialdienstAdminRole } from '@dv/shared/model/benutzer';
import {
  Sozialdienst,
  SozialdienstAdminCreate,
  SozialdienstCreate,
  SozialdienstService,
  SozialdienstUpdate,
} from '@dv/shared/model/gesuch';
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

type SozialdienstState = {
  sozialdienste: CachedRemoteData<Sozialdienst[]>;
  sozialdienst: RemoteData<Sozialdienst>;
};

const initialState: SozialdienstState = {
  sozialdienste: initial(),
  sozialdienst: initial(),
};

@Injectable()
export class SozialdienstStore extends signalStore(
  { protectedState: false },
  withState(initialState),
  withDevtools('SozialdienstStore'),
) {
  private sozialdienstService = inject(SozialdienstService);
  private keycloak = inject(KeycloakHttpService);
  private globalNotificationStore = inject(GlobalNotificationStore);

  // sozialdiensteListViewSig = computed(() => {
  //   return fromCachedDataSig(this.sozialdienste);
  // });

  // sozialdienstViewSig = computed(() => {
  //   return this.sozialdienst.data();
  // });

  loadAllSozialdienste$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          sozialdienste: cachedPending(state.sozialdienste),
        }));
      }),
      switchMap(() =>
        this.sozialdienstService
          .getAllSozialdienste$()
          .pipe(
            handleApiResponse((sozialdienste) =>
              patchState(this, { sozialdienste }),
            ),
          ),
      ),
    ),
  );

  loadSozialdienst$ = rxMethod<{ sozialdienstId: string }>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          sozialdienst: pending(),
        }));
      }),
      switchMap(({ sozialdienstId }) =>
        this.sozialdienstService
          .getSozialdienst$({ sozialdienstId })
          .pipe(
            handleApiResponse((sozialdienst) =>
              patchState(this, { sozialdienst }),
            ),
          ),
      ),
    ),
  );

  createSozialdienst$ = rxMethod<{
    sozialdienstCreate: Omit<SozialdienstCreate, 'sozialdienstAdmin'> & {
      sozialdienstAdmin: Omit<SozialdienstAdminCreate, 'keycloakId'>;
    };
    onAfterSave?: (sozialdienstId: string) => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, {
          sozialdienst: pending(),
        });
      }),
      exhaustMap(({ sozialdienstCreate, onAfterSave }) => {
        const newUser = {
          ...sozialdienstCreate.sozialdienstAdmin,
        };

        return this.keycloak
          .createUser$({
            email: newUser.eMail,
            name: newUser.nachname,
            vorname: newUser.vorname,
          })
          .pipe(
            filter(hasLocationHeader),
            throwIfEmpty(() => new Error('User creation failed')),
            switchMap((response) =>
              this.keycloak.loadUserByUrl$(response.headers.get('Location')),
            ),
            combineLatestWith(this.keycloak.getRoles$(bySozialdienstAdminRole)),
            switchMap(([user, roles]) => {
              const adminRole = roles.find(
                (role) => role.name === 'Sozialdienst-Admin',
              );

              if (!adminRole) {
                throw new Error('Admin Role not found');
              }

              return this.keycloak.assignRoles$(user, [
                { id: adminRole.id, name: 'Sozialdienst-Admin' },
              ]);
            }),
            switchMap((user) => {
              const newDienst: SozialdienstCreate = {
                ...sozialdienstCreate,
                sozialdienstAdmin: {
                  nachname: user.lastName,
                  vorname: user.firstName,
                  keycloakId: user.id,
                  // todo: make email on our user optional?
                  eMail: user.email ?? '',
                },
              };

              return this.sozialdienstService
                .createSozialdienst$({
                  sozialdienstCreate: newDienst,
                })
                .pipe(
                  map((sozialdienst) => ({
                    user,
                    sozialdienst,
                  })),
                  catchError((error) => {
                    // todo: test if this works
                    this.keycloak.deleteUser$(user.id);

                    return throwError(() => error);
                  }),
                );
            }),
            switchMap(({ sozialdienst, user }) =>
              this.keycloak
                .notifyUser$({
                  name: user.lastName,
                  vorname: user.firstName,
                  email: user.email,
                })
                .pipe(
                  handleApiResponse(
                    () => {
                      patchState(this, { sozialdienst: success(sozialdienst) });
                    },
                    {
                      onSuccess: (wasSuccessfull) => {
                        if (wasSuccessfull) {
                          this.globalNotificationStore.createSuccessNotification(
                            {
                              messageKey:
                                'sachbearbeitung-app.admin.sozialdienst.sozialdienstErstellt',
                            },
                          );
                          onAfterSave?.(sozialdienst.id);
                        }
                      },
                    },
                  ),
                ),
            ),
            catchError((error) => {
              patchState(this, { sozialdienst: failure(error) });
              return EMPTY;
            }),
          );
      }),
    ),
  );

  deleteSozialdienst$ = rxMethod<{ sozialdienstId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          sozialdienste: cachedPending(state.sozialdienste),
        }));
      }),
      switchMap(({ sozialdienstId }) =>
        this.sozialdienstService.deleteSozialdienst$({ sozialdienstId }).pipe(
          handleApiResponse(() => {
            patchState(this, (state) => ({
              sozialdienste: cachedPending(state.sozialdienste),
            }));
          }),
        ),
      ),
    ),
  );

  updateSozialdienst$ = rxMethod<{ sozialdienstUpdate: SozialdienstUpdate }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          sozialdienste: cachedPending(state.sozialdienste),
        }));
      }),
      switchMap(({ sozialdienstUpdate }) =>
        this.sozialdienstService
          .updateSozialdienst$({ sozialdienstUpdate })
          .pipe(
            handleApiResponse((sozialdienst) =>
              patchState(this, { sozialdienst }),
            ),
          ),
      ),
    ),
  );

  resetSozialdienst() {
    patchState(this, { sozialdienst: initial() });
  }

  // updateSozialdienstAdmin$ = rxMethod<{ sozialdienstId: string; values: unknown }>(
  //   pipe(
  //     tap(() => {
  //       patchState(this, (state) => ({
  //         sozialdienste: cachedPending(state.sozialdienste),
  //       }));
  //     }),
  //     switchMap(({ sozialdienstId, values }) =>
  //       this.sozialdienstService
  //         .updateSozialdienstAdmin$({ sozialdienstId, payload: values })
  //         .pipe(
  //           handleApiResponse((sozialdienst) =>
  //             patchState(this, { sozialdienst }),
  //           ),
  //         ),
  //     ),
  //   ),
  // );

  replaceSozialdienstAdmin$ = rxMethod<{
    sozialdienstId: string;
    existingSozialdienstAdminKeycloakId: string;
    newAdmin: Omit<SozialdienstAdminCreate, 'keycloakId'>;
  }>(
    pipe(
      tap(() => {
        patchState(this, {
          sozialdienst: pending(),
        });
      }),
      exhaustMap(
        ({ sozialdienstId, existingSozialdienstAdminKeycloakId, newAdmin }) => {
          return this.keycloak
            .createUser$({
              name: newAdmin.nachname,
              vorname: newAdmin.nachname,
              email: newAdmin.eMail,
            })
            .pipe(
              filter(hasLocationHeader),
              throwIfEmpty(() => new Error('User creation failed')),
              switchMap((response) =>
                this.keycloak.loadUserByUrl$(response.headers.get('Location')),
              ),
              combineLatestWith(
                this.keycloak.getRoles$(bySozialdienstAdminRole),
              ),
              switchMap(([user, roles]) => {
                const adminRole = roles.find(
                  (role) => role.name === 'Sozialdienst-Admin',
                );

                if (!adminRole) {
                  throw new Error('Admin Role not found');
                }

                return this.keycloak.assignRoles$(user, [
                  { id: adminRole.id, name: 'Sozialdienst-Admin' },
                ]);
              }),
              switchMap((user) =>
                this.sozialdienstService.replaceSozialdienstAdmin$({
                  sozialdienstId,
                  sozialdienstAdminCreate: {
                    nachname: user.lastName,
                    vorname: user.firstName,
                    eMail: user.email ?? '',
                    keycloakId: user.id,
                  },
                }),
              ),
              switchMap((sozialdienstAdmin) =>
                this.keycloak
                  .deleteUser$(existingSozialdienstAdminKeycloakId)
                  .pipe(map(() => sozialdienstAdmin)),
              ),
              switchMap((sozialdienstAdmin) =>
                this.keycloak
                  .notifyUser$({
                    vorname: sozialdienstAdmin.vorname,
                    name: sozialdienstAdmin.nachname,
                    email: sozialdienstAdmin.eMail,
                  })
                  .pipe(
                    handleApiResponse(
                      () => {
                        patchState(this, (state) => {
                          const dienst = state.sozialdienst.data;
                          if (!dienst || !sozialdienstAdmin) {
                            return state;
                          }
                          return {
                            sozialdienst: success({
                              ...dienst,
                              sozialdienstAdmin: {
                                ...sozialdienstAdmin,
                              },
                            }),
                          };
                        });
                      },
                      {
                        onSuccess: (wasSuccessfull) => {
                          if (wasSuccessfull) {
                            this.globalNotificationStore.createSuccessNotification(
                              {
                                messageKey:
                                  'sachbearbeitung-app.admin.sozialdienst.sozialdienstAdminErsetzt',
                              },
                            );
                          }
                        },
                      },
                    ),
                  ),
              ),
            );
        },
      ),
    ),
  );
}

// switchMap((user) =>
//   this.sozialdienstService
//     .replaceSozialdienstAdmin$({
//       sozialdienstId,
//       sozialdienstAdminCreate,
//     })
//     .pipe(
//       handleApiResponse((sozialdienstAdmin) =>
//         patchState(this, (state) => {
//           const dienst = state.sozialdienst.data;
//           if (!dienst || !sozialdienstAdmin.data) {
//             return state;
//           }
//           return {
//             sozialdienst: success({
//               ...dienst,
//               sozialdienstAdmin: {
//                 ...sozialdienstAdmin.data,
//               },
//             }),
//           };
//         }),
//       ),
//     ),
// ),
