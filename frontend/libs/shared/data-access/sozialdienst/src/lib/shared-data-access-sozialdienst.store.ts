import { Injectable, computed, inject } from '@angular/core';
import { Router } from '@angular/router';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import {
  catchError,
  exhaustMap,
  map,
  pipe,
  switchMap,
  tap,
  throwError,
} from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/global/notification';
import {
  DelegierenService,
  DelegierenServiceFallDelegierenRequestParams,
  Sozialdienst,
  SozialdienstAdmin,
  SozialdienstBenutzer,
  SozialdienstBenutzerCreate,
  SozialdienstBenutzerUpdate,
  SozialdienstCreate,
  SozialdienstService,
  SozialdienstSlim,
  SozialdienstUpdate,
} from '@dv/shared/model/gesuch';
import { handleUnauthorized } from '@dv/shared/util/http';
import { KeycloakHttpService } from '@dv/shared/util/keycloak-http';
import {
  CachedRemoteData,
  RemoteData,
  cachedPending,
  failure,
  handleApiResponse,
  initial,
  mapCachedData,
  optimisticCachedPending,
  pending,
  success,
} from '@dv/shared/util/remote-data';

type SozialdienstState = {
  delegierung: RemoteData<unknown>;
  sozialdienste: CachedRemoteData<Sozialdienst[]>;
  availableSozialdienste: CachedRemoteData<SozialdienstSlim[]>;
  sozialdienst: RemoteData<Sozialdienst>;
  sozialdienstBenutzerList: CachedRemoteData<SozialdienstBenutzer[]>;
  sozialdienstBenutzer: CachedRemoteData<SozialdienstBenutzer>;
};

const initialState: SozialdienstState = {
  delegierung: initial(),
  sozialdienste: initial(),
  availableSozialdienste: initial(),
  sozialdienst: initial(),
  sozialdienstBenutzerList: initial(),
  sozialdienstBenutzer: initial(),
};

export type SozialdienstBenutzerViewEntry = SozialdienstBenutzer & {
  name: string;
};

