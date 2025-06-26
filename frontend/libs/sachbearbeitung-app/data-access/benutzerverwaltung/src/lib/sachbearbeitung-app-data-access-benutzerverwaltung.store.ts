import { Injectable, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { EMPTY, catchError, map, pipe, switchMap, tap } from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/global/notification';
import {
  SharedModelSachbearbeiter,
  mapToSachbearbeiterWithKnownRoles,
} from '@dv/shared/model/benutzer';
import { SharedModelError } from '@dv/shared/model/error';
import { BenutzerService, SachbearbeiterUpdate } from '@dv/shared/model/gesuch';
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

type BenutzerverwaltungState = {
  benutzers: CachedRemoteData<SharedModelSachbearbeiter[]>;
  benutzer: RemoteData<SharedModelSachbearbeiter>;
};

const initialState: BenutzerverwaltungState = {
  benutzers: initial(),
  benutzer: initial(),
};

@Injectable()
export class BenutzerverwaltungStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private benutzerService = inject(BenutzerService);

  private globalNotificationStore = inject(GlobalNotificationStore);

  loadAllSbAppBenutzers$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          benutzers: cachedPending(state.benutzers),
        }));
      }),
      switchMap(() =>
        this.benutzerService.getSachbearbeitersForManagement$().pipe(
          map((benutzers) => benutzers.map(mapToSachbearbeiterWithKnownRoles)),
          handleApiResponse((benutzers) => patchState(this, { benutzers }), {
            onFailure: (error) => {
              const parsedError = SharedModelError.parse(error);
              if (parsedError.type === 'zodError') {
                console.error(parsedError.message, parsedError.errors);
              }
              this.globalNotificationStore.handleHttpRequestFailed([
                parsedError,
              ]);
            },
          }),
        ),
      ),
    ),
  );

  loadBenutzerWithRoles$ = rxMethod<string>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          benutzer: pending(),
        }));
      }),
      switchMap((sachbearbeiterId) =>
        this.benutzerService
          .getSachbearbeiterForManagement$({
            sachbearbeiterId,
          })
          .pipe(
            map(mapToSachbearbeiterWithKnownRoles),
            handleApiResponse((benutzer) => patchState(this, { benutzer })),
          ),
      ),
    ),
  );

  updateBenutzer$ = rxMethod<{
    userId: string;
    user: SachbearbeiterUpdate;
  }>(
    pipe(
      tap(() => {
        patchState(this, {
          benutzer: pending(),
        });
      }),
      switchMap(({ userId, user }) =>
        this.benutzerService
          .updateSachbearbeiter$({
            sachbearbeiterId: userId,
            sachbearbeiterUpdate: user,
          })
          .pipe(
            map(mapToSachbearbeiterWithKnownRoles),
            handleApiResponse((benutzer) => patchState(this, { benutzer }), {
              onSuccess: (benutzer) => {
                this.globalNotificationStore.createSuccessNotification({
                  messageKey:
                    'sachbearbeitung-app.admin.benutzerverwaltung.benutzerBearbeitet',
                });
                patchState(this, { benutzer: success(benutzer) });
              },
              onFailure: () => {
                this.loadBenutzerWithRoles$(userId);
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
        this.benutzerService
          .deleteSachbearbeiter$({ sachbearbeiterId: benutzerId })
          .pipe(
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

  registerUser$ = rxMethod<{
    user: SachbearbeiterUpdate;
    onAfterSave?: (userId: string) => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, {
          benutzer: pending(),
        });
      }),
      switchMap(({ onAfterSave, user }) =>
        this.benutzerService
          .createSachbearbeiter$({
            sachbearbeiterUpdate: user,
          })
          .pipe(
            map(mapToSachbearbeiterWithKnownRoles),
            handleApiResponse((benutzer) => patchState(this, { benutzer }), {
              onSuccess: (newUser) => {
                this.globalNotificationStore.createSuccessNotification({
                  messageKey:
                    'sachbearbeitung-app.admin.benutzerverwaltung.benutzerErstellt',
                });
                onAfterSave?.(newUser.id);
              },
              onFailure: (error) => {
                const parsedError = SharedModelError.parse(error);
                if (parsedError.status === 409) {
                  this.globalNotificationStore.createNotification({
                    type: 'ERROR',
                    messageKey:
                      'sachbearbeitung-app.admin.benutzerverwaltung.benutzerErstellenFehler.emailExists',
                  });
                }
              },
            }),
            catchError((error) => {
              patchState(this, { benutzer: failure(error) });
              return EMPTY;
            }),
          ),
      ),
    ),
  );
}
