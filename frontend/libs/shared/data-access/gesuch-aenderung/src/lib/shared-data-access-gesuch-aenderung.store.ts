import { Injectable, inject } from '@angular/core';
import { Router } from '@angular/router';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/global/notification';
import { isSharedModelError } from '@dv/shared/model/error';
import {
  CreateAenderungsantragRequest,
  CreateGesuchTrancheRequest,
  GesuchTranche,
  GesuchTrancheService,
  GesuchTrancheStatus,
  PatchAenderungsInfoRequest,
  getTrancheRoute,
} from '@dv/shared/model/gesuch';
import { PERSON } from '@dv/shared/model/gesuch-form';
import { shouldIgnoreBadRequestErrorsIf } from '@dv/shared/util/http';
import {
  CachedRemoteData,
  cachedPending,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';

type GesuchAenderungState = {
  cachedGesuchAenderung: CachedRemoteData<GesuchTranche>;
};

const initialState: GesuchAenderungState = {
  cachedGesuchAenderung: initial(),
};

const EXPECTED_ERRORS: Record<string, string> = {
  '{jakarta.validation.constraints.gesuchTranche.daterangeTooShort.message}':
    'shared.form.error.tranche.daterangeTooShort',
};

export type AenderungChangeState = Extract<
  GesuchTrancheStatus,
  'MANUELLE_AENDERUNG' | 'AKZEPTIERT' | 'ABGELEHNT'
>;

@Injectable({ providedIn: 'root' })
export class GesuchAenderungStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private gesuchTrancheService = inject(GesuchTrancheService);
  private globalNotificationStore = inject(GlobalNotificationStore);
  private router = inject(Router);

  createGesuchAenderung$ = rxMethod<{
    gesuchId: string;
    createAenderungsantragRequest: CreateAenderungsantragRequest;
    onSuccess: () => void;
    onFailure: (error: unknown) => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          // change to not cached?
          cachedGesuchAenderung: cachedPending(state.cachedGesuchAenderung),
        }));
      }),
      switchMap(
        ({ gesuchId, createAenderungsantragRequest, onSuccess, onFailure }) =>
          this.gesuchTrancheService
            .createAenderungsantrag$({
              gesuchId,
              createAenderungsantragRequest,
            })
            .pipe(
              handleApiResponse(
                (gesuchAenderung) => {
                  patchState(this, () => ({
                    cachedGesuchAenderung: gesuchAenderung,
                  }));
                },
                {
                  onSuccess: (data) => {
                    this.globalNotificationStore.createSuccessNotification({
                      messageKey:
                        'shared.dialog.gesuch-aenderung.create.success',
                    });
                    this.router.navigate([
                      'gesuch',
                      PERSON.route,
                      gesuchId,
                      getTrancheRoute('aenderung'),
                      data.id,
                    ]);
                    onSuccess();
                  },
                  onFailure,
                },
              ),
            ),
      ),
    ),
  );

  updateAenderungVonBis$ = rxMethod<{
    aenderungId: string;
    patchAenderungsInfoRequest: PatchAenderungsInfoRequest;
    onSuccess: () => void;
    onFailure: (error: unknown) => void;
  }>(
    pipe(
      switchMap(
        ({ aenderungId, patchAenderungsInfoRequest, onSuccess, onFailure }) =>
          this.gesuchTrancheService
            .patchAenderungInfo$({
              aenderungId,
              patchAenderungsInfoRequest,
            })
            .pipe(
              handleApiResponse(() => undefined, {
                onSuccess: () => {
                  this.globalNotificationStore.createSuccessNotification({
                    messageKey:
                      'shared.dialog.gesuch-aenderung.update-von-bis.success',
                  });
                  onSuccess();
                },
                onFailure,
              }),
            ),
      ),
    ),
  );

  deleteGesuchAenderung$ = rxMethod<{
    aenderungId: string;
    onSuccess?: () => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedGesuchAenderung: cachedPending(state.cachedGesuchAenderung),
        }));
      }),
      switchMap(({ aenderungId, onSuccess }) =>
        this.gesuchTrancheService.deleteAenderung$({ aenderungId }).pipe(
          handleApiResponse(
            () => {
              patchState(this, () => ({
                cachedGesuchAenderung: initial(),
              }));
            },
            {
              onSuccess: () => {
                this.globalNotificationStore.createSuccessNotification({
                  messageKey: 'shared.dialog.gesuch-aenderung.delete.success',
                });
                onSuccess?.();
              },
            },
          ),
        ),
      ),
    ),
  );

  createGesuchTrancheCopy$ = rxMethod<{
    gesuchId: string;
    createGesuchTrancheRequest?: CreateGesuchTrancheRequest;
    onSuccess: () => void;
    onFailure: (error: unknown) => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedGesuchAenderung: cachedPending(state.cachedGesuchAenderung),
        }));
      }),
      switchMap(
        ({ gesuchId, createGesuchTrancheRequest, onSuccess, onFailure }) =>
          this.gesuchTrancheService
            .createGesuchTrancheCopy$({
              gesuchId,
              createGesuchTrancheRequest,
            })
            .pipe(
              handleApiResponse(
                (gesuchAenderung) => {
                  patchState(this, () => ({
                    cachedGesuchAenderung: gesuchAenderung,
                  }));
                },
                {
                  onSuccess: () => {
                    this.globalNotificationStore.createSuccessNotification({
                      messageKey: 'shared.dialog.gesuch.tranche.create.success',
                    });
                    onSuccess();
                  },
                  onFailure,
                },
              ),
            ),
      ),
    ),
  );

  changeAenderungState$ = rxMethod<{
    aenderungId: string;
    gesuchId: string;
    comment: string;
    target: AenderungChangeState;
    onSuccess: (trancheId: string) => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedGesuchAenderung: cachedPending(state.cachedGesuchAenderung),
        }));
      }),
      switchMap(
        ({ aenderungId, target, comment, onSuccess: additionalOnSuccess }) => {
          const services$ = {
            AKZEPTIERT: () =>
              this.gesuchTrancheService.aenderungAkzeptieren$(
                { aenderungId },
                undefined,
                undefined,
                {
                  context: shouldIgnoreBadRequestErrorsIf(true),
                },
              ),
            ABGELEHNT: () =>
              this.gesuchTrancheService.aenderungAblehnen$({
                aenderungId,
                kommentar: { text: comment },
              }),
            MANUELLE_AENDERUNG: () =>
              this.gesuchTrancheService.aenderungManuellAnpassen$({
                aenderungId,
              }),
          } satisfies Record<AenderungChangeState, unknown>;

          return services$[target]().pipe(
            handleApiResponse(
              (gesuchAenderung) => {
                patchState(this, () => ({
                  cachedGesuchAenderung: gesuchAenderung,
                }));
              },
              {
                onSuccess: (value) => {
                  this.globalNotificationStore.createSuccessNotification({
                    messageKey: `shared.dialog.gesuch-aenderung.${target}.success`,
                  });
                  additionalOnSuccess(value.id);
                },
                onFailure: handleKnownErrors(this.globalNotificationStore),
              },
            ),
          );
        },
      ),
    ),
  );
}

const handleKnownErrors =
  (notificationStore: GlobalNotificationStore) =>
  (error: unknown): void => {
    if (
      isSharedModelError(error) &&
      error.type === 'validationError' &&
      EXPECTED_ERRORS[error.messageKey]
    ) {
      notificationStore.createNotification({
        type: 'ERROR',
        messageKey: EXPECTED_ERRORS[error.messageKey],
      });
    }
  };
