import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/global/notification';
import {
  BuchhaltungEntry,
  BuchhaltungOverview,
  BuchhaltungSaldokorrektur,
  BuchhaltungService,
  PaginatedFailedAuszahlungBuchhaltung,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  RemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  isSuccess,
  mapCachedData,
  pending,
} from '@dv/shared/util/remote-data';

export type BuchhaltungEntryView =
  | { type: 'entry'; entry: BuchhaltungEntry }
  | { type: 'gesuchStart'; entry: BuchhaltungEntry };

type BuchhaltungState = {
  buchhaltung: CachedRemoteData<BuchhaltungOverview>;
  paginatedFailedAuszahlungBuchhaltung: CachedRemoteData<PaginatedFailedAuszahlungBuchhaltung>;
  retriedAuszahlung: RemoteData<BuchhaltungEntry>;
};

const initialState: BuchhaltungState = {
  buchhaltung: initial(),
  paginatedFailedAuszahlungBuchhaltung: initial(),
  retriedAuszahlung: initial(),
};

@Injectable()
export class BuchhaltungStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private buchhaltungService = inject(BuchhaltungService);
  private globalNotificationStore = inject(GlobalNotificationStore);

  fehlgeschlageneZahlungenView = computed(() => {
    return {
      data: fromCachedDataSig(this.paginatedFailedAuszahlungBuchhaltung),
      loading: this.paginatedFailedAuszahlungBuchhaltung().type === 'pending',
    };
  });

  buchhaltungEntriesViewSig = computed(() => {
    const data = fromCachedDataSig(this.buchhaltung);

    if (!data?.buchhaltungEntrys) {
      return {
        buchhaltungEntrys: [] as BuchhaltungEntryView[],
        canRetryAuszahlung: data?.canRetryAuszahlung,
      };
    }

    const buchhaltungEntrys = data.buchhaltungEntrys.reduce(
      (acc, entry, index) => {
        const previousEntry = data.buchhaltungEntrys[index - 1];
        const isStartOfNewGesuch =
          previousEntry && previousEntry.gesuchId !== entry.gesuchId;
        return [
          ...acc,
          ...(isStartOfNewGesuch
            ? [{ type: 'gesuchStart' as const, entry }]
            : []),
          {
            type: 'entry' as const,
            entry,
          },
        ];
      },
      [] as BuchhaltungEntryView[],
    );

    return {
      buchhaltungEntrys,
      canRetryAuszahlung: data.canRetryAuszahlung,
    };
  });

  loadBuchhaltung$ = rxMethod<{
    gesuchId: string;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          buchhaltung: cachedPending(state.buchhaltung),
        }));
      }),
      switchMap(({ gesuchId }) =>
        this.buchhaltungService
          .getBuchhaltungEntrys$({
            gesuchId,
          })
          .pipe(
            handleApiResponse((buchhaltung) =>
              patchState(this, { buchhaltung }),
            ),
          ),
      ),
    ),
  );

  createBuchhaltungsKorrektur$ = rxMethod<{
    buchhaltungSaldokorrektur: BuchhaltungSaldokorrektur;
    gesuchId: string;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          buchhaltung: cachedPending(state.buchhaltung),
        }));
      }),
      switchMap(({ buchhaltungSaldokorrektur, gesuchId }) =>
        this.buchhaltungService
          .createBuchhaltungSaldokorrektur$({
            buchhaltungSaldokorrektur,
            gesuchId,
          })
          .pipe(
            handleApiResponse(
              () => {
                this.loadBuchhaltung$({ gesuchId });
              },
              {
                onSuccess: () => {
                  this.globalNotificationStore.createSuccessNotification({
                    messageKey:
                      'sachbearbeitung-app.infos.buchhaltung.notification.buchhaltungSaldokorrekturSuccess',
                  });
                },
              },
            ),
          ),
      ),
    ),
  );

  retryFailedAuszahlung$ = rxMethod<{ gesuchId: string }>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          retriedAuszahlung: pending(),
        }));
      }),
      switchMap(({ gesuchId }) =>
        this.buchhaltungService
          .retryFailedAuszahlungBuchhaltungForGesuch$({ gesuchId })
          .pipe(
            handleApiResponse((retriedAuszahlung) =>
              patchState(this, (state) => ({
                retriedAuszahlung,
                buchhaltung: mapCachedData(state.buchhaltung, (buchhaltung) => {
                  if (isSuccess(retriedAuszahlung)) {
                    return {
                      ...buchhaltung,
                      buchhaltungEntrys: [
                        ...buchhaltung.buchhaltungEntrys,
                        retriedAuszahlung.data,
                      ],
                    };
                  }
                  return buchhaltung;
                }),
              })),
            ),
          ),
      ),
    ),
  );

  getFehlgeschlageneZahlungen$ = rxMethod<{
    page: number;
    pageSize: number;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          paginatedFailedAuszahlungBuchhaltung: cachedPending(
            state.paginatedFailedAuszahlungBuchhaltung,
          ),
        }));
      }),
      switchMap(({ page, pageSize }) =>
        this.buchhaltungService
          .getFailedAuszahlungBuchhaltungEntrys$({
            page,
            pageSize,
          })
          .pipe(
            handleApiResponse((paginatedFailedAuszahlungBuchhaltung) =>
              patchState(this, { paginatedFailedAuszahlungBuchhaltung }),
            ),
          ),
      ),
    ),
  );
}
