import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/data-access/global-notification';
import {
  DokumentService,
  DokumentTyp,
  GesuchDokument,
  GesuchService,
  GesuchServiceGesuchFehlendeDokumenteUebermittelnRequestParams,
} from '@dv/shared/model/gesuch';
import {
  RemoteData,
  handleApiResponse,
  initial,
  pending,
} from '@dv/shared/util/remote-data';

type DokumentsState = {
  dokuments: RemoteData<GesuchDokument[]>;
  requiredDocumentTypes: RemoteData<DokumentTyp[]>;
};

const initialState: DokumentsState = {
  dokuments: initial(),
  requiredDocumentTypes: initial(),
};

@Injectable()
export class DokumentsStore extends signalStore(
  withState(initialState),
  withDevtools('DokumentsStore'),
) {
  private dokumentService = inject(DokumentService);
  private gesuchService = inject(GesuchService);
  private globalNotificationStore = inject(GlobalNotificationStore);

  dokumenteViewSig = computed(() => this.dokuments.data() ?? []);
  requiredDocumentTypesViewSig = computed(
    () => this.requiredDocumentTypes.data() ?? [],
  );

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

  /**
   * Send missing documents to the backend
   * only possible if there are documents in status "AGBELEHNT"
   * will trigger an email to the gesuchsteller
   */
  sendMissingDocuments$ =
    rxMethod<GesuchServiceGesuchFehlendeDokumenteUebermittelnRequestParams>(
      pipe(
        switchMap((req) => {
          return this.gesuchService.gesuchFehlendeDokumenteUebermitteln$(req);
        }),
      ),
    );

  dokumentsStateInit(gesuchId: string) {
    this.getGesuchDokumente$(gesuchId);
    this.getRequiredDocumentTypes$(gesuchId);
  }

  getGesuchDokumente$ = rxMethod<string>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          dokuments: pending(),
        }));
      }),
      switchMap((gesuchId) =>
        this.gesuchService.getGesuchDokumente$({ gesuchId }),
      ),
      handleApiResponse((dokuments) => patchState(this, { dokuments })),
    ),
  );

  getRequiredDocumentTypes$ = rxMethod<string>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          requiredDocumentTypes: pending(),
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
