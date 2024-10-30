import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { endOfDay, format } from 'date-fns';
import { pipe, switchMap, tap } from 'rxjs';

import { AppType, SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import {
  Ausbildung,
  AusbildungService,
  AusbildungUpdate,
  Gesuchstatus,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';

type AusbildungState = {
  ausbildung: CachedRemoteData<Ausbildung>;
};

const initialState: AusbildungState = {
  ausbildung: initial(),
};

const readonlyMap = {
  'gesuch-app': 'IN_BEARBEITUNG_GS',
  'sachbearbeitung-app': 'IN_BEARBEITUNG_SB',
} satisfies Record<AppType, Gesuchstatus>;

@Injectable()
export class AusbildungStore extends signalStore(
  { protectedState: false },
  withState(initialState),
  withDevtools('AusbildungStore'),
) {
  private config = inject(SharedModelCompileTimeConfig);
  private ausbildungService = inject(AusbildungService);

  ausbildungViewSig = computed(() => {
    const ausbildung = fromCachedDataSig(this.ausbildung);
    const minEndDatum = endOfDay(new Date());

    return {
      ausbildung,
      minEndDatum,
      minEndDatumFormatted: format(minEndDatum, 'MM.yyyy'),
      writeableWhen: readonlyMap[this.config.appType],
    };
  });

  loadAusbildung$ = rxMethod<{ ausbildungId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          ausbildung: cachedPending(state.ausbildung),
        }));
      }),
      switchMap(({ ausbildungId }) =>
        this.ausbildungService
          .getAusbildung$({ ausbildungId })
          .pipe(
            handleApiResponse((ausbildung) => patchState(this, { ausbildung })),
          ),
      ),
    ),
  );

  createAusbildung$ = rxMethod<AusbildungUpdate>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          ausbildung: cachedPending(state.ausbildung),
        }));
      }),
      switchMap((ausbildungUpdate) =>
        this.ausbildungService
          .createAusbildung$({ ausbildungUpdate })
          .pipe(
            handleApiResponse((ausbildung) => patchState(this, { ausbildung })),
          ),
      ),
    ),
  );

  // saveAusbildung$ = rxMethod<{
  //   ausbildungId: string;
  //   values: unknown;
  // }>(
  //   pipe(
  //     tap(() => {
  //       patchState(this, (state) => ({
  //         ausbildung: cachedPending(state.ausbildung),
  //       }));
  //     }),
  //     switchMap(({ ausbildungId, values }) =>
  //       this.ausbildungService
  //         .updateAusbildung$({
  //           ausbildungId,
  //           payload: values,
  //         })
  //         .pipe(
  //           handleApiResponse(
  //             (ausbildung) => {
  //               patchState(this, { ausbildung });
  //             },
  //             {
  //               onSuccess: (ausbildung) => {
  //                 // Do something after save, like showing a notification
  //               },
  //             },
  //           ),
  //         ),
  //     ),
  //   ),
  // );
}
