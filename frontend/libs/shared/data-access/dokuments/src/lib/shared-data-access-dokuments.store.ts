import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

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
  handleApiResponse,
  initial,
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
  withState(initialState),
  withDevtools('DokumentsStore'),
) {
  private dokumentService = inject(DokumentService);
  private gesuchService = inject(GesuchService);
  private globalNotificationStore = inject(GlobalNotificationStore);

  dokumenteViewSig = computed(() => ({
    dokuments: this.dokuments().data ?? [],
    requiredDocumentTypes: this.requiredDocumentTypes.data() ?? [],
  }));

  hasAusstehendeDokumentsSig = computed(() => {
    return (
      this.dokuments
        .data()
        ?.some((dokument) =>
          dokument.status
            ? dokument.status === Dokumentstatus.AUSSTEHEND
            : false,
        ) ?? false
    );
  });

  // error handling
  gesuchDokumentAblehnen$ = rxMethod<{
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
            switchMap(() =>
              this.gesuchService.getGesuchDokumente$({
                gesuchId,
              }),
            ),
            handleApiResponse((dokuments) => patchState(this, { dokuments }), {
              onSuccess: () => {
                this.globalNotificationStore.createSuccessNotification({
                  messageKey: 'shared.dokumente.reject.success',
                });
              },
            }),
          ),
      ),
    ),
  );

  gesuchDokumentAkzeptieren$ = rxMethod<{
    gesuchId: string;
    gesuchDokumentId: string;
  }>(
    pipe(
      switchMap(({ gesuchDokumentId, gesuchId }) => {
        return this.dokumentService
          .gesuchDokumentAkzeptieren$({
            gesuchDokumentId,
          })
          .pipe(
            switchMap(() =>
              this.gesuchService.getGesuchDokumente$({ gesuchId }),
            ),
            handleApiResponse((dokuments) => patchState(this, { dokuments }), {
              onSuccess: () => {
                this.globalNotificationStore.createSuccessNotification({
                  messageKey: 'shared.dokumente.accept.success',
                });
              },
            }),
          );
      }),
    ),
  );

  // error handling....
  /**
   * Send missing documents to the backend
   * only possible if there are documents in status "AGBELEHNT"
   * will trigger an email to the gesuchsteller
   */
  sendMissingDocuments$ = rxMethod<string>(
    pipe(
      switchMap((gesuchId) => {
        return this.gesuchService
          .gesuchFehlendeDokumenteUebermitteln$({ gesuchId })
          .pipe(
            switchMap(() =>
              this.gesuchService.getGesuchDokumente$({ gesuchId }),
            ),
            handleApiResponse((dokuments) => patchState(this, { dokuments }), {
              onSuccess: () => {
                this.globalNotificationStore.createSuccessNotification({
                  messageKey: 'shared.dokumente.accept.success',
                });
              },
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
}
