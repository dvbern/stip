import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { Store } from '@ngrx/store';
import { Observable, pipe, switchMap, tap } from 'rxjs';

import { SharedDataAccessGesuchEvents } from '@dv/shared/data-access/gesuch';
import {
  GesuchInfo,
  GesuchService,
  GesuchServiceGetGesucheSbRequestParams,
  PaginatedSbDashboard,
  SharedModelGesuch,
} from '@dv/shared/model/gesuch';
import { StatusUebergang } from '@dv/shared/util/gesuch';
import {
  CachedRemoteData,
  RemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  isPending,
  success,
} from '@dv/shared/util/remote-data';

type GesuchState = {
  gesuchInfo: CachedRemoteData<GesuchInfo>;
  gesuche: CachedRemoteData<PaginatedSbDashboard>;
  lastStatusChange: RemoteData<null>;
};

const initialState: GesuchState = {
  gesuchInfo: initial(),
  gesuche: initial(),
  lastStatusChange: initial(),
};

@Injectable()
export class GesuchStore extends signalStore(
  { protectedState: false },
  withState(initialState),
  withDevtools('GesuchStore'),
) {
  private store = inject(Store);
  private gesuchService = inject(GesuchService);
  private handleStatusChange =
    <T, R extends SharedModelGesuch>(handler$: (params: T) => Observable<R>) =>
    (source$: Observable<T & { onSuccess?: (data?: R) => void }>) => {
      return source$.pipe(
        tap(() => patchState(this, { lastStatusChange: initial() })),
        switchMap(({ onSuccess, ...params }) =>
          handler$(params as T).pipe(
            handleApiResponse(
              () => {
                patchState(this, { lastStatusChange: success(null) });
              },
              {
                onSuccess: (data) => {
                  this.store.dispatch(
                    SharedDataAccessGesuchEvents.gesuchSetReturned({
                      gesuch: data,
                    }),
                  );
                  this.loadGesuchInfo$({ gesuchId: data.id });
                  onSuccess?.(data);
                },
              },
            ),
          ),
        ),
      );
    };

  cockpitViewSig = computed(() => {
    return {
      gesuche: fromCachedDataSig(this.gesuche),
      loading: isPending(this.gesuche()),
    };
  });

  loadGesuchInfo$ = rxMethod<{ gesuchId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          gesuchInfo: cachedPending(state.gesuchInfo),
        }));
      }),
      switchMap(({ gesuchId }) =>
        this.gesuchService
          .getGesuchInfo$({ gesuchId })
          .pipe(
            handleApiResponse((gesuchInfo) => patchState(this, { gesuchInfo })),
          ),
      ),
    ),
  );

  loadGesuche$ = rxMethod<GesuchServiceGetGesucheSbRequestParams>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          gesuche: cachedPending(state.gesuche),
        }));
      }),
      switchMap((params) =>
        this.gesuchService
          .getGesucheSb$(params)
          .pipe(handleApiResponse((gesuche) => patchState(this, { gesuche }))),
      ),
    ),
  );

  setStatus$ = {
    SET_TO_BEARBEITUNG: rxMethod<{ gesuchTrancheId: string }>(
      pipe(
        this.handleStatusChange(({ gesuchTrancheId }) =>
          this.gesuchService.changeGesuchStatusToInBearbeitung$({
            gesuchTrancheId,
          }),
        ),
      ),
    ),

    EINGEREICHT: rxMethod<{ gesuchTrancheId: string }>(
      pipe(
        this.handleStatusChange(({ gesuchTrancheId }) =>
          this.gesuchService.gesuchEinreichen$({
            gesuchTrancheId,
          }),
        ),
      ),
    ),

    VERFUEGT: rxMethod<{ gesuchTrancheId: string }>(
      pipe(
        this.handleStatusChange(({ gesuchTrancheId }) =>
          this.gesuchService.changeGesuchStatusToVerfuegt$({
            gesuchTrancheId,
          }),
        ),
      ),
    ),

    VERSENDET: rxMethod<{ gesuchTrancheId: string }>(
      pipe(
        this.handleStatusChange(({ gesuchTrancheId }) =>
          this.gesuchService.changeGesuchStatusToVersendet$({
            gesuchTrancheId,
          }),
        ),
      ),
    ),

    BEREIT_FUER_BEARBEITUNG: rxMethod<{
      gesuchTrancheId: string;
      text?: string;
    }>(
      pipe(
        this.handleStatusChange(({ gesuchTrancheId, text }) =>
          this.gesuchService.changeGesuchStatusToBereitFuerBearbeitung$({
            gesuchTrancheId,
            ...(text ? { kommentar: { text } } : {}),
          }),
        ),
      ),
    ),

    BEARBEITUNG_ABSCHLIESSEN: rxMethod<{ gesuchTrancheId: string }>(
      pipe(
        this.handleStatusChange(({ gesuchTrancheId }) =>
          this.gesuchService.bearbeitungAbschliessen$({
            gesuchTrancheId,
          }),
        ),
      ),
    ),

    ZURUECKWEISEN: rxMethod<{
      gesuchTrancheId: string;
      text: string;
      onSuccess?: () => void;
    }>(
      pipe(
        this.handleStatusChange(({ gesuchTrancheId, text }) =>
          this.gesuchService.gesuchZurueckweisen$({
            gesuchTrancheId,
            kommentar: { text },
          }),
        ),
      ),
    ),

    NEGATIVE_VERFUEGUNG_ERSTELLEN: rxMethod<{
      gesuchTrancheId: string;
      grundId: string;
    }>(
      pipe(
        this.handleStatusChange(({ gesuchTrancheId, grundId }) =>
          this.gesuchService.changeGesuchStatusToNegativeVerfuegung$({
            gesuchTrancheId,
            ausgewaehlterGrund: { decisionId: grundId },
          }),
        ),
      ),
    ),
  } satisfies Record<StatusUebergang, unknown>;

  /**
   * Set the gesuchInfo in the store, for example when the gesuch was updated outside of this store.
   */
  setGesuchInfo(gesuchInfo: GesuchInfo) {
    patchState(this, () => ({ gesuchInfo: success(gesuchInfo) }));
  }
}
