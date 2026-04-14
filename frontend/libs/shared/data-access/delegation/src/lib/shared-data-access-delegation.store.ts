import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import {
  DelegierenService,
  Delegierung,
  DelegierungStatus,
  StatusColor,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';

type DelegationState = {
  delegationen: CachedRemoteData<Delegierung[]>;
};

const initialState: DelegationState = {
  delegationen: initial(),
};

const statusColorMap: Record<DelegierungStatus, StatusColor> = {
  EINGEREICHT: 'caution',
  AKZEPTIERT: 'success',
  ABGELEHNT: 'warn',
  BEENDET: 'warn',
} as const;

@Injectable()
export class DelegationStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private delegierenService = inject(DelegierenService);

  delegationenViewSig = computed(() => {
    return fromCachedDataSig(this.delegationen)?.map((d) => ({
      ...d,
      color: statusColorMap[d.status],
    }));
  });

  loadDelegationen$ = rxMethod<{ gesuchId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          delegationen: cachedPending(state.delegationen),
        }));
      }),
      switchMap(({ gesuchId }) =>
        this.delegierenService
          .getAllDelegierungsForGesuch$({ gesuchId })
          .pipe(
            handleApiResponse((delegationen) =>
              patchState(this, { delegationen }),
            ),
          ),
      ),
    ),
  );
}
