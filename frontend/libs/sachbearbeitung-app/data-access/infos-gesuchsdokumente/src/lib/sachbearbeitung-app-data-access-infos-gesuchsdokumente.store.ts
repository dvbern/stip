import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import {
  DarlehenBuchhaltungEntry,
  DarlehenService,
  DarlehenServiceGetDarlehenBuchhaltungEntrysRequestParams,
  Verfuegung,
  VerfuegungService,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  isPending,
} from '@dv/shared/util/remote-data';

// Todo KSTIP-2697 Dummy types until the contract is updated
export interface DarlehenDokument {
  id: string;
  datum: string;
  kategorie: string;
  darlehensverfuegung?: string;
  gesetzlichesDarlehen?: string;
  freiwilligesDarlehen?: string;
  kommentar?: string;
}

// Todo: Dummy types until the contract is updated Task unkown
export interface DatenschutzbriefDokument {
  id: string;
  datum: string;
  kategorie: string;
  sachbearbeiter: string;
  person: string;
  massendruckJobId?: string;
}

type InfosAdminState = {
  verfuegungen: CachedRemoteData<Verfuegung[]>;
  darlehenBuchhaltungEntrys: CachedRemoteData<DarlehenBuchhaltungEntry[]>;
  datenschutzbriefeDokumente: CachedRemoteData<DatenschutzbriefDokument[]>;
};

const initialState: InfosAdminState = {
  verfuegungen: initial(),
  darlehenBuchhaltungEntrys: initial(),
  datenschutzbriefeDokumente: initial(),
};

@Injectable()
export class InfosGesuchsdokumenteStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private verfuegungService = inject(VerfuegungService);
  private darlehenService = inject(DarlehenService);

  verfuegungenViewSig = computed(() => {
    return {
      verfuegungen: fromCachedDataSig(this.verfuegungen),
      loading: isPending(this.verfuegungen()),
    };
  });

  darlehenDokumenteViewSig = computed(() => {
    return {
      darlehenDokumente: fromCachedDataSig(this.darlehenBuchhaltungEntrys),
      loading: isPending(this.darlehenBuchhaltungEntrys()),
    };
  });

  datenschutzbriefeDokumenteViewSig = computed(() => {
    return {
      datenschutzbriefe: fromCachedDataSig(this.datenschutzbriefeDokumente),
      loading: isPending(this.datenschutzbriefeDokumente()),
    };
  });

  loadVerfuegungDokumente$ = rxMethod<{ gesuchId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          verfuegungen: cachedPending(state.verfuegungen),
        }));
      }),
      switchMap(({ gesuchId }) =>
        this.verfuegungService
          .getVerfuegungen$({
            gesuchId,
          })
          .pipe(
            handleApiResponse((verfuegungen) =>
              patchState(this, { verfuegungen }),
            ),
          ),
      ),
    ),
  );

  loadDarlehenBuchhaltungEntrys$ =
    rxMethod<DarlehenServiceGetDarlehenBuchhaltungEntrysRequestParams>(
      pipe(
        tap(() => {
          patchState(this, (state) => ({
            darlehenBuchhaltungEntrys: cachedPending(
              state.darlehenBuchhaltungEntrys,
            ),
          }));
        }),
        switchMap((req) =>
          this.darlehenService.getDarlehenBuchhaltungEntrys$(req).pipe(
            handleApiResponse((darlehenBuchhaltungEntrys) => {
              patchState(this, { darlehenBuchhaltungEntrys });
            }),
          ),
        ),
      ),
    );

  loadDatenschutzbriefeDokumente$ = rxMethod<{ gesuchId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          datenschutzbriefeDokumente: cachedPending(
            state.datenschutzbriefeDokumente,
          ),
        }));
      }),
      tap(() => {
        // Todo KSTIP-2697: Dummy data - will be replaced with actual API call
        const dummyData: DatenschutzbriefDokument[] = [
          {
            id: '1',
            datum: new Date().toISOString(),
            kategorie: 'Standard',
            sachbearbeiter: 'Max Mustermann',
            person: 'Anna Schmidt',
            massendruckJobId: 'job-123',
          },
        ];
        patchState(this, {
          datenschutzbriefeDokumente: { data: dummyData, type: 'success' },
        });
      }),
    ),
  );
}
