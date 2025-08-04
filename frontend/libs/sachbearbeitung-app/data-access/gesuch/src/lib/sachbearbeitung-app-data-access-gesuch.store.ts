import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { Store } from '@ngrx/store';
import { Observable, exhaustMap, map, pipe, switchMap, tap } from 'rxjs';

import { SharedDataAccessGesuchEvents } from '@dv/shared/data-access/gesuch';
import {
  GesuchInfo,
  GesuchService,
  GesuchServiceGetGesucheSbRequestParams,
  Kanton,
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
  pending,
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
) {
  private store = inject(Store);
  private gesuchService = inject(GesuchService);
  private handleStatusChange =
    <T, R extends SharedModelGesuch>(handler$: (params: T) => Observable<R>) =>
    (source$: Observable<T & { onSuccess?: (data?: R) => void }>) => {
      return source$.pipe(
        tap(() => patchState(this, { lastStatusChange: pending() })),
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
      exhaustMap(({ gesuchId }) =>
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

    ANSPRUCH_PRUEFEN: rxMethod<{ gesuchTrancheId: string }>(
      pipe(
        this.handleStatusChange(({ gesuchTrancheId }) =>
          this.gesuchService.gesuchManuellPruefenJur$({
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

    VERSENDET: rxMethod<{ gesuchTrancheId: string; onSuccess: () => void }>(
      pipe(
        this.handleStatusChange(({ gesuchTrancheId, onSuccess }) =>
          this.gesuchService
            .changeGesuchStatusToVersendet$({
              gesuchTrancheId,
            })
            .pipe(map((p) => ({ ...p, onSuccess }))),
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

    NEGATIVE_VERFUEGUNG_ERSTELLEN: rxMethod<{
      gesuchTrancheId: string;
      grundId: string;
      kanton: Kanton | undefined;
    }>(
      pipe(
        this.handleStatusChange(({ gesuchTrancheId, grundId, kanton }) =>
          this.gesuchService.changeGesuchStatusToNegativeVerfuegung$({
            gesuchTrancheId,
            ausgewaehlterGrund: {
              decisionId: grundId,
              kanton: kanton,
            },
          }),
        ),
      ),
    ),

    ZURUECKWEISEN: rxMethod<{
      gesuchTrancheId: string;
      text: string;
      onSuccess?: (newGesuchTrancheId: string) => void;
    }>(
      pipe(
        switchMap(({ gesuchTrancheId, text, onSuccess }) =>
          this.gesuchService
            .gesuchZurueckweisen$({
              gesuchTrancheId,
              kommentar: { text },
            })
            .pipe(
              handleApiResponse(
                () => {
                  patchState(this, { lastStatusChange: success(null) });
                },
                {
                  onSuccess: ({ gesuchTrancheId: newGesuchTrancheId }) => {
                    onSuccess?.(newGesuchTrancheId);
                  },
                },
              ),
            ),
        ),
      ),
    ),
  } satisfies Record<StatusUebergang, unknown>;

  createManuelleVerfuegung$ = rxMethod<{
    gesuchTrancheId: string;
    fileUpload: File;
    onSuccess?: () => void;
    kommentar?: string;
  }>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          lastStatusChange: pending(),
        }));
      }),
      switchMap(({ gesuchTrancheId, fileUpload, onSuccess, kommentar }) =>
        this.gesuchService
          .createManuelleVerfuegung$({
            gesuchTrancheId,
            fileUpload,
            kommentar,
          })
          .pipe(
            handleApiResponse(
              () => {
                patchState(this, { lastStatusChange: success(null) });
              },
              {
                onSuccess,
              },
            ),
          ),
      ),
    ),
  );
}
