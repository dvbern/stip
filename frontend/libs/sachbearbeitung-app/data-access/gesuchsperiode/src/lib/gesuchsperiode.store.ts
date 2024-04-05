import { Injectable, computed, inject } from '@angular/core';
import {
  patchState,
  signalStore,
  withComputed,
  withMethods,
  withState,
} from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import {
  BehaviorSubject,
  filter,
  map,
  merge,
  pipe,
  switchMap,
  take,
  tap,
} from 'rxjs';

import {
  Gesuchsjahr,
  GesuchsjahrService,
  Gesuchsperiode,
  GesuchsperiodeService,
  GesuchsperiodeWithDaten,
} from '@dv/shared/model/gesuch';
import {
  RemoteData,
  handleApiResponse,
  initial,
  pending,
} from '@dv/shared/util/remote-data';
import {
  formatBackendLocalDate,
  fromBackendLocalDate,
} from '@dv/shared/util/validator-date';
import { isDefined } from '@dv/shared/util-fn/type-guards';

type GesuchsperiodeState = {
  gesuchsjahre: RemoteData<Gesuchsjahr[]>;
  gesuchsperioden: RemoteData<Gesuchsperiode[]>;
  currentGesuchsperiode: RemoteData<GesuchsperiodeWithDaten>;
  currentGesuchsJahr: RemoteData<Gesuchsjahr>;
};
@Injectable({ providedIn: 'root' })
export class MockGesuchsperiodenService {
  private gesuchsperiode$ = new BehaviorSubject<
    GesuchsperiodeWithDaten | undefined
  >(____mockdata);

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  getGesuchsperiode$(_param: { gesuchsperiodeId: string }) {
    return this.gesuchsperiode$.pipe(take(1), filter(isDefined));
  }

  updateGesuchsperiode$(data: {
    gesuchsperiodeId: string;
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    gesuchsperiodeUpdate: any;
  }) {
    return this.gesuchsperiode$.pipe(
      take(1),
      filter(isDefined),
      map(
        (p) =>
          ({ ...p, ...data.gesuchsperiodeUpdate }) as GesuchsperiodeWithDaten,
      ),
    );
  }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  createGesuchsperiode$(data: { gesuchsperiodeCreate: any }) {
    return this.gesuchsperiode$.pipe(
      take(1),
      map(
        (p) =>
          ({
            ...p,
            ...data.gesuchsperiodeCreate,
            id: 'newId',
          }) as GesuchsperiodeWithDaten,
      ),
    );
  }
}

