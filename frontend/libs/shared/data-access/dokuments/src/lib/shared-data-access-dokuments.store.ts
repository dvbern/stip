import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import {
  DokumentService,
  DokumentServiceGesuchDokumentAblehnenRequestParams,
  DokumentServiceGesuchDokumentAkzeptierenRequestParams,
  GesuchService,
  GesuchServiceGesuchFehlendeDokumenteRequestParams,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  RemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  pending,
} from '@dv/shared/util/remote-data';

type DokumentsState = {
  cachedDokuments: CachedRemoteData<unknown>;
  dokuments: RemoteData<unknown>;
};

const initialState: DokumentsState = {
  cachedDokuments: initial(),
  dokuments: initial(),
};

@Injectable()
export class DokumentsStore extends signalStore(
  withState(initialState),
  withDevtools('DokumentsStore'),
) {
  private dokumentService = inject(DokumentService);
  private gesuchService = inject(GesuchService);

  cachedDokumentsListViewSig = computed(() => {
    return fromCachedDataSig(this.cachedDokuments);
  });

  dokumentsViewSig = computed(() => {
    return this.dokuments.data();
  });

  gesuchDokumentAblehnen$ =
    rxMethod<DokumentServiceGesuchDokumentAblehnenRequestParams>(
      pipe(
        switchMap((req) => {
          return this.dokumentService.gesuchDokumentAblehnen$(req);
        }),
      ),
    );

  gesuchDokumentAkzeptieren$ =
    rxMethod<DokumentServiceGesuchDokumentAkzeptierenRequestParams>(
      pipe(
        switchMap((req) => {
          return this.dokumentService.gesuchDokumentAkzeptieren$(req);
        }),
      ),
    );

  setFehlendeDokumente$ =
    rxMethod<GesuchServiceGesuchFehlendeDokumenteRequestParams>(
      pipe(
        switchMap((req) => {
          return this.gesuchService.gesuchFehlendeDokumente$(req);
        }),
      ),
    );

  // loadDokuments$ = rxMethod<void>(
  //   pipe(
  //     tap(() => {
  //       patchState(this, () => ({
  //         dokuments: pending(),
  //       }));
  //     }),
  //     switchMap(() =>
  //       this.dokumentService
  //         .getDokuments$()
  //         .pipe(
  //           handleApiResponse((dokuments) => patchState(this, { dokuments })),
  //         ),
  //     ),
  //   ),
  // );
}
