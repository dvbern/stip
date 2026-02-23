import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import {
  UserConsent,
  UserConsentService,
  UserConsentServiceCreateUserConsentRequestParams,
} from '@dv/shared/model/gesuch';
import {
  RemoteData,
  handleApiResponse,
  initial,
  pending,
} from '@dv/shared/util/remote-data';

type UserConsentState = {
  userConsent: RemoteData<UserConsent>;
};

const initialState: UserConsentState = {
  userConsent: initial(),
};

@Injectable({ providedIn: 'root' }) // really necessary?
export class UserConsentStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private userConsentService = inject(UserConsentService);

  userConsentViewSig = computed(() => {
    return {
      userHasGivenConsent: this.userConsent.data()?.consentGiven ?? false,
    };
  });

  getUserConsent$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, {
          userConsent: pending(),
        });
      }),
      switchMap(() =>
        this.userConsentService
          .getUserConsent$()
          .pipe(
            handleApiResponse((userConsent) =>
              patchState(this, { userConsent }),
            ),
          ),
      ),
    ),
  );

  createUserConsent$ = rxMethod<{
    req: UserConsentServiceCreateUserConsentRequestParams;
    onSuccess?: () => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, {
          userConsent: pending(),
        });
      }),
      switchMap((params) =>
        this.userConsentService
          .createUserConsent$(params.req)
          .pipe(
            handleApiResponse(
              (userConsent) => patchState(this, { userConsent }),
              { onSuccess: params.onSuccess },
            ),
          ),
      ),
    ),
  );
}
