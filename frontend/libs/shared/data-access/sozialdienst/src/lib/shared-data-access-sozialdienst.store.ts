import { Injectable, computed, inject } from '@angular/core';
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

import { GlobalNotificationStore } from '@dv/shared/global/notification';
import { SharedModelBenutzerApi } from '@dv/shared/model/benutzer';
import {
  Sozialdienst,
  SozialdienstAdmin,
  SozialdienstAdminUpdate,
  SozialdienstBenutzer,
  SozialdienstCreate,
  SozialdienstService,
  SozialdienstUpdate,
} from '@dv/shared/model/gesuch';
import { KeycloakHttpService } from '@dv/shared/util/keycloak-http';
import {
  CachedRemoteData,
  RemoteData,
  cachedPending,
  failure,
  handleApiResponse,
  initial,
  mapCachedData,
  pending,
  success,
} from '@dv/shared/util/remote-data';

type SozialdienstState = {
  sozialdienste: CachedRemoteData<Sozialdienst[]>;
  sozialdienst: RemoteData<Sozialdienst>;
  sozialdienstBenutzers: CachedRemoteData<SozialdienstBenutzer[]>;
};

const initialState: SozialdienstState = {
  sozialdienste: initial(),
  sozialdienst: initial(),
  sozialdienstBenutzers: initial(),
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

  sozialdienstBenutzersView = computed(() => {
    const benutzers = this.sozialdienstBenutzers();

    return mapCachedData(benutzers, (data) =>
      data.map((benutzer) => ({
        ...benutzer,
        name: `${benutzer.vorname} ${benutzer.nachname}`,
      })),
    );
  });

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
      sozialdienstAdmin: Omit<SozialdienstAdmin, 'keycloakId'>;
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
          id: sozialdienst.sozialdienstAdmin.id,
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
    newUser: Omit<SozialdienstAdmin, 'keycloakId'>;
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
                  sozialdienstAdmin: {
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
      catchError((error) => {
        this.loadAllSozialdienste$();
        return throwError(() => error);
      }),
    ),
  );

  loadSozialdienstBenutzer$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          sozialdienstBenutzers: cachedPending(state.sozialdienstBenutzers),
        }));
      }),
      switchMap(() =>
        this.sozialdienstService.getSozialdienstBenutzer$().pipe(
          handleApiResponse((benutzer) => {
            patchState(this, { sozialdienstBenutzers: benutzer });
          }),
        ),
      ),
    ),
  );

  deleteSozialdienstBenutzer$ = rxMethod<{ benutzerId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          sozialdienstBenutzers: cachedPending(state.sozialdienstBenutzers),
        }));
      }),
      switchMap(({ benutzerId }) =>
        this.sozialdienstService
          .deleteSozialdienstBenutzer$({ body: benutzerId })
          .pipe(
            handleApiResponse(() => {
              this.loadSozialdienstBenutzer$();
            }),
          ),
      ),
    ),
  );
}