@Injectable()
export class SozialdienstStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private router = inject(Router);
  private sozialdienstService = inject(SozialdienstService);
  private delegierenService = inject(DelegierenService);
  private keycloak = inject(KeycloakHttpService);
  private globalNotificationStore = inject(GlobalNotificationStore);

  sozialdienstBenutzersView = computed(() => {
    const benutzers = this.sozialdienstBenutzerList();

    return mapCachedData(benutzers, (data) =>
      data.map(
        (benutzer) =>
          ({
            ...benutzer,
            name: `${benutzer.vorname} ${benutzer.nachname}`,
          }) satisfies SozialdienstBenutzerViewEntry,
      ),
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

  loadAvailableSozialdienste$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          sozialdienste: cachedPending(state.sozialdienste),
        }));
      }),
      switchMap(() =>
        this.sozialdienstService
          .getAllSozialdiensteForDelegation$()
          .pipe(
            handleApiResponse((availableSozialdienste) =>
              patchState(this, { availableSozialdienste }),
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
      sozialdienstAdmin: Omit<SozialdienstAdmin, 'keycloakId' | 'id'>;
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
                                'shared.admin.sozialdienst.sozialdienstErstellt',
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

              return throwError(() => error);
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
        const sozialdienstUpdate: SozialdienstUpdate = {
          zahlungsverbindung: sozialdienst.zahlungsverbindung,
          id: sozialdienst.id,
          name: sozialdienst.name,
        };

        const sozialdienstBenutzerUpdate: SozialdienstBenutzerUpdate = {
          id: sozialdienst.sozialdienstAdmin.id,
          nachname: sozialdienst.sozialdienstAdmin.nachname,
          vorname: sozialdienst.sozialdienstAdmin.vorname,
        };

        return this.sozialdienstService
          .updateSozialdienstAdmin$({
            sozialdienstBenutzerUpdate,
          })
          .pipe(
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
                            'shared.admin.sozialdienst.sozialdienstAktualisiert',
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
            catchError((error) => {
              this.loadSozialdienst$({
                sozialdienstId: sozialdienst.id,
              });

              return throwError(() => error);
            }),
          );
      }),
    ),
  );

  replaceSozialdienstAdmin$ = rxMethod<{
    sozialdienstId: string;
    newUser: Omit<SozialdienstAdmin, 'keycloakId'>;
  }>(
    pipe(
      tap(() => {
        patchState(this, {
          sozialdienst: pending(),
        });
      }),
      exhaustMap(({ sozialdienstId, newUser }) => {
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
                        this.globalNotificationStore.createSuccessNotification({
                          messageKey:
                            'shared.admin.sozialdienst.sozialdienstAdminErsetzt',
                        });
                      }
                    },
                  }),
                ),
            ),
            catchError((error) => {
              this.loadSozialdienst$({ sozialdienstId });

              return throwError(() => error);
            }),
          );
      }),
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
                      'shared.admin.sozialdienst.sozialdienstGeloescht',
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

  fallDelegieren$ = rxMethod<{
    req: DelegierenServiceFallDelegierenRequestParams;
    onSuccess?: () => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, {
          delegierung: pending(),
        });
      }),
      exhaustMap(({ req, onSuccess }) =>
        this.delegierenService.fallDelegieren$(req).pipe(
          handleApiResponse(
            (delegierung) => patchState(this, { delegierung }),
            {
              onSuccess,
            },
          ),
        ),
      ),
    ),
  );

  loadSozialdienstBenutzerList$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          sozialdienstBenutzerList: cachedPending(
            state.sozialdienstBenutzerList,
          ),
        }));
      }),
      switchMap(() =>
        this.sozialdienstService.getSozialdienstBenutzerList$().pipe(
          handleApiResponse((benutzer) => {
            patchState(this, { sozialdienstBenutzerList: benutzer });
          }),
        ),
      ),
    ),
  );

  resetSozialdienstBenutzerCache = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          sozialdienstBenutzer: initial(),
        }));
      }),
    ),
  );

  loadSozialdienstBenutzer$ = rxMethod<{ sozialdienstBenutzerId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          sozialdienstBenutzer: cachedPending(state.sozialdienstBenutzer),
        }));
      }),
      switchMap(({ sozialdienstBenutzerId }) =>
        this.sozialdienstService
          .getSozialdienstBenutzer$(
            { sozialdienstBenutzerId },
            undefined,
            undefined,
            {
              context: handleUnauthorized((error) => {
                this.globalNotificationStore.createNotification({
                  type: 'ERROR_PERMANENT',
                  messageKey: 'shared.genericError.unauthorized',
                  content: error,
                });
                this.router.navigate(['/'], { replaceUrl: true });
              }),
            },
          )
          .pipe(
            handleApiResponse((benutzer) =>
              patchState(this, { sozialdienstBenutzer: benutzer }),
            ),
          ),
      ),
    ),
  );

  updateSozialdienstBenutzer$ = rxMethod<{
    sozialdienstBenutzerUpdate: SozialdienstBenutzerUpdate;
  }>(
    pipe(
      tap(({ sozialdienstBenutzerUpdate }) => {
        patchState(this, (state) => ({
          sozialdienstBenutzer: optimisticCachedPending(
            state.sozialdienstBenutzer,
            sozialdienstBenutzerUpdate,
          ),
        }));
      }),
      exhaustMap((sozialdienstBenutzerUpdate) =>
        this.sozialdienstService
          .updateSozialdienstBenutzer$(sozialdienstBenutzerUpdate)
          .pipe(
            handleApiResponse(
              (sozialdienstBenutzer) => {
                patchState(this, { sozialdienstBenutzer });
              },
              {
                onSuccess: () => {
                  this.globalNotificationStore.createSuccessNotification({
                    messageKey:
                      'shared.admin.sozialdienstBenutzer.aktualisiert',
                  });
                },
              },
            ),
          ),
      ),
    ),
  );

  createSozialdienstBenutzer$ = rxMethod<{
    sozialdienstBenutzerCreate: SozialdienstBenutzerCreate;
    onAfterSave?: (sozialdienstId: string) => void;
  }>(
    pipe(
      tap(({ sozialdienstBenutzerCreate }) => {
        patchState(this, () => ({
          sozialdienstBenutzer: cachedPending(
            success({ id: 'new', ...sozialdienstBenutzerCreate }),
          ),
        }));
      }),
      exhaustMap(({ sozialdienstBenutzerCreate, onAfterSave }) =>
        this.sozialdienstService
          .createSozialdienstBenutzer$({ sozialdienstBenutzerCreate })
          .pipe(
            handleApiResponse(
              (sozialdienstBenutzer) => {
                patchState(this, { sozialdienstBenutzer });
              },
              {
                onSuccess: (sozialdienstBenutzer) => {
                  this.globalNotificationStore.createSuccessNotification({
                    messageKey: 'shared.admin.sozialdienstBenutzer.erstellt',
                  });
                  onAfterSave?.(sozialdienstBenutzer.id);
                },
              },
            ),
          ),
      ),
    ),
  );

  deleteSozialdienstBenutzer$ = rxMethod<{ sozialdienstBenutzerId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          sozialdienstBenutzerList: cachedPending(
            state.sozialdienstBenutzerList,
          ),
        }));
      }),
      switchMap(({ sozialdienstBenutzerId }) =>
        this.sozialdienstService
          .deleteSozialdienstBenutzer$({ sozialdienstBenutzerId })
          .pipe(
            handleApiResponse(() => {
              this.loadSozialdienstBenutzerList$();
            }),
          ),
      ),
    ),
  );
}
