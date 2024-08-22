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
  GesuchDokumentKommentar,
  GesuchService,
} from '@dv/shared/model/gesuch';
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

type DokumentsState = {
  dokuments: CachedRemoteData<GesuchDokument[]>;
  requiredDocumentTypes: CachedRemoteData<DokumentTyp[]>;
  gesuchDokumentKommentare: RemoteData<GesuchDokumentKommentar[]>;
};

const initialState: DokumentsState = {
  dokuments: initial(),
  requiredDocumentTypes: initial(),
  gesuchDokumentKommentare: initial(),
};

@Injectable({ providedIn: 'root' })
export class DokumentsStore extends signalStore(
  { protectedState: false },
  withState(initialState),
  withDevtools('DokumentsStore'),
) {
  private dokumentService = inject(DokumentService);
  private gesuchService = inject(GesuchService);
  private globalNotificationStore = inject(GlobalNotificationStore);

  dokumenteViewSig = computed(() => ({
    dokuments: fromCachedDataSig(this.dokuments) ?? [],
    requiredDocumentTypes: fromCachedDataSig(this.requiredDocumentTypes) ?? [],
  }));

  kommentareViewSig = computed(() => {
    return (
      this.gesuchDokumentKommentare.data()?.filter((k) => k.kommentar) ?? []
    );
  });

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

  getGesuchDokumentKommentare$ = rxMethod<{
    dokumentTyp: DokumentTyp;
    gesuchId: string;
  }>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          gesuchDokumentKommentare: pending(),
        }));
      }),
      switchMap((req) =>
        this.dokumentService.getGesuchDokumentKommentare$(req),
      ),
      handleApiResponse((gesuchDokumentKommentare) =>
        patchState(this, { gesuchDokumentKommentare }),
      ),
    ),
  );

  gesuchDokumentAblehnen$ = rxMethod<{
    gesuchId: string;
    dokumentTyp: DokumentTyp;
    gesuchDokumentId: string;
    kommentar: string;
  }>(
    pipe(
      switchMap(({ gesuchId, gesuchDokumentId, dokumentTyp, kommentar }) =>
        this.dokumentService
          .gesuchDokumentAblehnen$({
            gesuchDokumentId,
            gesuchDokumentAblehnenRequest: {
              kommentar: {
                kommentar,
                dokumentTyp,
                gesuchId,
              },
            },
          })
          .pipe(
            this.reloadGesuchDokumente(
              gesuchId,
              'shared.dokumente.reject.success',
            ),
          ),
      ),
    ),
  );

  gesuchDokumentAkzeptieren$ = rxMethod<{
    gesuchId: string;
    gesuchDokumentId: string;
  }>(
    pipe(
      switchMap(({ gesuchDokumentId, gesuchId }) =>
        this.dokumentService
          .gesuchDokumentAkzeptieren$({
            gesuchDokumentId,
          })
          .pipe(
            this.reloadGesuchDokumente(
              gesuchId,
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
    onSuccess: () => void;
  }>(
    pipe(
      switchMap(({ gesuchId, onSuccess }) => {
        return this.gesuchService
          .gesuchFehlendeDokumenteUebermitteln$({ gesuchId })
          .pipe(
            tap(() => {
              patchState(this, (state) => ({
                dokuments: cachedPending(state.dokuments),
              }));
            }),
            switchMap(() =>
              this.gesuchService.getGesuchDokumente$({ gesuchId }),
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

  getDokumenteAndRequired$(gesuchId: string) {
    this.getGesuchDokumente$(gesuchId);
    this.getRequiredDocumentTypes$(gesuchId);
  }

  private getGesuchDokumente$ = rxMethod<string>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          dokuments: cachedPending(state.dokuments),
        }));
      }),
      switchMap((gesuchId) =>
        this.gesuchService.getGesuchDokumente$({ gesuchId }),
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
      switchMap((gesuchId) =>
        this.gesuchService
          .getRequiredGesuchDokumentTyp$({ gesuchId })
          .pipe(
            handleApiResponse((requiredDocumentTypes) =>
              patchState(this, { requiredDocumentTypes }),
            ),
          ),
      ),
    ),
  );

  private reloadGesuchDokumente = (gesuchId: string, messageKey: string) =>
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          dokuments: cachedPending(state.dokuments),
        }));
      }),
      switchMap(() =>
        this.gesuchService.getGesuchDokumente$({
          gesuchId,
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
