import { Injectable, inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { BehaviorSubject, lastValueFrom, of, take } from 'rxjs';

import { GesuchsperiodeDaten } from '@dv/shared/model/gesuch';

type GesuchsperiodeState = {
  currentGesuchsperiode?: GesuchsperiodeDaten;
  hasLoadedOnce: boolean;
  loading: boolean;
  error?: string;
};

@Injectable({ providedIn: 'root' })
export class MockGesuchsperiodenService {
  private gesuchsperiode = new BehaviorSubject<GesuchsperiodeDaten | undefined>(
    ____mockdata,
  );
  loadGesuchsperiode() {
    return this.gesuchsperiode.pipe(take(1));
  }

  saveGesuchsperiode(gesuchsperiodenDaten: GesuchsperiodeDaten) {
    this.gesuchsperiode.next(gesuchsperiodenDaten);
    return of('');
  }
}

const initialState: GesuchsperiodeState = {
  currentGesuchsperiode: undefined,
  hasLoadedOnce: false,
  loading: false,
  error: undefined,
};
export const GesuchsperiodeStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods(
    (store, gesuchsperiodeService = inject(MockGesuchsperiodenService)) => {
      async function loadGesuchsperiode() {
        patchState(store, {
          loading: true,
          error: undefined,
        });
        const gesuchsperiode = await lastValueFrom(
          gesuchsperiodeService.loadGesuchsperiode(),
        );
        patchState(store, {
          currentGesuchsperiode: gesuchsperiode,
          hasLoadedOnce: false,
          loading: false,
          error: undefined,
        });
      }
      async function saveGesuchsperiode(
        gesuchsperiodenDaten: GesuchsperiodeDaten,
      ) {
        patchState(store, {
          loading: true,
          error: undefined,
        });
        await lastValueFrom(
          gesuchsperiodeService.saveGesuchsperiode(gesuchsperiodenDaten),
        );
        await loadGesuchsperiode();
      }
      return {
        loadGesuchsperiode,
        saveGesuchsperiode,
      };
    },
  ),
);

const ____mockdata = {
  _00_18: 3,
  _19_25: 65,
  _26_99: 3,
  aufschaltterminStopp: new Date().toISOString(),
  ausbKosten_SekII: 43,
  ausbKosten_Tertiaer: 20,
  bB_1Pers: 1,
  bB_2Pers: 2,
  bB_3Pers: 3,
  bB_4Pers: 5,
  bB_5Pers: 5,
  b_Einkommenfreibetrag: 3,
  b_VermogenSatzAngerechnet: 2,
  b_Verpf_Auswaerts_Tagessatz: 2,
  bezeichnung: '1a',
  einreichefristNormal: new Date().toISOString(),
  einreichefristReduziert: new Date().toISOString(),
  elternbeteiligungssatz: 23,
  fB_1Pers: 20,
  fB_2Pers: 40,
  fB_3Pers: 0,
  fB_4Pers: 0,
  fB_5Pers: 0,
  f_Einkommensfreibetrag: 0,
  f_Vermoegensfreibetrag: 0,
  f_VermogenSatzAngerechnet: 0,
  fiskaljahr: 'awd',
  gesuchsperiodeStart: new Date().toISOString(),
  gesuchsperiodeStopp: new Date().toISOString(),
  integrationszulage: 0,
  limite_EkFreibetrag_Integrationszulag: 0,
  person_1: 0,
  person_2: 0,
  person_3: 0,
  person_4: 0,
  person_5: 0,
  person_6: 0,
  person_7: 0,
  ppP_8: 0,
  stipLimite_Minimalstipendium: 0,
  aufschaltterminStart: new Date().toISOString(),
};
