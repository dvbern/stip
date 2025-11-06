import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { AdminDokumente, GesuchService } from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';

// Dummy types until the contract is updated
export interface StipendienDokument {
  id: string;
  datum: string;
  versendetVerfuegung?: string;
  verfuegungsbrief?: string;
  berechnungsblaetter?: string;
  gesetzlichesDarlehen?: string;
}

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
  adminDokumente: CachedRemoteData<AdminDokumente>;
  stipendienDokumente: CachedRemoteData<StipendienDokument[]>;
  darlehenDokumente: CachedRemoteData<DarlehenDokument[]>;
  datenschutzbriefeDokumente: CachedRemoteData<DatenschutzbriefDokument[]>;
};

const initialState: InfosAdminState = {
  adminDokumente: initial(),
  stipendienDokumente: initial(),
  darlehenDokumente: initial(),
  datenschutzbriefeDokumente: initial(),
};

@Injectable()
export class InfosGesuchsdokumenteStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private gesuchService = inject(GesuchService);

  adminDokumenteViewSig = computed(() => {
    return fromCachedDataSig(this.adminDokumente);
  });

  stipendienDokumenteViewSig = computed(() => {
    return fromCachedDataSig(this.stipendienDokumente);
  });

  darlehenDokumenteViewSig = computed(() => {
    return fromCachedDataSig(this.darlehenDokumente);
  });

  datenschutzbriefeDokumenteViewSig = computed(() => {
    return fromCachedDataSig(this.datenschutzbriefeDokumente);
  });

  loadAdminDokumente$ = rxMethod<{ gesuchId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          adminDokumente: cachedPending(state.adminDokumente),
        }));
      }),
      switchMap(({ gesuchId }) =>
        this.gesuchService
          .getAdminDokumente$({
            gesuchId,
          })
          .pipe(
            handleApiResponse((adminDokumente) =>
              patchState(this, { adminDokumente }),
            ),
          ),
      ),
    ),
  );

  // Dummy implementations for loading document lists
  // TODO: Replace with actual API calls when contract is updated
  loadStipendienDokumente$ = rxMethod<{ gesuchId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          stipendienDokumente: cachedPending(state.stipendienDokumente),
        }));
      }),
      tap(() => {
        // Dummy data - will be replaced with actual API call
        const dummyData: StipendienDokument[] = [
          {
            id: '1',
            datum: new Date().toISOString(),
            versendetVerfuegung: 'verfuegung_1.pdf',
            verfuegungsbrief: 'brief_1.pdf',
            berechnungsblaetter: 'berechnung_1.pdf',
            gesetzlichesDarlehen: 'darlehen_1.pdf',
          },
        ];
        patchState(this, {
          stipendienDokumente: { data: dummyData, type: 'success' },
        });
      }),
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
