import { inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { lastValueFrom } from 'rxjs/internal/lastValueFrom';

import { BuchstabenZuordnungUpdate } from '@dv/sachbearbeitung-app/model/administration';
import { Benutzer, BenutzerService } from '@dv/shared/model/gesuch';

type SachbearbeiterState = {
  sachbearbeiter?: Benutzer[];
  loading?: boolean;
  error?: string;
};

const initialState: SachbearbeiterState = {
  sachbearbeiter: undefined,
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
      for (const zuordnung of zuordnungen) {
        await lastValueFrom(
          benutzerService.createOrUpdateSachbearbeiterStammdaten$(zuordnung),
        );
      }
      await loadSachbearbeiterZuweisung();
    }

    return {
      loadSachbearbeiterZuweisung,
      saveSachbearbeiterZuweisung,
    };
  }),
);
