import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { OAuthService } from 'angular-oauth2-oidc';
import {
  exhaustMap,
  filter,
  map,
  pipe,
  switchMap,
  tap,
  throwIfEmpty,
} from 'rxjs';
import * as z from 'zod';

import {
  CachedRemoteData,
  RemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  pending,
  success,
} from '@dv/shared/util/remote-data';

const RoleList = z.array(z.object({ id: z.string(), name: z.string() }));
type RoleList = z.infer<typeof RoleList>;

const User = z.object({
  id: z.string(),
});
type User = z.infer<typeof User>;

type BenutzerverwaltungState = {
  availableRoles: CachedRemoteData<RoleList>;
  usearCreated: RemoteData<User>;
};

const initialState: BenutzerverwaltungState = {
  availableRoles: initial(),
  usearCreated: initial(),
};

@Injectable()
export class BenutzerverwaltungStore extends signalStore(
  withState(initialState),
  withDevtools('BenutzerverwaltungStore'),
) {
  private http = inject(HttpClient);
  private authService = inject(OAuthService);

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
    return fromCachedDataSig(this.usearCreated);
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
          .get<RoleList>(
            `${this.oauthParams.url}/auth/admin/realms/${this.oauthParams.realm}/roles`,
          )
          .pipe(
            map((roles) => RoleList.parse(roles)),
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
    roles: RoleList;
  }>(
    pipe(
      tap(() => {
        patchState(this, {
          usearCreated: pending(),
        });
      }),
      exhaustMap(({ name, vorname, email, roles }) =>
        this.http
          .post(
            `${this.oauthParams.url}/auth/admin/realms/${this.oauthParams.realm}/users`,
            {
              enabled: true,
              firstName: vorname,
              lastName: name,
              username: email,
              email,
            },
            {
              observe: 'response',
            },
          )
          .pipe(
            filter(hasLocationHeader),
            throwIfEmpty(() => new Error('User creation failed')),
            switchMap((response) =>
              this.http
                .get<User>(response.headers.get('Location'))
                .pipe(map((user) => User.parse(user))),
            ),
            switchMap((user) =>
              this.http
                .post(
                  `${this.oauthParams.url}/auth/admin/realms/${this.oauthParams.realm}/users/${user.id}/role-mappings/realm`,
                  roles,
                )
                .pipe(
                  handleApiResponse(() => {
                    patchState(this, { usearCreated: success(user) });
                  }),
                ),
            ),
          ),
      ),
    ),
  );
}

const hasLocationHeader = (
  response: HttpResponse<unknown>,
): response is HttpResponse<unknown> & {
  headers: { get: (header: 'Location') => string };
} => {
  return !response.headers.has('Location');
};
