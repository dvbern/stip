import { Injectable, computed, inject } from '@angular/core';
import {
  patchState,
  signalStore,
  withComputed,
  withMethods,
  withState,
} from '@ngrx/signals';
import { BehaviorSubject, lastValueFrom, map, of, take } from 'rxjs';

import {
  Gesuchsjahr,
  GesuchsjahrService,
  Gesuchsperiode,
  GesuchsperiodeDaten,
  GesuchsperiodeService,
} from '@dv/shared/model/gesuch';
import {
  formatBackendLocalDate,
  fromBackendLocalDate,
} from '@dv/shared/util/validator-date';

type GesuchsperiodeState = {
  gesuchsjahre: Gesuchsjahr[];
  gesuchsperioden: Gesuchsperiode[];
  currentGesuchsperiode?: GesuchsperiodeDaten;
  currentGesuchsJahr?: Gesuchsjahr;
  hasLoadedOnce: boolean;
  loading: boolean;
  error?: string;
};

@Injectable({ providedIn: 'root' })
export class MockGesuchsperiodenService {
  private gesuchsperiode = new BehaviorSubject<GesuchsperiodeDaten | undefined>(
    ____mockdata,
  );
  private originalService = inject(GesuchsperiodeService);

  getGesuchsperioden$() {
    return this.originalService
      .getGesuchsperioden$()
      .pipe(map((list) => list.map(____mockGesuchperiode)));
  }

  getGesuchsperiode$() {
    return this.gesuchsperiode.pipe(take(1));
  }

  saveGesuchsperiode$(gesuchsperiodenDaten: GesuchsperiodeDaten) {
    this.gesuchsperiode.next(gesuchsperiodenDaten);
    return of('');
  }
}

const initialState: GesuchsperiodeState = {
  gesuchsjahre: [],
  gesuchsperioden: [],
  currentGesuchsJahr: undefined,
  currentGesuchsperiode: undefined,
  hasLoadedOnce: false,
  loading: false,
  error: undefined,
};
export const GesuchsperiodeStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods(
    (
      store,
      gesuchsperiodeService = inject(MockGesuchsperiodenService),
      gesuchjahrService = inject(GesuchsjahrService),
    ) => {
      async function loadOverview() {
        patchState(store, {
          loading: true,
          error: undefined,
        });
        const gesuchsperioden = await lastValueFrom(
          gesuchsperiodeService.getGesuchsperioden$(),
        );

        const gesuchsjahre = await lastValueFrom(
          gesuchjahrService.getGesuchsjahre$(),
        );
        patchState(store, {
          gesuchsperioden,
          gesuchsjahre,
          hasLoadedOnce: true,
          loading: false,
          error: undefined,
        });
      }
      async function loadGesuchsperiode() {
        patchState(store, {
          loading: true,
          error: undefined,
        });
        const gesuchsperiode = await lastValueFrom(
          gesuchsperiodeService.getGesuchsperiode$(),
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
          gesuchsperiodeService.saveGesuchsperiode$(gesuchsperiodenDaten),
        );
        await loadGesuchsperiode();
      }
      return {
        loadOverview,
        loadGesuchsperiode,
        saveGesuchsperiode,
      };
    },
  ),
  withComputed((store) => ({
    gesuchperiodenListView: computed(() => {
      return store.gesuchsperioden().map((g) => ({
        ...g,
        gesuchsperiode:
          formatBackendLocalDate(g.gueltigAb, 'de') +
          ' - ' +
          formatBackendLocalDate(g.gueltigBis, 'de'),
        gesuchsjahr: fromBackendLocalDate(g.aufschaltdatum)?.getFullYear(),
      }));
    }),
  })),
);

const ____mockdatata: Gesuchsjahr = {
  id: 'a',
  bezeichnungDe: 'SA',
  bezeichnungFr: 'LA',
  technischesJahr: 2922,
  ausbildungsjahrEnde: 'wadc',
  ausbildungsjahrStart: 'anjsd',
  gueltigkeitStatus: 'ARCHIVIERT',
};

const ____mockdata: GesuchsperiodeDaten = {
  kinder_00_18: 3,
  jugendliche_erwachsene_19_25: 65,
  erwachsene_26_99: 3,
  aufschaltterminStopp: new Date().toISOString(),
  ausbKosten_SekII: 43,
  ausbKosten_Tertiaer: 20,
  wohnkosten_fam_1pers: 1,
  wohnkosten_fam_2pers: 2,
  wohnkosten_fam_3pers: 3,
  wohnkosten_fam_4pers: 5,
  wohnkosten_fam_5pluspers: 5,
  freibetrag_vermögen: 3,
  freibetrag_erwerbseinkommen: 2,
  einkommensfreibetrag: 2,
  bezeichnungDe: '1a',
  bezeichnungFr: '2b',
  einreichefristNormal: new Date().toISOString(),
  einreichefristReduziert: new Date().toISOString(),
  elternbeteiligungssatz: 23,
  wohnkosten_persoenlich_1pers: 20,
  wohnkosten_persoenlich_2pers: 40,
  wohnkosten_persoenlich_3pers: 0,
  wohnkosten_persoenlich_4pers: 0,
  wohnkosten_persoenlich_5pluspers: 0,
  f_Einkommensfreibetrag: 0,
  f_Vermoegensfreibetrag: 0,
  f_VermogenSatzAngerechnet: 0,
  fiskaljahr: 'awd',
  gesuchsjahr: 'afn',
  gesuchsperiodeStart: new Date().toISOString(),
  gesuchsperiodeStopp: new Date().toISOString(),
  integrationszulage: 0,
  limite_EkFreibetrag_Integrationszulag: 0,
  person_1: 0,
  personen_2: 0,
  personen_3: 0,
  personen_4: 0,
  personen_5: 0,
  personen_6: 0,
  personen_7: 0,
  proWeiterePerson: 0,
  stipLimite_Minimalstipendium: 0,
  aufschaltterminStart: new Date().toISOString(),
};

const ____mockGesuchperiode = (g: Gesuchsperiode): Gesuchsperiode => {
  const aufschaltdatum = g.aufschaltdatum
    ? fromBackendLocalDate(g.aufschaltdatum)
    : new Date();
  const einreichefrist = fromBackendLocalDate(g.einreichfrist);

  if (!aufschaltdatum || !einreichefrist) {
    return {
      ...g,
      bezeichnungDe: 'TBD',
      bezeichnungFr: 'TBD',
      status: 'ARCHIVIERT',
    };
  }

  const periode = {
    ...g,
    bezeichnungDe: `${
      aufschaltdatum.getMonth() + 1 > 6 ? 'Herbst' : 'Frühling'
    } ${aufschaltdatum.getFullYear()}`,
    bezeichnungFr: `${
      aufschaltdatum.getMonth() + 1 > 6 ? 'Automne' : 'Printemps'
    } ${aufschaltdatum.getFullYear()}`,
  };
  if (!periode.einreichfrist || einreichefrist > new Date()) {
    return { ...periode, status: 'AKTIV' };
  }
  if (new Date(periode.gueltigBis) < new Date()) {
    return { ...periode, status: 'ARCHIVIERT' };
  }
  return { ...periode, status: 'AKTIV' };
};
