import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { OAuthService } from 'angular-oauth2-oidc';
import {
  EMPTY,
  Observable,
  catchError,
  exhaustMap,
  filter,
  map,
  pipe,
  switchMap,
  tap,
  throwIfEmpty,
} from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/data-access/global-notification';
import {
  SharedModelBenutzer,
  SharedModelRoleList,
  SharedModelUserAdminError,
} from '@dv/shared/model/benutzer';
import { noGlobalErrorsIf } from '@dv/shared/util/http';
import {
  CachedRemoteData,
  RemoteData,
  cachedPending,
  failure,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  pending,
  success,
} from '@dv/shared/util/remote-data';

const USABLE_ROLES = ['Sachbearbeiter', 'Admin'];

type HttpResponseWithLocation = HttpResponse<unknown> & {
  headers: { get: (header: 'Location') => string };
};
type BenutzerverwaltungState = {
  availableRoles: CachedRemoteData<SharedModelRoleList>;
  userCreated: RemoteData<SharedModelBenutzer>;
};

const initialState: BenutzerverwaltungState = {
  availableRoles: initial(),
  userCreated: initial(),
};

@Injectable()
export class BenutzerverwaltungStore extends signalStore(
  withState(initialState),
  withDevtools('BenutzerverwaltungStore'),
) {
  private http = inject(HttpClient);
  private authService = inject(OAuthService);
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

  usearCreatedViewSig = computed(() => {
    return fromCachedDataSig(this.userCreated);
  });

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
    onAfterSave?: () => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, {
          userCreated: pending(),
        });
      }),
      exhaustMap(({ name, vorname, email, roles, onAfterSave }) =>
        this.createUser$(vorname, name, email).pipe(
          filter(hasLocationHeader),
          throwIfEmpty(() => new Error('User creation failed')),
          switchMap((response) => this.loadUser$(response)),
          switchMap((user) => this.asignRoles(user, roles)),
          switchMap((user) =>
            this.notifyUser$(user).pipe(
              handleApiResponse(
                () => {
                  patchState(this, { userCreated: success(user) });
                },
                {
                  onSuccess: (notifyUserWasSuccessfull) => {
                    onAfterSave?.();
                    if (notifyUserWasSuccessfull) {
                      this.globalNotificationStore.createSuccessNotification({
                        messageKey:
                          'sachbearbeitung-app.admin.benutzerverwaltung.benutzerErstellt',
                      });
                    }
                  },
                },
              ),
            ),
          ),
          catchError((error) => {
            patchState(this, { userCreated: failure(error) });
            return EMPTY;
          }),
        ),
      ),
    ),
  );

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
        },
        {
          observe: 'response',
          context: noGlobalErrorsIf(true),
        },
      )
      .pipe(this.interceptError('erstellen'));
  }

  private loadUser$(response: HttpResponseWithLocation) {
    return this.http
      .get<SharedModelBenutzer>(response.headers.get('Location'), {
        context: noGlobalErrorsIf(true),
      })
      .pipe(
        map((user) => SharedModelBenutzer.parse(user)),
        this.interceptError('laden'),
      );
  }

  private asignRoles(
    user: { id: string },
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

  private notifyUser$(user: { id: string }) {
    return this.http
      .put(
        `${this.oauthParams.url}/admin/realms/${this.oauthParams.realm}/users/${user.id}/execute-actions-email?redirect_uri=http://localhost:4201&client_id=stip-gesuch-app`,
        ['UPDATE_PASSWORD'],
        {
          context: noGlobalErrorsIf(true),
        },
      )
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
          const parsedErrorType = toKnownUserError(error, fallbackErrorType);
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

const hasLocationHeader = (
  response: HttpResponse<unknown>,
): response is HttpResponseWithLocation => {
  return response.headers.has('Location');
};

const byUsableRoles = (role: SharedModelRoleList[number]) => {
  return USABLE_ROLES.includes(role.name);
};

const toKnownUserError = (error: unknown, fallbackType: string) => {
  const parsed = SharedModelUserAdminError.parse(error);
  return parsed.type ?? fallbackType;
};