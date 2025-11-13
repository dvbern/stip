import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { Verfuegung, VerfuegungService } from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  isPending,
} from '@dv/shared/util/remote-data';

// Dummy types until the contract is updated
export interface DarlehenDokument {
  id: string;
  datum: string;
  kategorie: string;
  darlehensverfuegung?: string;
  gesetzlichesDarlehen?: string;
  freiwilligesDarlehen?: string;
  kommentar?: string;
}

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
  darlehenDokumente: CachedRemoteData<DarlehenDokument[]>;
  datenschutzbriefeDokumente: CachedRemoteData<DatenschutzbriefDokument[]>;
};

const initialState: InfosAdminState = {
  verfuegungen: initial(),
  darlehenDokumente: initial(),
  datenschutzbriefeDokumente: initial(),
};

@Injectable()
export class InfosGesuchsdokumenteStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private verfuegungService = inject(VerfuegungService);

  verfuegungenViewSig = computed(() => {
    return {
      verfuegungen: fromCachedDataSig(this.verfuegungen),
      loading: isPending(this.verfuegungen()),
    };
  });

  darlehenDokumenteViewSig = computed(() => {
    return fromCachedDataSig(this.darlehenDokumente);
  });

  datenschutzbriefeDokumenteViewSig = computed(() => {
    return fromCachedDataSig(this.datenschutzbriefeDokumente);
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

  loadDarlehenDokumente$ = rxMethod<{ gesuchId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          darlehenDokumente: cachedPending(state.darlehenDokumente),
        }));
      }),
      tap(() => {
        // Dummy data - will be replaced with actual API call
        const dummyData: DarlehenDokument[] = [
          {
            id: '1',
            datum: new Date().toISOString(),
            kategorie: 'Gesetzlich',
            darlehensverfuegung: 'darlehen_verfuegung_1.pdf',
            gesetzlichesDarlehen: 'gesetzlich_1.pdf',
            kommentar: 'Test Kommentar',
          },
        ];
        patchState(this, {
          darlehenDokumente: { data: dummyData, type: 'success' },
        });
      }),
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
        // Dummy data - will be replaced with actual API call
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
