import { Injectable, computed, inject } from '@angular/core';
import { tapResponse } from '@ngrx/operators';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import {
  EMPTY,
  catchError,
  combineLatest,
  pipe,
  switchMap,
  tap,
  throwError,
} from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/global/notification';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import {
  DokumentService,
  DokumentTyp,
  DokumenteToUpload,
  Dokumentstatus,
  GesuchDokument,
  GesuchDokumentKommentar,
  GesuchService,
  GesuchTrancheService,
  GesuchTrancheTyp,
  UnterschriftenblattDokument,
} from '@dv/shared/model/gesuch';
import { byAppType } from '@dv/shared/model/permission-state';
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
  isPending,
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
  dokument: CachedRemoteData<GesuchDokument | undefined>;
  expandedComponentList: 'custom' | 'required' | undefined;
  nachfrist: RemoteData<string>;
};

const initialState: DokumentsState = {
  additionalDokumente: initial(),
  dokuments: initial(),
  documentsToUpload: initial(),
  gesuchDokumentKommentare: initial(),
  dokument: initial(),
  expandedComponentList: undefined,
  nachfrist: initial(),
};

@Injectable({ providedIn: 'root' })
export class DokumentsStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private dokumentService = inject(DokumentService);
  private gesuchService = inject(GesuchService);
  private trancheService = inject(GesuchTrancheService);
  private globalNotificationStore = inject(GlobalNotificationStore);
  private config = inject(SharedModelCompileTimeConfig);

  private getGesuchDokumenteByAppType$(gesuchTrancheId: string) {
    return byAppType(this.config.appType, {
      'gesuch-app': () =>
        this.trancheService.getGesuchDokumenteGS$({ gesuchTrancheId }),
      'sachbearbeitung-app': () =>
        this.trancheService.getGesuchDokumenteSB$({ gesuchTrancheId }),
      'demo-data-app': () =>
        throwError(() => new Error('Not implemented for this AppType')),
    });
  }

  private getGesuchDokumentByAppType$({
    trancheId,
    dokumentTyp,
  }: {
    trancheId: string;
    dokumentTyp: DokumentTyp;
  }) {
    return byAppType(this.config.appType, {
      'gesuch-app': () =>
        this.dokumentService.getGesuchDokumentForTypGS$({
          gesuchTrancheId: trancheId,
          dokumentTyp,
        }),
      'sachbearbeitung-app': () =>
        this.dokumentService.getGesuchDokumentForTypSB$({
          gesuchTrancheId: trancheId,
          dokumentTyp,
        }),
      'demo-data-app': () =>
        throwError(() => new Error('Not implemented for this AppType')),
    });
  }

  private getDocumentsToUploadByAppType$(gesuchTrancheId: string) {
    return byAppType(this.config.appType, {
      'gesuch-app': () =>
        this.trancheService.getDocumentsToUploadGS$({ gesuchTrancheId }),
      'sachbearbeitung-app': () =>
        this.trancheService.getDocumentsToUploadSB$({ gesuchTrancheId }),
      'demo-data-app': () =>
        throwError(() => new Error('Not implemented for this AppType')),
    });
  }

  setExpandedList(list: 'custom' | 'required' | undefined) {
    patchState(this, { expandedComponentList: list });
  }

  resetGesuchDokumentStateToInitial = rxMethod(
    tap(() => {
      patchState(this, {
        dokument: initial(),
      });
    }),
  );

  dokumenteCanFlagsSig = computed(() => {
    const {
      gsCanDokumenteUebermitteln,
      sbCanBearbeitungAbschliessen,
      sbCanFehlendeDokumenteUebermitteln,
    } = this.documentsToUpload.data() ?? {};
    return {
      gsCanDokumenteUebermitteln,
      sbCanBearbeitungAbschliessen,
      sbCanFehlendeDokumenteUebermitteln,
    };
  });

  dokumenteViewSig = computed(() => {
    // only show standard documents
    const dokuments = (this.dokuments().data ?? []).filter(
      (d) => d.dokumentTyp,
    );

    return {
      dokuments,
      loading: isPending(this.dokuments()),
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
    const dokuments = (this.dokuments().data ?? []).filter(
      (d) => d.customDokumentTyp,
    );

    return {
      dokuments,
      loading: isPending(this.dokuments()),
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
    const dokuments = this.additionalDokumente().data ?? [];
    return {
      dokuments,
      loading: isPending(this.additionalDokumente()),

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

  /**
   * check if there are any abgelehnte dokumente or new custom dokument types
   * that the GS needs to upload. Also include documents, that become required through
   * changes by the SB
   */
  hasDokumenteToUebermittelnSig = computed(() => {
    const hasAbgelehnteDokumente =
      this.dokuments
        .data()
        ?.some((dokument) => dokument.status === Dokumentstatus.ABGELEHNT) ??
      false;

    const hasDokumenteToUpload =
      !!this.documentsToUpload().data?.required?.length;

    const newCustomDokumentTypes =
      this.dokuments.data()?.filter((dokument) => {
        const hasFiles = dokument.dokumente.length > 0;
        return (
          dokument.customDokumentTyp &&
          ((hasFiles && dokument.status === 'ABGELEHNT') ||
            (!hasFiles && dokument.status === 'AUSSTEHEND'))
        );
      }) ?? [];

    return (
      hasAbgelehnteDokumente ||
      newCustomDokumentTypes?.length > 0 ||
      hasDokumenteToUpload
    );
  });

  // Gesuch has dokuments that have not been rejected or accepted by SB
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
      switchMap(({ trancheId, dokumentTyp }) => {
        return this.getGesuchDokumentByAppType$({
          trancheId,
          dokumentTyp,
        });
      }),
      handleApiResponse((res) => {
        patchState(this, () => ({
          // Response is NullableGesuchDokument, so we extract the value
          dokument: mapData(res, (data) => data.value),
        }));
      }),
    ),
  );

  /**
   * Get all gesuch dokumente and documents to be uploaded
   * @param gesuchTrancheId
   * @param ignoreCache If true, the dokuments will ignore the cache when setting pending state
   */
  getGesuchDokumenteAndDocumentsToUpload$ = rxMethod<{
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
          this.getGesuchDokumenteByAppType$(gesuchTrancheId),
          this.getDocumentsToUploadByAppType$(gesuchTrancheId),
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
        // Achtung! Dieses errorhandling verhindert nicht ein canceling der subscription wenn ein error innerhalb
        // vom combineLatest passiert!
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

  // get Unterschriftenblaetter
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

  // get Unterschriftenblaetter
  getAdditionalDokumenteAndDocumentsToUpload$ = rxMethod<{
    gesuchId: string;
    gesuchTrancheId: string;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          additionalDokumente: cachedPending(state.additionalDokumente),
        }));
      }),
      switchMap(({ gesuchId, gesuchTrancheId }) =>
        combineLatest([
          this.dokumentService.getUnterschriftenblaetterForGesuch$({
            gesuchId,
          }),
          this.getDocumentsToUploadByAppType$(gesuchTrancheId),
        ]).pipe(
          tapResponse({
            next: ([additionalDokumente, documentsToUpload]) => {
              patchState(this, () => ({
                // Patch both lists at the same time to avoid unecessary rerenders
                additionalDokumente: success(additionalDokumente),
                documentsToUpload: success(documentsToUpload),
              }));
            },
            // Achtung! Dieses errorhandling verhindert nicht ein canceling der subscription wenn ein error innerhalb
            // vom combineLatest passiert!
            error: (error) => {
              patchState(this, () => ({
                additionalDokumente: failure(error),
                documentsToUpload: failure(error),
              }));
            },
          }),
          catchRemoteDataError((error) => {
            patchState(this, () => ({
              additionalDokumente: failure(error),
              documentsToUpload: failure(error),
            }));
          }),
        ),
      ),
    ),
  );

  // get all missing documents that need to be uploaded
  getDocumentsToUpload$ = rxMethod<{
    gesuchTrancheId: string;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          documentsToUpload: cachedPending(state.documentsToUpload),
        }));
      }),
      switchMap(({ gesuchTrancheId }) =>
        this.getDocumentsToUploadByAppType$(gesuchTrancheId).pipe(
          handleApiResponse((documentsToUpload) =>
            patchState(this, { documentsToUpload }),
          ),
        ),
      ),
    ),
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
      switchMap((req) => {
        const service$ = byAppType(this.config.appType, {
          'gesuch-app': () =>
            this.dokumentService.getGesuchDokumentKommentareGS$(req),
          'sachbearbeitung-app': () =>
            this.dokumentService.getGesuchDokumentKommentareSB$(req),
          'demo-data-app': () =>
            throwError(() => new Error('Not implemented for this AppType')),
        });
        return service$.pipe(
          handleApiResponse((gesuchDokumentKommentare) =>
            patchState(this, {
              gesuchDokumentKommentare: mapData(
                gesuchDokumentKommentare,
                (data) =>
                  data.map((d) => ({
                    ...d,
                    gesuchDokumentId: req.gesuchDokumentId,
                  })),
              ),
            }),
          ),
        );
      }),
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
    customDokumentTypId: string;
    onSuccess: () => void;
  }>(
    pipe(
      switchMap(({ customDokumentTypId, onSuccess }) =>
        this.dokumentService
          .deleteCustomDokumentTyp$({
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
    trancheTyp: GesuchTrancheTyp;
    onSuccess: () => void;
  }>(
    pipe(
      switchMap(({ trancheId, trancheTyp, onSuccess }) => {
        const serviceMap$ = {
          TRANCHE: () =>
            this.gesuchService.gesuchFehlendeDokumenteUebermitteln$({
              gesuchTrancheId: trancheId,
            }),
          AENDERUNG: () =>
            this.trancheService.aenderungFehlendeDokumenteUebermitteln$({
              gesuchTrancheId: trancheId,
            }),
        } satisfies Record<GesuchTrancheTyp, unknown>;
        return serviceMap$[trancheTyp]().pipe(
          tap(() => {
            patchState(this, (state) => ({
              dokuments: cachedPending(state.dokuments),
            }));
          }),
          switchMap(() => this.getGesuchDokumenteByAppType$(trancheId)),
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
          .changeGesuchStatusToVerfuegungDruckbereit$({
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
    tranchenTyp: GesuchTrancheTyp;
    onSuccess: () => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          dokuments: cachedPending(state.dokuments),
          documentsToUpload: cachedPending(state.documentsToUpload),
        }));
      }),
      switchMap(({ trancheId, tranchenTyp, onSuccess }) => {
        const serviceMap$ = {
          TRANCHE: () =>
            this.gesuchService.gesuchTrancheFehlendeDokumenteEinreichen$({
              gesuchTrancheId: trancheId,
            }),
          AENDERUNG: () =>
            this.trancheService.aenderungFehlendeDokumenteEinreichen$({
              gesuchTrancheId: trancheId,
            }),
        } satisfies Record<GesuchTrancheTyp, unknown>;
        return serviceMap$[tranchenTyp]().pipe(
          tapResponse({
            next: () => {
              this.getDocumentsToUploadByAppType$(trancheId);
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
        );
      }),
    ),
  );

  editNachfrist$ = rxMethod<{
    gesuchId: string;
    newNachfrist: string;
    onSuccess: () => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, {
          nachfrist: pending(),
        });
      }),
      switchMap(({ gesuchId, newNachfrist, onSuccess }) =>
        this.gesuchService
          .updateNachfristDokumente$({
            gesuchId,
            nachfristAendernRequest: {
              newNachfrist,
            },
          })
          .pipe(
            tapResponse({
              next: () => {
                this.globalNotificationStore.createSuccessNotification({
                  messageKey: 'shared.dokumente.nachfrist.editSuccess',
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
