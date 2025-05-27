import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/global/notification';
import {
  BeschwerdeVerlaufEntry,
  BeschwerdeVerlaufEntryCreate,
  GesuchService,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';

type BeschwerdeState = {
  beschwerden: CachedRemoteData<BeschwerdeVerlaufEntry[]>;
  beschwerde: CachedRemoteData<BeschwerdeVerlaufEntry>;
};

const initialState: BeschwerdeState = {
  beschwerden: initial(),
  beschwerde: initial(),
};

@Injectable()
export class BeschwerdeStore extends signalStore(
  { protectedState: false },
  withState(initialState),
  withDevtools('BeschwerdeStore'),
) {
  private gesuchService = inject(GesuchService);
  private globalNotificationStore = inject(GlobalNotificationStore);

  beschwerdeVelaufSig = computed(() => {
    const beschwerde = this.beschwerden.data();

    if (!beschwerde) {
      return [];
    }

    return beschwerde.map((entry) => ({
      ...entry,
      document: entry.beschwerdeEntscheid?.dokumente[0],
    }));
  });

  loadBeschwerden$ = rxMethod<{ gesuchId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          beschwerden: cachedPending(state.beschwerden),
        }));
      }),
      switchMap(({ gesuchId }) =>
        this.gesuchService
          .getAllBeschwerdeVerlaufEntrys$({
            gesuchId,
          })
          .pipe(
            handleApiResponse((beschwerde) =>
              patchState(this, { beschwerden: beschwerde }),
            ),
          ),
      ),
    ),
  );

  createBeschwerdeEntry$ = rxMethod<{
    gesuchId: string;
    values: BeschwerdeVerlaufEntryCreate;
    onSucces?: () => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          beschwerde: cachedPending(state.beschwerde),
        }));
      }),
      switchMap(({ gesuchId, values, onSucces }) =>
        this.gesuchService
          .createBeschwerdeVerlaufEntry$({
            gesuchId,
            beschwerdeVerlaufEntryCreate: values,
          })
          .pipe(
            handleApiResponse(
              (beschwerde) => {
                patchState(this, { beschwerde });
              },
              {
                onSuccess: () => {
                  this.globalNotificationStore.createSuccessNotification({
                    messageKey:
                      'sachbearbeitung-app.infos.beschwerde.create.success.' +
                      values.beschwerdeSetTo,
                  });
                  onSucces?.();
                },
              },
            ),
          ),
      ),
    ),
  );

  createBeschwerdeEntscheid$ = rxMethod<{
    gesuchId: string;
    fileUpload: File;
    kommentar: string;
    beschwerdeErfolgreich: boolean;
    onSucces?: () => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          beschwerde: cachedPending(state.beschwerde),
        }));
      }),
      switchMap(
        ({
          gesuchId,
          fileUpload,
          kommentar,
          beschwerdeErfolgreich,
          onSucces,
        }) =>
          this.gesuchService
            .createBeschwerdeEntscheid$({
              gesuchId,
              fileUpload,
              kommentar,
              beschwerdeErfolgreich,
            })
            .pipe(
              handleApiResponse(
                (beschwerde) => {
                  patchState(this, { beschwerde });
                },
                {
                  onSuccess: () => {
                    this.globalNotificationStore.createSuccessNotification({
                      messageKey:
                        'sachbearbeitung-app.infos.beschwerde-entscheid.create.success',
                    });
                    onSucces?.();
                  },
                },
              ),
            ),
      ),
    ),
  );
}
