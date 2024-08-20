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
    gesuchsId: string;
    dokumentTyp: DokumentTyp;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          dokument: cachedPending(state.dokument),
        }));
      }),
      switchMap(({ gesuchsId, dokumentTyp }) =>
        this.gesuchService.getGesuchDokument$({ gesuchsId, dokumentTyp }),
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

  gesuchDokumentAblehnenAndReloadList$ = rxMethod<{
    gesuchId: string;
    gesuchDokumentId: string;
    kommentar: string;
  }>(
    pipe(
      switchMap(({ gesuchId, gesuchDokumentId, kommentar }) =>
        this.dokumentService
          .gesuchDokumentAblehnen$({
            gesuchDokumentId,
            gesuchDokumentAblehnenRequest: {
              kommentar,
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

  gesuchDokumentAkzeptierenAndReloadList$ = rxMethod<{
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

  gesuchDokumentAblehnenAndReloadCurrent$ = rxMethod<{
    gesuchId: string;
    gesuchDokumentId: string;
    kommentar: string;
    gesuchDokumentTyp: DokumentTyp;
  }>(
    pipe(
      switchMap(
        ({ gesuchId, gesuchDokumentId, kommentar, gesuchDokumentTyp }) =>
          this.dokumentService
            .gesuchDokumentAblehnen$({
              gesuchDokumentId,
              gesuchDokumentAblehnenRequest: {
                kommentar,
              },
            })
            .pipe(this.loadGesuchDokument$(gesuchId, gesuchDokumentTyp)),
      ),
    ),
  );

  gesuchDokumentAkzeptierenAndReloadCurrent$ = rxMethod<{
    gesuchId: string;
    gesuchDokumentId: string;
    gesuchDokumentTyp: DokumentTyp;
  }>(
    pipe(
      switchMap(({ gesuchDokumentId, gesuchId, gesuchDokumentTyp }) =>
        this.dokumentService
          .gesuchDokumentAkzeptieren$({
            gesuchDokumentId,
          })
          .pipe(this.loadGesuchDokument$(gesuchId, gesuchDokumentTyp)),
      ),
    ),
  );

  private loadGesuchDokument$ = (
    gesuchsId: string,
    dokumentTyp: DokumentTyp,
  ) => {
    return pipe(
      tap(() => {
        patchState(this, (state) => ({
          dokument: cachedPending(state.dokument),
        }));
      }),
      switchMap(() =>
        this.gesuchService.getGesuchDokument$({ gesuchsId, dokumentTyp }),
      ),
      handleApiResponse((dokument) => patchState(this, { dokument })),
    );
  };

  //TODO: akzeptieren und ablehnen umbenennen fuer liste und duplizieren und umschreiben fuer einzeln

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
