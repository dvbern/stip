import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { tapResponse } from '@ngrx/operators';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { EMPTY, catchError, pipe, switchMap, tap } from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/global/notification';
import {
  DokumentService,
  DokumentTyp,
  Dokumentstatus,
  GesuchDokument,
  GesuchDokumentKommentar,
  GesuchService,
  GesuchTrancheService,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  RemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  isSuccess,
  pending,
  success,
} from '@dv/shared/util/remote-data';

type DokumentsState = {
  dokuments: CachedRemoteData<GesuchDokument[]>;
  requiredDocumentTypes: CachedRemoteData<DokumentTyp[]>;
  gesuchDokumentKommentare: RemoteData<GesuchDokumentKommentar[]>;
  dokument: CachedRemoteData<GesuchDokument>;
};

const initialState: DokumentsState = {
  dokuments: initial(),
  requiredDocumentTypes: initial(),
  gesuchDokumentKommentare: initial(),
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

  dokumenteViewSig = computed(() => {
    const dokuments = fromCachedDataSig(this.dokuments) ?? [];
    return {
      dokuments,
      requiredDocumentTypes:
        fromCachedDataSig(this.requiredDocumentTypes)?.filter(
          // A document can already be uploaded but later on get rejected. In this case the document list would contain
          // both the empty gesuch dokument and a gesuch dokument typ of the rejected document. So we need to filter
          // them out
          (required) => !dokuments.map((d) => d.dokumentTyp).includes(required),
        ) ?? [],
    };
  });

  kommentareViewSig = computed(() => {
    return (
      this.gesuchDokumentKommentare.data()?.filter((k) => k.kommentar) ?? []
    );
  });

  dokumentViewSig = computed(() =>
    isSuccess(this.dokument()) ? this.dokument().data : undefined,
  );

  hasAcceptedAllDokumentsSig = computed(() => {
    return (
      this.dokuments
        .data()
        ?.every((dokument) => dokument.status === Dokumentstatus.AKZEPTIERT) ??
      false
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

  getGesuchDokumentKommentare$ = rxMethod<{
    dokumentTyp: DokumentTyp;
    gesuchTrancheId: string;
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
    gesuchTrancheId: string;
    gesuchDokumentId: string;
    dokumentTyp: DokumentTyp;
    kommentar: string;
    afterSuccess?: () => void;
  }>(
    pipe(
      switchMap(
        ({
          gesuchTrancheId,
          gesuchDokumentId,
          dokumentTyp,
          kommentar,
          afterSuccess,
        }) =>
          this.dokumentService
            .gesuchDokumentAblehnen$({
              gesuchDokumentId,
              gesuchDokumentAblehnenRequest: {
                kommentar: {
                  kommentar,
                  dokumentTyp,
                  gesuchTrancheId,
                },
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

  getDokumenteAndRequired$(gesuchTrancheId: string, ignoreCache?: boolean) {
    this.getGesuchDokumente$({ gesuchTrancheId, ignoreCache });
    this.getRequiredDocumentTypes$(gesuchTrancheId);
  }

  getGesuchDokumente$ = rxMethod<{
    gesuchTrancheId: string;
    ignoreCache?: boolean;
  }>(
    pipe(
      tap(({ ignoreCache }) => {
        patchState(this, (state) => ({
          dokuments: ignoreCache ? pending() : cachedPending(state.dokuments),
        }));
      }),
      switchMap(({ gesuchTrancheId }) =>
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
