import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

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
