import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { tapResponse } from '@ngrx/operators';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { EMPTY, catchError, pipe, switchMap, tap } from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/data-access/global-notification';
import {
  DokumentService,
  DokumentTyp,
  Dokumentstatus,
  GesuchDokument,
  GesuchService,
  GesuchTrancheService,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  isSuccess,
  success,
} from '@dv/shared/util/remote-data';

type DokumentsState = {
  dokuments: CachedRemoteData<GesuchDokument[]>;
  requiredDocumentTypes: CachedRemoteData<DokumentTyp[]>;
  dokument: CachedRemoteData<GesuchDokument>;
};

const initialState: DokumentsState = {
  dokuments: initial(),
  requiredDocumentTypes: initial(),
  dokument: initial(),
};

@Injectable({ providedIn: 'root' })
export class DokumentsStore extends signalStore(
  { protectedState: false },
  withState(initialState),
  withDevtools('DokumentsStore'),
) {
  private dokumentService = inject(DokumentService);
  private gesuchService = inject(GesuchService);
  private trancheService = inject(GesuchTrancheService);
  private globalNotificationStore = inject(GlobalNotificationStore);

  dokumenteViewSig = computed(() => ({
    dokuments: fromCachedDataSig(this.dokuments) ?? [],
    requiredDocumentTypes: fromCachedDataSig(this.requiredDocumentTypes) ?? [],
  }));

  dokumentViewSig = computed(() =>
    isSuccess(this.dokument()) ? this.dokument().data : undefined,
  );

  hasAbgelehnteDokumentsSig = computed(() => {
    return (
      this.dokuments
        .data()
        ?.some((dokument) => dokument.status === Dokumentstatus.ABGELEHNT) ??
      false
    );
  });
  hasAusstehendeDokumentsSig = computed(() => {
    return (
      this.dokuments
        .data()
        ?.some((dokument) => dokument.status === Dokumentstatus.AUSSTEHEND) ??
      false
    );
  });

  getGesuchDokument$ = rxMethod<{
    trancheId: string;
    dokumentTyp: DokumentTyp;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          dokument: cachedPending(state.dokument),
        }));
      }),
      switchMap(({ trancheId, dokumentTyp }) =>
        this.trancheService.getGesuchDokument$({
          gesuchTrancheId: trancheId,
          dokumentTyp,
        }),
      ),
      handleApiResponse((dokument) => patchState(this, { dokument })),
    ),
  );

  resetGesuchDokumentStateToInitial = rxMethod(
    tap(() => {
      patchState(this, {
        dokument: initial(),
      });
    }),
  );

  gesuchDokumentAblehnen$ = rxMethod<{
    gesuchDokumentId: string;
    kommentar: string;
    afterSuccess?: () => void;
  }>(
    pipe(
      switchMap(({ gesuchDokumentId, kommentar, afterSuccess }) =>
        this.dokumentService
          .gesuchDokumentAblehnen$({
            gesuchDokumentId,
            gesuchDokumentAblehnenRequest: {
              kommentar,
            },
          })
          .pipe(
            tapResponse({
              next: () => {
                this.globalNotificationStore.createSuccessNotification({
                  messageKey: 'shared.dokumente.reject.success',
                });
                afterSuccess?.();
              },
              error: () => undefined,
            }),
          ),
      ),
    ),
  );

  gesuchDokumentAkzeptieren$ = rxMethod<{
    gesuchDokumentId: string;
    afterSuccess?: () => void;
  }>(
    pipe(
      switchMap(({ gesuchDokumentId, afterSuccess }) =>
        this.dokumentService
          .gesuchDokumentAkzeptieren$({
            gesuchDokumentId,
          })
          .pipe(
            tapResponse({
              next: () => {
                this.globalNotificationStore.createSuccessNotification({
                  messageKey: 'shared.dokumente.accept.success',
                });
                afterSuccess?.();
              },
              error: () => undefined,
            }),
          ),
      ),
    ),
  );

  //TODO: akzeptieren und ablehnen umbenennen fuer liste und duplizieren und umschreiben fuer einzeln

  /**
   * Send missing documents to the backend
   * only possible if there are documents in status "AGBELEHNT"
   * will trigger an email to the gesuchsteller
   */
  fehlendeDokumenteUebermitteln$ = rxMethod<{
    gesuchId: string;
    trancheId: string;
    onSuccess: () => void;
  }>(
    pipe(
      switchMap(({ gesuchId, trancheId, onSuccess }) => {
        return this.gesuchService
          .gesuchFehlendeDokumenteUebermitteln$({ gesuchId })
          .pipe(
            tap(() => {
              patchState(this, (state) => ({
                dokuments: cachedPending(state.dokuments),
              }));
            }),
            switchMap(() =>
              this.trancheService.getGesuchDokumente$({
                gesuchTrancheId: trancheId,
              }),
            ),
            tapResponse({
              next: (dokuments) => {
                patchState(this, { dokuments: success(dokuments) });
                this.globalNotificationStore.createSuccessNotification({
                  messageKey: 'shared.dokumente.uebermitteln.success',
                });
                onSuccess();
              },
              error: () => {
                patchState(this, (state) => ({
                  dokuments: success(state.dokuments.data ?? []),
                }));
              },
            }),
            catchError(() => {
              return EMPTY;
            }),
          );
      }),
    ),
  );

  getDokumenteAndRequired$(trancheId: string) {
    this.getGesuchDokumente$(trancheId);
    this.getRequiredDocumentTypes$(trancheId);
  }

  private getGesuchDokumente$ = rxMethod<string>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          dokuments: cachedPending(state.dokuments),
        }));
      }),
      switchMap((gesuchTrancheId) =>
        this.trancheService.getGesuchDokumente$({ gesuchTrancheId }),
      ),
      handleApiResponse((dokuments) => patchState(this, { dokuments })),
    ),
  );

  private getRequiredDocumentTypes$ = rxMethod<string>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          requiredDocumentTypes: cachedPending(state.requiredDocumentTypes),
        }));
      }),
      switchMap((gesuchTrancheId) =>
        this.trancheService
          .getRequiredGesuchDokumentTyp$({ gesuchTrancheId })
          .pipe(
            handleApiResponse((requiredDocumentTypes) =>
              patchState(this, { requiredDocumentTypes }),
            ),
          ),
      ),
    ),
  );
}