const initialState: GesuchsperiodeState = {
  gesuchsjahre: initial(),
  gesuchsperioden: initial(),
  currentGesuchsJahr: initial(),
  currentGesuchsperiode: initial(),
};
export const GesuchsperiodeStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods(
    (
      store,
      gesuchsperiodeService = inject(GesuchsperiodeService),
      gesuchsjahrService = inject(GesuchsjahrService),
      mockGesuchsperiodeService = inject(MockGesuchsperiodenService),
    ) => {
      const loadOverview$ = rxMethod<void>(
        pipe(
          tap(() => {
            patchState(store, {
              gesuchsperioden: pending(),
              gesuchsjahre: pending(),
            });
          }),
          switchMap(() =>
            merge(
              gesuchsperiodeService
                .getGesuchsperioden$()
                .pipe(
                  handleApiResponse((gesuchsperioden) =>
                    patchState(store, { gesuchsperioden }),
                  ),
                ),
              gesuchsjahrService
                .getGesuchsjahre$()
                .pipe(
                  handleApiResponse((gesuchsjahre) =>
                    patchState(store, { gesuchsjahre }),
                  ),
                ),
            ),
          ),
        ),
      );
      const loadGesuchsperiode$ = rxMethod<string>(
        pipe(
          switchMap((id) =>
            mockGesuchsperiodeService.getGesuchsperiode$({
              gesuchsperiodeId: id,
            }),
          ),
          handleApiResponse((currentGesuchsperiode) =>
            patchState(store, { currentGesuchsperiode }),
          ),
        ),
      );
      const saveGesuchsperiode$ = rxMethod<{
        gesuchsperiodeId: string;
        gesuchsperiodenDaten: Omit<
          GesuchsperiodeWithDaten,
          'id' | 'gueltigAb' | 'gueltigBis' | 'status'
        >;
      }>(
        pipe(
          switchMap(({ gesuchsperiodeId, gesuchsperiodenDaten }) =>
            gesuchsperiodeId
              ? mockGesuchsperiodeService.updateGesuchsperiode$({
                  gesuchsperiodeId,
                  gesuchsperiodeUpdate: gesuchsperiodenDaten,
                })
              : mockGesuchsperiodeService.createGesuchsperiode$({
                  gesuchsperiodeCreate: gesuchsperiodenDaten,
                }),
          ),
          handleApiResponse((currentGesuchsperiode) => {
            patchState(store, {
              currentGesuchsperiode,
            });
          }),
        ),
      );
      const loadGesuchsjahr$ = rxMethod<string>(
        pipe(
          switchMap((gesuchsjahrId) =>
            gesuchsjahrService.getGesuchsjahr$({ gesuchsjahrId }),
          ),
          handleApiResponse((currentGesuchsJahr) => {
            patchState(store, {
              currentGesuchsJahr,
            });
          }),
        ),
      );
      const saveGesuchsjahr$ = rxMethod<{
        gesuchsjahrId?: string;
        gesuchsjahrDaten: Omit<Gesuchsjahr, 'id' | 'gueltigkeitStatus'>;
      }>(
        pipe(
          switchMap(({ gesuchsjahrId, gesuchsjahrDaten }) =>
            gesuchsjahrId
              ? gesuchsjahrService.updateGesuchsjahr$({
                  gesuchsjahrId,
                  gesuchsjahrUpdate: gesuchsjahrDaten,
                })
              : gesuchsjahrService.createGesuchsjahr$({
                  // eslint-disable-next-line @typescript-eslint/no-explicit-any
                  gesuchsjahrCreate: gesuchsjahrDaten as any,
                }),
          ),
          handleApiResponse((currentGesuchsJahr) => {
            patchState(store, {
              currentGesuchsJahr,
            });
          }),
        ),
      );
      return {
        loadOverview$,
        loadGesuchsperiode$,
        saveGesuchsperiode$,
        loadGesuchsjahr$,
        saveGesuchsjahr$,
      };
    },
  ),
  withComputed((store) => ({
    gesuchperiodenListView: computed(() => {
      return store.gesuchsperioden.data?.()?.map((g) => ({
        ...g,
        gesuchsperiode:
          formatBackendLocalDate(g.gueltigAb, 'de') +
          ' - ' +
          formatBackendLocalDate(g.gueltigBis, 'de'),
        gesuchsjahr: fromBackendLocalDate(g.aufschaltdatum)?.getFullYear(),
      }));
    }),
    gesuchsjahreListView: computed(() => {
      return store.gesuchsjahre.data?.()?.map((g) => ({
        ...g,
        ausbildungsjahr: `${g.technischesJahr}/${(g.technischesJahr + 1)
          .toString()
          .slice(-2)}`,
      }));
    }),
  })),
);

const ____mockdata: GesuchsperiodeWithDaten = {
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
  freibetrag_vermoegen: 3,
  freibetrag_erwerbseinkommen: 2,
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
  einkommensfreibetrag: 0,
  vermoegensfreibetrag: 0,
  vermogenSatzAngerechnet: 0,
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
  gueltigAb: new Date().toISOString(),
  gueltigBis: new Date().toISOString(),
  id: 'a',
  status: 'PUBLIZIERT',
  aufschaltdatum: new Date().toISOString(),
  einreichfrist: new Date().toISOString(),
};
