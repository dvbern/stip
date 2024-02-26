import { inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { lastValueFrom } from 'rxjs/internal/lastValueFrom';

import { BuchstabenZuordnungUpdate } from '@dv/sachbearbeitung-app/model/administration';
import { Benutzer, BenutzerService } from '@dv/shared/model/gesuch';

type SachbearbeiterState = {
  sachbearbeiter?: Benutzer[];
  hasLoadedOnce: boolean;
  loading: boolean;
  error?: string;
};

const initialState: SachbearbeiterState = {
  sachbearbeiter: undefined,
  hasLoadedOnce: false,
  loading: false,
  error: undefined,
};

export const SachbearbeiterStore = signalStore(
  withState(initialState),
  withMethods((store, benutzerService = inject(BenutzerService)) => {
    async function loadSachbearbeiterZuweisung() {
      patchState(store, {
        loading: true,
        error: undefined,
      });
      const sachbearbeiter = await lastValueFrom(
        benutzerService.getSachbearbeitende$(),
      );
      patchState(store, {
        sachbearbeiter: sachbearbeiter,
        hasLoadedOnce: true,
        loading: false,
        error: undefined,
      });
    }

    async function saveSachbearbeiterZuweisung(
      zuordnungen: BuchstabenZuordnungUpdate[],
    ) {
      patchState(store, {
        loading: true,
        error: undefined,
      });
      await lastValueFrom(
        benutzerService.createOrUpdateSachbearbeiterStammdatenList$({
          sachbearbeiterZuordnungStammdatenList: zuordnungen,
        }),
      );
      await loadSachbearbeiterZuweisung();
    }

    return {
      loadSachbearbeiterZuweisung,
      saveSachbearbeiterZuweisung,
    };
  }),
);
