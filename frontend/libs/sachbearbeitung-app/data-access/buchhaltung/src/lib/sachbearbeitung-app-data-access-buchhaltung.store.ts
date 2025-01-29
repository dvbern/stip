import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { map, pipe, switchMap, tap } from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/global/notification';
import {
  BuchhaltungEntry,
  BuchhaltungSaldokorrektur,
  BuchhaltungService,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';

export type BuchhaltungEntryView =
  | { type: 'entry'; entry: BuchhaltungEntry }
  | { type: 'gesuchStart'; entry: BuchhaltungEntry };

type BuchhaltungState = {
  buchhaltung: CachedRemoteData<BuchhaltungEntry[]>;
};

const initialState: BuchhaltungState = {
  buchhaltung: initial(),
};

@Injectable()
export class BuchhaltungStore extends signalStore(
  { protectedState: false },
  withState(initialState),
  withDevtools('BuchhaltungStore'),
) {
  private buchhaltungService = inject(BuchhaltungService);
  private globalNotificationStore = inject(GlobalNotificationStore);

  buchhaltungEntriesViewSig = computed(() => {
    const buchhaltungEntries = this.buchhaltung().data;

    if (!buchhaltungEntries) {
      return [];
    }

    return buchhaltungEntries.reduce((acc, entry, index) => {
      const previousEntry = buchhaltungEntries[index - 1];
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
    }, [] as BuchhaltungEntryView[]);
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
            map(
              (buchhaltungEntries) =>
                [
                  // TODO (KSTIP-1622): Remove once real data is available
                  {
                    buchhaltungType: 'SALDOAENDERUNG',
                    timestampErstellt: new Date('2024-10-31').toISOString(),
                    gesuchId: 'gesuch-1',
                    comment: 'Manuelle Saldokorrektur',
                    saldoAenderung: -1250,
                    saldo: -1250,
                  },
                  {
                    buchhaltungType: 'STIPENDIUM',
                    timestampErstellt: new Date('2024-11-05').toISOString(),
                    gesuchId: 'gesuch-1',
                    comment: 'Verfügung Erstgesuch',
                    stipendienBetrag: 10000,
                    saldoAenderung: 10000,
                    saldo: 8750,
                    verfuegungId: 'verfuegung-1',
                  },
                  {
                    buchhaltungType: 'AUSZAHLUNG',
                    timestampErstellt: new Date('2024-11-07').toISOString(),
                    gesuchId: 'gesuch-1',
                    comment: 'Auszahlung',
                    saldoAenderung: -3750,
                    saldo: 5000,
                    auszahlung: 3750,
                  },
                  ...(buchhaltungEntries ?? []),
                ] satisfies BuchhaltungEntry[],
            ),
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
}
