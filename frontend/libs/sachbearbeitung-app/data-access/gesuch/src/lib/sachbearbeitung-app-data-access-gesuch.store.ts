import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { Store } from '@ngrx/store';
import { Observable, pipe, switchMap, tap } from 'rxjs';

import { SharedDataAccessGesuchEvents } from '@dv/shared/data-access/gesuch';
import {
  GesuchService,
  GesuchServiceGetGesucheSbRequestParams,
  PaginatedSbDashboard,
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
  gesuche: CachedRemoteData<PaginatedSbDashboard>;
  lastStatusChange: RemoteData<null>;
};

const initialState: GesuchState = {
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
    <T, R>(handler$: (params: T) => Observable<R>, onSuccess?: () => void) =>
    (source$: Observable<T>) => {
      return source$.pipe(
        tap(() => patchState(this, { lastStatusChange: initial() })),
        switchMap(handler$),
        handleApiResponse(
          () => {
            patchState(this, { lastStatusChange: success(null) });
          },
          {
            onSuccess: () => {
              this.store.dispatch(SharedDataAccessGesuchEvents.loadGesuch());
              onSuccess?.();
            },
          },
        ),
      );
    };

  cockpitViewSig = computed(() => {
    return {
      gesuche: fromCachedDataSig(this.gesuche),
      loading: isPending(this.gesuche()),
    };
  });

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
    EINGEREICHT: rxMethod<{ gesuchId: string }>(
      pipe(
        this.handleStatusChange(({ gesuchId }) =>
          this.gesuchService.gesuchEinreichen$({
            gesuchId,
          }),
        ),
      ),
    ),

    VERFUEGT: rxMethod<{ gesuchId: string }>(
      pipe(
        this.handleStatusChange(({ gesuchId }) =>
          this.gesuchService.changeGesuchStatusToVerfuegt$({
            gesuchId,
          }),
        ),
      ),
    ),

    VERSENDET: rxMethod<{ gesuchId: string }>(
      pipe(
        this.handleStatusChange(({ gesuchId }) =>
          this.gesuchService.changeGesuchStatusToVersendet$({
            gesuchId,
          }),
        ),
      ),
    ),

    BEREIT_FUER_BEARBEITUNG: rxMethod<{ gesuchId: string; text?: string }>(
      pipe(
        this.handleStatusChange(({ gesuchId, text }) =>
          this.gesuchService.changeGesuchStatusToBereitFuerBearbeitung$({
            gesuchId,
            ...(text ? { kommentar: { text } } : {}),
          }),
        ),
      ),
    ),

    BEARBEITUNG_ABSCHLIESSEN: rxMethod<{ gesuchId: string }>(
      pipe(
        this.handleStatusChange(({ gesuchId }) =>
          this.gesuchService.bearbeitungAbschliessen$({
            gesuchId,
          }),
        ),
      ),
    ),

    ZURUECKWEISEN: rxMethod<{ gesuchId: string; text: string }>(
      pipe(
        this.handleStatusChange(({ gesuchId, text }) =>
          this.gesuchService.gesuchZurueckweisen$({
            gesuchId,
            kommentar: { text },
          }),
        ),
      ),
    ),

    NEGATIVE_VERFUEGUNG_ERSTELLEN: rxMethod<{
      gesuchId: string;
      grundId: string;
    }>(
      pipe(
        this.handleStatusChange(({ gesuchId, grundId }) =>
          this.gesuchService.changeGesuchStatusToNegativeVerfuegung$({
            gesuchId,
            ausgewaehlterGrund: { decisionId: grundId },
          }),
        ),
      ),
    ),
  } satisfies Record<StatusUebergang, unknown>;
}
