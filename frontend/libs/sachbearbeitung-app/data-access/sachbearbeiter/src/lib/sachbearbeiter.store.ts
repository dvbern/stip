import { Injectable, computed, inject } from '@angular/core';
import { patchState, withDevtools } from '@angular-architects/ngrx-toolkit';
import { signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { BuchstabenZuordnungUpdate } from '@dv/sachbearbeitung-app/model/administration';
import { Benutzer, BenutzerService } from '@dv/shared/model/gesuch';
import {
  RemoteData,
  handleApiResponse,
  initial,
  pending,
} from '@dv/shared/util/remote-data';

type SachbearbeiterState = {
  sachbearbeiter: RemoteData<Benutzer[]>;
};

const initialState: SachbearbeiterState = {
  sachbearbeiter: initial(),
};

@Injectable()
export class SachbearbeiterStore extends signalStore(
  withDevtools('SachbearbeiterStore'),
  withState(initialState),
) {
  private benutzerService = inject(BenutzerService);
  private loadZuweisungen = pipe(
    tap(() =>
      patchState(this, 'loadZuweisungen', {
        sachbearbeiter: pending(),
      }),
    ),
    switchMap(() =>
      this.benutzerService.getSachbearbeitende$().pipe(
        handleApiResponse((sachbearbeiter) =>
          patchState(this, 'loadZuweisungen', {
            sachbearbeiter,
          }),
        ),
      ),
    ),
  );

  loadSachbearbeiterZuweisungen$ = rxMethod<void>(this.loadZuweisungen);

  saveSachbearbeiterZuweisung$ = rxMethod<BuchstabenZuordnungUpdate[]>(
    pipe(
      tap(() =>
        patchState(this, 'saveSachbearbeiterZuweisung', {
          sachbearbeiter: pending(),
        }),
      ),
      switchMap((zuordnungen) =>
        this.benutzerService
          .createOrUpdateSachbearbeiterStammdatenList$({
            sachbearbeiterZuordnungStammdatenList: zuordnungen,
          })
          .pipe(switchMap(this.loadZuweisungen)),
      ),
    ),
  );

  zuweisungenViewSig = computed(
    () =>
      this.sachbearbeiter().data?.map((u) => ({
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
}
