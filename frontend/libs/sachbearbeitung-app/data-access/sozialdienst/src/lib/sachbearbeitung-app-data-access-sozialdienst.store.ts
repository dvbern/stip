import { Injectable, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import {
  EMPTY,
  catchError,
  exhaustMap,
  map,
  pipe,
  switchMap,
  tap,
  throwError,
} from 'rxjs';

import { KeycloakHttpService } from '@dv/sachbearbeitung-app/util/keycloak-http';
import { GlobalNotificationStore } from '@dv/shared/global/notification';
import { SharedModelBenutzerApi } from '@dv/shared/model/benutzer';
import {
  Sozialdienst,
  SozialdienstAdminCreate,
  SozialdienstAdminUpdate,
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

  resetSozialdienst() {
    patchState(this, { sozialdienst: initial() });
  }

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
          .createUserWithSozialDienstAdminRole$({
            name: newUser.nachname,
            vorname: newUser.vorname,
            email: newUser.email,
          })
          .pipe(
            switchMap((user) => {
              if (!user.email) {
                throw new Error('User email not defined');
              }

              const newDienst: SozialdienstCreate = {
                ...sozialdienstCreate,
                sozialdienstAdmin: {
                  nachname: user.lastName,
                  vorname: user.firstName,
                  keycloakId: user.id,
                  email: user.email,
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
                  // delete the user if the creation of the sozialdienst fails
                  catchError((error) => {
                    return this.keycloak
                      .deleteUser$(user.id)
                      .pipe(switchMap(() => throwError(() => error)));
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

  updateSozialdienst$ = rxMethod<{ sozialdienst: Sozialdienst }>(
    pipe(
      tap(() => {
        patchState(this, {
          sozialdienst: pending(),
        });
      }),
      exhaustMap(({ sozialdienst }) => {
        const userUpdate: SharedModelBenutzerApi = {
          id: sozialdienst.sozialdienstAdmin.keycloakId,
          email: sozialdienst.sozialdienstAdmin.email,
          firstName: sozialdienst.sozialdienstAdmin.nachname,
          lastName: sozialdienst.sozialdienstAdmin.vorname,
        };

        const sozialdienstUpdate: SozialdienstUpdate = {
          adresse: sozialdienst.adresse,
          iban: sozialdienst.iban,
          id: sozialdienst.id,
          name: sozialdienst.name,
        };

        const sozialdienstAdminUpdate: SozialdienstAdminUpdate = {
          nachname: sozialdienst.sozialdienstAdmin.nachname,
          vorname: sozialdienst.sozialdienstAdmin.vorname,
        };

        return this.keycloak.updateUser$(userUpdate).pipe(
          switchMap(() => {
            return this.sozialdienstService.updateSozialdienstAdmin$({
              sozialdienstId: sozialdienst.id,
              sozialdienstAdminUpdate,
            });
          }),
          switchMap(() =>
            this.sozialdienstService
              .updateSozialdienst$({
                sozialdienstUpdate,
              })
              .pipe(
                handleApiResponse(
                  (sozialdienst) => {
                    patchState(this, { sozialdienst });
                  },
                  {
                    onSuccess: () => {
                      this.globalNotificationStore.createSuccessNotification({
                        messageKey:
                          'sachbearbeitung-app.admin.sozialdienst.sozialdienstAktualisiert',
                      });
                    },
                    onFailure: () => {
                      this.loadSozialdienst$({
                        sozialdienstId: sozialdienst.id,
                      });
                    },
                  },
                ),
              ),
          ),
          catchError(() => {
            this.loadSozialdienst$({
              sozialdienstId: sozialdienst.id,
            });

            return EMPTY;
          }),
        );
      }),
    ),
  );

  replaceSozialdienstAdmin$ = rxMethod<{
    sozialdienstId: string;
    existingSozialdienstAdminKeycloakId: string;
    newUser: Omit<SozialdienstAdminCreate, 'keycloakId'>;
  }>(
    pipe(
      tap(() => {
        patchState(this, {
          sozialdienst: pending(),
        });
      }),
      exhaustMap(
        ({ sozialdienstId, existingSozialdienstAdminKeycloakId, newUser }) => {
          return this.keycloak
            .createUserWithSozialDienstAdminRole$({
              name: newUser.nachname,
              vorname: newUser.vorname,
              email: newUser.email,
            })
            .pipe(
              switchMap((user) => {
                if (!user.email) {
                  throw new Error('User email not defined');
                }

                return this.sozialdienstService.replaceSozialdienstAdmin$({
                  sozialdienstId,
                  sozialdienstAdminCreate: {
                    nachname: user.lastName,
                    vorname: user.firstName,
                    email: user.email,
                    keycloakId: user.id,
                  },
                });
              }),
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
                    email: sozialdienstAdmin.email,
                  })
                  .pipe(
                    handleApiResponse(() => undefined, {
                      onSuccess: (wasSuccessfull) => {
                        this.loadSozialdienst$({ sozialdienstId });
                        if (wasSuccessfull) {
                          this.globalNotificationStore.createSuccessNotification(
                            {
                              messageKey:
                                'sachbearbeitung-app.admin.sozialdienst.sozialdienstAdminErsetzt',
                            },
                          );
                        }
                      },
                    }),
                  ),
              ),
              catchError(() => {
                this.loadSozialdienst$({ sozialdienstId });

                return EMPTY;
              }),
            );
        },
      ),
    ),
  );

  deleteSozialdienst$ = rxMethod<Sozialdienst>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          sozialdienste: cachedPending(state.sozialdienste),
        }));
      }),
      exhaustMap((sozialdienst) =>
        this.keycloak
          .deleteUser$(sozialdienst.sozialdienstAdmin.keycloakId)
          .pipe(
            switchMap(() =>
              this.sozialdienstService
                .deleteSozialdienst$({ sozialdienstId: sozialdienst.id })
                .pipe(
                  handleApiResponse(
                    () => {
                      patchState(this, {
                        sozialdienst: initial(),
                      });
                    },
                    {
                      onSuccess: () => {
                        this.loadAllSozialdienste$();
                        this.globalNotificationStore.createSuccessNotification({
                          messageKey:
                            'sachbearbeitung-app.admin.sozialdienst.sozialdienstGeloescht',
                        });
                      },
                      onFailure: () => {
                        this.loadAllSozialdienste$();
                      },
                    },
                  ),
                ),
            ),
          ),
      ),
      catchError((error) => {
        this.loadAllSozialdienste$();
        return throwError(() => error);
      }),
    ),
  );
}
