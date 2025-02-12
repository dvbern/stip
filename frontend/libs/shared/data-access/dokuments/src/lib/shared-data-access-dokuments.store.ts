import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { tapResponse } from '@ngrx/operators';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { EMPTY, catchError, combineLatest, pipe, switchMap, tap } from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/global/notification';
import {
  DokumentService,
  DokumentTyp,
  DokumenteToUpload,
  Dokumentstatus,
  GesuchDokument,
  GesuchDokumentKommentar,
  GesuchService,
  GesuchTrancheService,
  UnterschriftenblattDokument,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  RemoteData,
  cachedFailure,
  cachedPending,
  catchRemoteDataError,
  failure,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  isSuccess,
  mapData,
  pending,
  success,
} from '@dv/shared/util/remote-data';

type DokumentsState = {
  additionalDokumente: CachedRemoteData<UnterschriftenblattDokument[]>;
  /**
   * Contains all the uploaded required and custom documents
   */
  dokuments: CachedRemoteData<GesuchDokument[]>;
  documentsToUpload: CachedRemoteData<DokumenteToUpload>;
  gesuchDokumentKommentare: RemoteData<GesuchDokumentKommentar[]>;
  dokument: CachedRemoteData<GesuchDokument>;
};

const initialState: DokumentsState = {
  additionalDokumente: initial(),
  dokuments: initial(),
  documentsToUpload: initial(),
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
    // only show standard documents
    const dokuments = (fromCachedDataSig(this.dokuments) ?? []).filter(
      (d) => d.dokumentTyp,
    );

    return {
      dokuments,
      requiredDocumentTypes:
        fromCachedDataSig(this.documentsToUpload)?.required?.filter(
          // A document can already be uploaded but later on get rejected. In this case the document list would contain
          // both the empty gesuch dokument and a gesuch dokument typ of the rejected document. So we need to filter
          // them out
          (required) => !dokuments.map((d) => d.dokumentTyp).includes(required),
        ) ?? [],
    };
  });

  customDokumenteViewSig = computed(() => {
    // only show custom documents
    const dokuments = (fromCachedDataSig(this.dokuments) ?? []).filter(
      (d) => d.customDokumentTyp,
    );

    return {
      dokuments,
      requiredDocumentTypes:
        fromCachedDataSig(this.documentsToUpload)?.customDokumentTyps?.filter(
          // A document can already be uploaded but later on get rejected. In this case the document list would contain
          // both the empty gesuch dokument and a gesuch dokument typ of the rejected document. So we need to filter
          // them out
          (required) =>
            !dokuments.map((d) => d.customDokumentTyp?.id === required.id),
        ) ?? [],
    };
  });

  additionalDokumenteViewSig = computed(() => {
    const dokuments = fromCachedDataSig(this.additionalDokumente) ?? [];
    return {
      dokuments,
      requiredDocumentTypes:
        fromCachedDataSig(this.documentsToUpload)?.unterschriftenblaetter ?? [],
    };
  });

  kommentareViewSig = computed(() => {
    return mapData(
      this.gesuchDokumentKommentare(),
      (data) => data.filter((k) => k.kommentar) ?? [],
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

  // has abgelehnte dokumente or new custom dokument types for SB to send to GS
  hasDokumenteToUebermittelnSig = computed(() => {
    const hasAbgelehnteDokumente =
      this.dokuments
        .data()
        ?.some((dokument) => dokument.status === Dokumentstatus.ABGELEHNT) ??
      false;

    const newCustomDokumentTypes =
      this.dokuments.data()?.filter((dokument) => {
        const hasFiles = dokument.dokumente.length > 0;
        return (
          dokument.customDokumentTyp &&
          ((hasFiles && dokument.status === 'ABGELEHNT') ||
            (!hasFiles && dokument.status === 'AUSSTEHEND'))
        );
      }) ?? [];

    return hasAbgelehnteDokumente || newCustomDokumentTypes?.length > 0;
  });

  // SB has dokuments that have not been rejected or accepted by SB
  hasSBAusstehendeDokumentsSig = computed(() => {
    return (
      this.dokuments
        .data()
        // Filter out custom dokument types that have no documents
        ?.filter(
          (dokument) =>
            !(dokument.customDokumentTyp && dokument.dokumente.length === 0),
        )
        ?.some((dokument) => dokument.status === Dokumentstatus.AUSSTEHEND) ??
      false
    );
  });

  // GS has not reuploaded all rejected documents
  hasGSAussstehendeDokumentsSig = computed(() => {
    // covers all custom dokument types that have no documents
    const hasDokumenteWithoutDocuments = this.dokuments
      .data()
      ?.some((dokument) => dokument.dokumente.length === 0);

    // Check if there are required dokumente that have not been uploaded
    // necessary, since the required aussstehende dokumente will not be in the dokuments list, but show up in the documentsToUpload list
    const hasRequiredDokumenteWithoutDokument =
      this.documentsToUpload().data?.required?.some(
        (dokumentTyp) =>
          !this.dokuments
            .data()
            ?.some((dokument) => dokument.dokumentTyp === dokumentTyp),
      );

    return hasDokumenteWithoutDocuments || hasRequiredDokumenteWithoutDokument;
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
    gesuchDokumentId: string;
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
    kommentar: string;
    onSuccess?: () => void;
  }>(
    pipe(
      switchMap(({ gesuchTrancheId, gesuchDokumentId, kommentar, onSuccess }) =>
        this.dokumentService
          .gesuchDokumentAblehnen$({
            gesuchDokumentId,
            gesuchDokumentAblehnenRequest: {
              kommentar: {
                kommentar,
                gesuchDokumentId,
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
                onSuccess?.();
              },
              error: () => undefined,
            }),
          ),
      ),
    ),
  );

  gesuchDokumentAkzeptieren$ = rxMethod<{
    gesuchDokumentId: string;
    onSuccess?: () => void;
  }>(
    pipe(
      switchMap(({ gesuchDokumentId, onSuccess }) =>
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
                onSuccess?.();
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
    trancheId: string;
    onSuccess: () => void;
  }>(
    pipe(
      switchMap(({ trancheId, onSuccess }) => {
        return this.gesuchService
          .gesuchFehlendeDokumenteUebermitteln$({ gesuchTrancheId: trancheId })
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

  setToAdditionalDokumenteErhalten$ = rxMethod<{
    gesuchTrancheId: string;
    onSuccess: () => void;
  }>(
    pipe(
      switchMap(({ gesuchTrancheId, onSuccess }) => {
        return this.gesuchService
          .changeGesuchStatusToVersandbereit$({
            gesuchTrancheId,
          })
          .pipe(
            tapResponse({
              next: () => {
                onSuccess();
              },
              error: () => undefined,
            }),
            catchError(() => {
              return EMPTY;
            }),
          );
      }),
    ),
  );

  fehlendeDokumenteEinreichen$ = rxMethod<{
    trancheId: string;
    onSuccess: () => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          dokuments: cachedPending(state.dokuments),
          documentsToUpload: cachedPending(state.documentsToUpload),
        }));
      }),
      switchMap(({ trancheId, onSuccess }) =>
        this.gesuchService
          .gesuchTrancheFehlendeDokumenteEinreichen$({
            gesuchTrancheId: trancheId,
          })
          .pipe(
            tapResponse({
              next: () => {
                this.getRequiredDocumentTypes$(trancheId);
                onSuccess();
              },
              error: (error) => {
                patchState(this, (state) => ({
                  dokuments: cachedFailure(state.dokuments, error),
                  documentsToUpload: cachedFailure(
                    state.documentsToUpload,
                    error,
                  ),
                }));
              },
            }),
          ),
      ),
    ),
  );

  getDokumenteAndRequired$ = rxMethod<{
    gesuchTrancheId: string;
    ignoreCache?: boolean;
  }>(
    pipe(
      tap(({ ignoreCache }) => {
        patchState(this, (state) => ({
          dokuments: ignoreCache ? pending() : cachedPending(state.dokuments),
          documentsToUpload: cachedPending(state.documentsToUpload),
        }));
      }),
      switchMap(({ gesuchTrancheId }) =>
        combineLatest([
          this.trancheService.getGesuchDokumente$({ gesuchTrancheId }),
          this.trancheService.getDocumentsToUpload$({
            gesuchTrancheId,
          }),
        ]),
      ),
      tapResponse({
        next: ([dokuments, documentsToUpload]) => {
          patchState(this, () => ({
            // Patch both lists at the same time to avoid unecessary rerenders
            dokuments: success(dokuments),
            documentsToUpload: success(documentsToUpload),
          }));
        },
        // @scph dieses errorhandling verhindert nicht ein canceling der subscription wenn ein error innerhalb
        // vom combineLatest passiert. Wie koennen wir das beheben?
        error: (error) => {
          patchState(this, () => ({
            dokuments: failure(error),
            documentsToUpload: failure(error),
          }));
        },
      }),
      catchRemoteDataError((error) => {
        patchState(this, () => ({
          dokuments: failure(error),
          documentsToUpload: failure(error),
        }));
      }),
    ),
  );

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

  getAdditionalDokumente$ = rxMethod<{
    gesuchId: string;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          additionalDokumente: cachedPending(state.additionalDokumente),
        }));
      }),
      switchMap(({ gesuchId }) =>
        this.dokumentService
          .getUnterschriftenblaetterForGesuch$({ gesuchId })
          .pipe(
            handleApiResponse((additionalDokumente) =>
              patchState(this, { additionalDokumente }),
            ),
          ),
      ),
    ),
  );

  getRequiredDocumentTypes$ = rxMethod<string>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          documentsToUpload: cachedPending(state.documentsToUpload),
        }));
      }),
      switchMap((gesuchTrancheId) =>
        this.trancheService
          .getDocumentsToUpload$({ gesuchTrancheId })
          .pipe(
            handleApiResponse((documentsToUpload) =>
              patchState(this, { documentsToUpload }),
            ),
          ),
      ),
    ),
  );

  createCustomDokumentTyp$ = rxMethod<{
    trancheId: string;
    type: string;
    description: string;
    onSuccess: () => void;
  }>(
    pipe(
      switchMap(({ trancheId, type, description, onSuccess }) =>
        this.dokumentService
          .createCustomDokumentTyp$({
            customDokumentTypCreate: {
              description,
              trancheId,
              type,
            },
          })
          .pipe(
            tapResponse({
              next: () => {
                this.globalNotificationStore.createSuccessNotification({
                  messageKey:
                    'shared.dokumente.createCustomDokumentTyp.success',
                });
                onSuccess();
              },
              error: () => undefined,
            }),
          ),
      ),
    ),
  );

  deleteCustomDokumentTyp$ = rxMethod<{
    gesuchTrancheId: string;
    customDokumentTypId: string;
    onSuccess: () => void;
  }>(
    pipe(
      switchMap(({ gesuchTrancheId, customDokumentTypId, onSuccess }) =>
        this.dokumentService
          .deleteCustomDokumentTyp$({
            gesuchTrancheId,
            customDokumentTypId,
          })
          .pipe(
            tapResponse({
              next: () => {
                this.globalNotificationStore.createSuccessNotification({
                  messageKey:
                    'shared.dokumente.deleteCustomDokumentTyp.success',
                });
                onSuccess();
              },
              error: () => undefined,
            }),
          ),
      ),
    ),
  );
}
