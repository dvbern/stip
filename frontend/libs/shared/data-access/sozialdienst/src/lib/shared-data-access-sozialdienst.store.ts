import { Injectable, computed, inject } from '@angular/core';
import { Router } from '@angular/router';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import {
  EMPTY,
  catchError,
  exhaustMap,
  pipe,
  switchMap,
  tap,
  throwError,
} from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/global/notification';
import { SharedModelError } from '@dv/shared/model/error';
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
import {
  CachedRemoteData,
  RemoteData,
  cachedPending,
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
    sozialdienstCreate: SozialdienstCreate;
    onAfterSave?: (sozialdienstId: string) => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, {
          sozialdienst: pending(),
        });
      }),
      exhaustMap(({ sozialdienstCreate, onAfterSave }) =>
        this.sozialdienstService
          .createSozialdienst$({
            sozialdienstCreate,
          })
          .pipe(
            handleApiResponse(
              (sozialdienst) => {
                patchState(this, { sozialdienst });
              },
              {
                onSuccess: (newSozialdienst) => {
                  this.globalNotificationStore.createSuccessNotification({
                    messageKey:
                      'shared.admin.sozialdienst.sozialdienstErstellt',
                  });
                  onAfterSave?.(newSozialdienst.id);
                },
                onFailure: (error) => {
                  const parsedError = SharedModelError.parse(error);
                  if (parsedError.status === 409) {
                    this.globalNotificationStore.createNotification({
                      type: 'ERROR',
                      messageKey:
                        'shared.admin.sozialdienstBenutzer.error.emailExists',
                    });
                  }
                },
              },
            ),
          ),
      ),
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
    sozialdienstAdmin: SozialdienstAdmin;
  }>(
    pipe(
      tap(() => {
        patchState(this, {
          sozialdienst: pending(),
        });
      }),
      exhaustMap(({ sozialdienstId, sozialdienstAdmin }) =>
        this.sozialdienstService
          .replaceSozialdienstAdmin$({
            sozialdienstId,
            sozialdienstAdmin,
          })
          .pipe(
            handleApiResponse(() => undefined, {
              onSuccess: () => {
                this.loadSozialdienst$({ sozialdienstId });
                this.globalNotificationStore.createSuccessNotification({
                  messageKey:
                    'shared.admin.sozialdienst.sozialdienstAdminErsetzt',
                });
              },
            }),
          ),
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
                onFailure: (error) => {
                  const parsedError = SharedModelError.parse(error);
                  if (parsedError.status === 409) {
                    this.globalNotificationStore.createNotification({
                      type: 'ERROR',
                      messageKey:
                        'shared.admin.sozialdienstBenutzer.error.emailExists',
                    });
                  }
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

  setSozialdienstStatusTo$ = rxMethod<{
    sozialdienstId: string;
    aktiv: boolean;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          sozialdienste: cachedPending(state.sozialdienste),
        }));
      }),
      exhaustMap(({ sozialdienstId, aktiv }) =>
        this.sozialdienstService
          .setSozialdienstAktivTo$({ sozialdienstId, aktiv })
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
                      'shared.admin.sozialdienst.sozialdienstStatusChanged.' +
                      aktiv,
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
}
