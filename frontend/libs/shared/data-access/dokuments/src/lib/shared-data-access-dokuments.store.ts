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
  success,
} from '@dv/shared/util/remote-data';

type DokumentsState = {
  dokuments: CachedRemoteData<GesuchDokument[]>;
  requiredDocumentTypes: CachedRemoteData<DokumentTyp[]>;
};

const initialState: DokumentsState = {
  dokuments: initial(),
  requiredDocumentTypes: initial(),
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

  gesuchDokumentAblehnen$ = rxMethod<{
    trancheId: string;
    gesuchDokumentId: string;
    kommentar: string;
  }>(
    pipe(
      switchMap(({ trancheId, gesuchDokumentId, kommentar }) =>
        this.dokumentService
          .gesuchDokumentAblehnen$({
            gesuchDokumentId,
            gesuchDokumentAblehnenRequest: {
              kommentar,
            },
          })
          .pipe(
            this.reloadGesuchDokumente(
              trancheId,
              'shared.dokumente.reject.success',
            ),
          ),
      ),
    ),
  );

  gesuchDokumentAkzeptieren$ = rxMethod<{
    trancheId: string;
    gesuchDokumentId: string;
  }>(
    pipe(
      switchMap(({ gesuchDokumentId, trancheId }) =>
        this.dokumentService
          .gesuchDokumentAkzeptieren$({
            gesuchDokumentId,
          })
          .pipe(
            this.reloadGesuchDokumente(
              trancheId,
              'shared.dokumente.accept.success',
            ),
          ),
      ),
    ),
  );

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

  private reloadGesuchDokumente = (
    gesuchTrancheId: string,
    messageKey: string,
  ) =>
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          dokuments: cachedPending(state.dokuments),
        }));
      }),
      switchMap(() =>
        this.trancheService.getGesuchDokumente$({
          gesuchTrancheId,
        }),
      ),
      tapResponse({
        next: (dokuments) => {
          patchState(this, { dokuments: success(dokuments) });
          this.globalNotificationStore.createSuccessNotification({
            messageKey,
          });
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
}
