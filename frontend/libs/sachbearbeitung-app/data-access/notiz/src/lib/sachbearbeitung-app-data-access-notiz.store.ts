import { Injectable, Signal, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { OAuthService } from 'angular-oauth2-oidc';
import { pipe, switchMap, tap } from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/data-access/global-notification';
import {
  GesuchNotiz,
  GesuchNotizCreate,
  GesuchNotizService,
  GesuchNotizUpdate,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';

type NotizState = {
  notizen: CachedRemoteData<GesuchNotiz[]>;
  notiz: CachedRemoteData<GesuchNotiz>;
  userIsJurist: boolean;
};

const initialState: NotizState = {
  notizen: initial(),
  notiz: initial(),
  userIsJurist: false,
};

/**
 * Decodes a JWT token and returns the payload as a JSON object.
 *
 * @param token - The JWT token to decode.
 * @returns The decoded payload as an object, or null if decoding fails.
 */
function decodeJwt<T extends object = { [key: string]: any }>(
  token: string,
): T | null {
  try {
    // Split the token into its parts
    const parts = token.split('.');
    if (parts.length !== 3) {
      throw new Error('Invalid JWT format');
    }

    // Base64Url decode the payload
    const base64Url = parts[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join(''),
    );

    // Parse the JSON payload
    return JSON.parse(jsonPayload);
  } catch (error) {
    console.error('Failed to decode JWT:', error);
    return null;
  }
}

@Injectable()
export class NotizStore extends signalStore(
  { protectedState: false },
  withState(initialState),
  withDevtools('NotizStore'),
) {
  private notizService = inject(GesuchNotizService);
  private notificationStore = inject(GlobalNotificationStore);
  private authService = inject(OAuthService);

  notizenListViewSig = computed(() => {
    return fromCachedDataSig(this.notizen);
  });

  notizViewSig = computed(() => {
    return fromCachedDataSig(this.notiz);
  });

  setJurist() {
    // randomly set userIsJurist to true or false
    patchState(this, { userIsJurist: Math.random() < 0.5 });
  }

  loadNotizen$ = rxMethod<{ gesuchId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          notizen: cachedPending(state.notizen),
        }));
      }),
      switchMap(({ gesuchId }) =>
        this.notizService
          .getNotizen$({ gesuchId })
          .pipe(handleApiResponse((notizen) => patchState(this, { notizen }))),
      ),
    ),
  );

  loadNotiz$ = rxMethod<{ notizId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          notiz: cachedPending(state.notiz),
        }));
      }),
      switchMap(({ notizId }) =>
        this.notizService
          .getNotiz$({ notizId })
          .pipe(handleApiResponse((notiz) => patchState(this, { notiz }))),
      ),
    ),
  );

  editNotiz = rxMethod<{
    gesuchId: string;
    notizDaten: GesuchNotizUpdate;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          notiz: cachedPending(state.notiz),
        }));
      }),
      switchMap(({ gesuchId, notizDaten }) =>
        this.notizService
          .updateNotiz$({
            gesuchNotizUpdate: notizDaten,
          })
          .pipe(
            handleApiResponse(
              (notiz) => {
                patchState(this, { notiz });
              },
              {
                onSuccess: () => {
                  this.notificationStore.createSuccessNotification({
                    messageKey:
                      'sachbearbeitung-app.infos.notiz.bearbeiten.success',
                  });
                  this.loadNotizen$({ gesuchId });
                },
              },
            ),
          ),
      ),
    ),
  );

  createNotiz = rxMethod<{
    gesuchNotizCreate: GesuchNotizCreate;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          notiz: cachedPending(state.notiz),
        }));
      }),
      switchMap(({ gesuchNotizCreate }) =>
        this.notizService
          .createNotiz$({
            gesuchNotizCreate,
          })
          .pipe(
            handleApiResponse(
              (notiz) => {
                patchState(this, { notiz });
              },
              {
                onSuccess: () => {
                  this.notificationStore.createSuccessNotification({
                    messageKey:
                      'sachbearbeitung-app.infos.notiz.erstellen.success',
                  });
                  this.loadNotizen$({ gesuchId: gesuchNotizCreate.gesuchId });
                },
              },
            ),
          ),
      ),
    ),
  );

  deleteNotiz = rxMethod<{
    gesuchId: string;
    notizId: string;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          notiz: cachedPending(state.notiz),
        }));
      }),
      switchMap(({ gesuchId, notizId }) =>
        this.notizService
          .deleteNotiz$({
            notizId,
          })
          .pipe(
            handleApiResponse(
              (notiz) => {
                patchState(this, { notiz });
              },
              {
                onSuccess: () => {
                  this.notificationStore.createSuccessNotification({
                    messageKey:
                      'sachbearbeitung-app.infos.notiz.delete.success',
                  });
                  this.loadNotizen$({ gesuchId });
                },
              },
            ),
          ),
      ),
    ),
  );
}
