import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { BuchstabenZuordnungUpdate } from '@dv/sachbearbeitung-app/model/administration';
import { Benutzer, BenutzerService } from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  catchRemoteDataError,
  fromCachedDataSig,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';

type SachbearbeiterState = {
  sachbearbeiter: CachedRemoteData<Benutzer[]>;
};

const initialState: SachbearbeiterState = {
  sachbearbeiter: initial(),
};

@Injectable()
export class SachbearbeiterStore extends signalStore(
  { protectedState: false },

  withState(initialState),
) {
  private benutzerService = inject(BenutzerService);

  zuweisungenViewSig = computed(
    () =>
      fromCachedDataSig(this.sachbearbeiter)?.map((u) => ({
        benutzerId: u.id,
        fullName: `${u.vorname} ${u.nachname}`,
        enabledDe:
          (u.sachbearbeiterZuordnungStammdaten?.buchstabenDe?.length ?? 0) > 0,
        buchstabenDe: u.sachbearbeiterZuordnungStammdaten?.buchstabenDe,
        enabledFr:
          (u.sachbearbeiterZuordnungStammdaten?.buchstabenFr?.length ?? 0) > 0,
        buchstabenFr: u.sachbearbeiterZuordnungStammdaten?.buchstabenFr,
      })) ?? [],
  );

  private loadZuweisungen = pipe(
    switchMap(() =>
      this.benutzerService.getSachbearbeitende$().pipe(
        handleApiResponse((sachbearbeiter) =>
          patchState(this, {
            sachbearbeiter,
          }),
        ),
      ),
    ),
  );

  loadSachbearbeiterZuweisungen$ = rxMethod<void>(
    pipe(
      tap(() =>
        patchState(this, (state) => ({
          sachbearbeiter: cachedPending(state.sachbearbeiter),
        })),
      ),
      this.loadZuweisungen,
    ),
  );

  saveSachbearbeiterZuweisung$ = rxMethod<BuchstabenZuordnungUpdate[]>(
    pipe(
      tap(() =>
        patchState(this, (state) => ({
          sachbearbeiter: cachedPending(state.sachbearbeiter),
        })),
      ),
      switchMap((zuordnungen) =>
        this.benutzerService
          .createOrUpdateSachbearbeiterStammdatenList$({
            sachbearbeiterZuordnungStammdatenList: zuordnungen,
          })
          .pipe(catchRemoteDataError()),
      ),
      this.loadZuweisungen,
    ),
  );
}
