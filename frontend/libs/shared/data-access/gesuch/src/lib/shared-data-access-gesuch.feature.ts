import { createFeature, createReducer, on } from '@ngrx/store';

import { SharedEventGesuchFormAbschluss } from '@dv/shared/event/gesuch-form-abschluss';
import { SharedEventGesuchFormEinnahmenkosten } from '@dv/shared/event/gesuch-form-einnahmenkosten';
import { SharedEventGesuchFormEltern } from '@dv/shared/event/gesuch-form-eltern';
import { SharedEventGesuchFormElternSteuerdaten } from '@dv/shared/event/gesuch-form-eltern-steuerdaten';
import { SharedEventGesuchFormFamiliensituation } from '@dv/shared/event/gesuch-form-familiensituation';
import { SharedEventGesuchFormGeschwister } from '@dv/shared/event/gesuch-form-geschwister';
import { SharedEventGesuchFormKinder } from '@dv/shared/event/gesuch-form-kinder';
import { SharedEventGesuchFormLebenslauf } from '@dv/shared/event/gesuch-form-lebenslauf';
import { SharedEventGesuchFormPerson } from '@dv/shared/event/gesuch-form-person';
import { SharedModelError } from '@dv/shared/model/error';
import {
  FallDashboardItem,
  GesuchFormularType,
  GesuchUrlType,
  SharedModelGesuch,
  SteuerdatenTyp,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  failure,
  initial,
  success,
} from '@dv/shared/util/remote-data';

import { SharedDataAccessGesuchEvents } from './shared-data-access-gesuch.events';

export interface State {
  gesuch: SharedModelGesuch | null;
  gesuchFormular: GesuchFormularType | null;
  isEditingAenderung: boolean | null;
  trancheTyp: GesuchUrlType | null;
  gesuchs: SharedModelGesuch[];
  gsDashboard: FallDashboardItem[];
  cache: {
    gesuch: SharedModelGesuch | null;
    gesuchId: string | null;
    gesuchFormular: GesuchFormularType | null;
  };
  steuerdatenTabs: CachedRemoteData<SteuerdatenTyp[]>;
  lastUpdate: string | null;
  loading: boolean;
  error: SharedModelError | undefined;
}

const initialState: State = {
  gesuch: null,
  gesuchFormular: null,
  isEditingAenderung: null,
  trancheTyp: null,
  gesuchs: [],
  gsDashboard: [],
  cache: {
    gesuch: null,
    gesuchId: null,
    gesuchFormular: null,
  },
  steuerdatenTabs: initial(),
  lastUpdate: null,
  loading: false,
  error: undefined,
};

export const sharedDataAccessGesuchsFeature = createFeature({
  name: 'gesuchs',
  reducer: createReducer(
    initialState,

    on(SharedDataAccessGesuchEvents.reset, (): State => initialState),

    on(
      SharedDataAccessGesuchEvents.init,
      (state): State => ({
        ...state,
        gesuchs: [],
      }),
    ),

    on(
      SharedDataAccessGesuchEvents.init,
      SharedDataAccessGesuchEvents.loadGesuch,
      SharedEventGesuchFormPerson.init,
      SharedEventGesuchFormFamiliensituation.init,
      SharedEventGesuchFormEltern.init,
      SharedEventGesuchFormElternSteuerdaten.init,
      SharedEventGesuchFormGeschwister.init,
      SharedEventGesuchFormKinder.init,
      SharedEventGesuchFormLebenslauf.init,
      SharedEventGesuchFormEinnahmenkosten.init,
      SharedEventGesuchFormAbschluss.init,
      (state): State => ({
        ...state,
        gesuch: null,
        gesuchFormular: null,
        steuerdatenTabs: cachedPending(state.steuerdatenTabs),
        loading: true,
      }),
    ),

    on(
      SharedDataAccessGesuchEvents.deleteGesuch,
      SharedDataAccessGesuchEvents.loadAll,
      SharedDataAccessGesuchEvents.loadAllDebounced,
      SharedEventGesuchFormPerson.saveTriggered,
      SharedEventGesuchFormFamiliensituation.saveTriggered,
      SharedEventGesuchFormEltern.saveTriggered,
      SharedEventGesuchFormEltern.saveSubformTriggered,
      SharedEventGesuchFormGeschwister.saveTriggered,
      SharedEventGesuchFormKinder.saveTriggered,
      SharedEventGesuchFormKinder.saveSubformTriggered,
      SharedEventGesuchFormLebenslauf.saveTriggered,
      SharedEventGesuchFormLebenslauf.saveSubformTriggered,
      SharedEventGesuchFormEinnahmenkosten.saveTriggered,
      SharedEventGesuchFormAbschluss.saveTriggered,
      (state): State => ({
        ...state,
        loading: true,
        steuerdatenTabs: cachedPending(state.steuerdatenTabs),
      }),
    ),

    on(
      SharedEventGesuchFormGeschwister.saveSubformTriggered,
      (state): State => ({
        ...state,
        loading: true,
        gesuchFormular: state.gesuchFormular
          ? {
              ...state.gesuchFormular,
              geschwisters: [],
            }
          : null,
      }),
    ),

    on(
      SharedDataAccessGesuchEvents.gesuchsLoadedSuccess,
      (state, { gesuchs }): State => ({
        ...state,
        gesuchs,
        loading: false,
        error: undefined,
      }),
    ),

    on(
      SharedDataAccessGesuchEvents.gsDashboardLoadedSuccess,
      (state, { gsDashboard }): State => ({
        ...state,
        gsDashboard,
        loading: false,
        error: undefined,
      }),
    ),

    on(
      SharedDataAccessGesuchEvents.gesuchLoadedSuccess,
      (state, { gesuch, typ }): State => {
        const gesuchFormular = getGesuchFormular(gesuch);
        return {
          ...state,
          gesuch,
          gesuchFormular: gesuchFormular,
          isEditingAenderung: typ === 'AENDERUNG',
          trancheTyp: typ,
          steuerdatenTabs: success(gesuchFormular?.steuerdatenTabs ?? []),
          cache: {
            gesuch: gesuch ?? state.cache.gesuch,
            gesuchId: gesuch.id ?? state.cache.gesuchId,
            gesuchFormular: gesuchFormular ?? state.cache.gesuchFormular,
          },
          loading: false,
          error: undefined,
        };
      },
    ),

    on(
      SharedDataAccessGesuchEvents.gesuchSetReturned,
      (state, { gesuch }): State => {
        const gesuchFormular = getGesuchFormular(gesuch);
        return {
          ...state,
          gesuch,
          gesuchFormular: gesuchFormular,
          steuerdatenTabs: success(gesuchFormular?.steuerdatenTabs ?? []),
          cache: {
            gesuch: gesuch ?? state.cache.gesuch,
            gesuchId: gesuch.id ?? state.cache.gesuchId,
            gesuchFormular: gesuchFormular ?? state.cache.gesuchFormular,
          },
          loading: false,
          error: undefined,
        };
      },
    ),

    on(
      SharedDataAccessGesuchEvents.gesuchUpdatedSuccess,
      SharedDataAccessGesuchEvents.deleteGesuchSuccess,
      (state): State => ({
        ...state,
        lastUpdate: new Date().toISOString(),
        loading: false,
        error: undefined,
      }),
    ),

    on(
      SharedDataAccessGesuchEvents.gesuchUpdatedSubformSuccess,
      (state): State => ({
        ...state,
        lastUpdate: new Date().toISOString(),
        error: undefined,
      }),
    ),

    on(
      SharedDataAccessGesuchEvents.gesuchsLoadedFailure,
      SharedDataAccessGesuchEvents.gesuchLoadedFailure,
      SharedDataAccessGesuchEvents.gesuchCreatedFailure,
      SharedDataAccessGesuchEvents.gesuchUpdatedFailure,
      SharedDataAccessGesuchEvents.gesuchUpdatedSubformFailure,
      SharedDataAccessGesuchEvents.deleteGesuchFailure,
      // add other failure actions here (if handled the same way)
      (state, { error }): State => ({
        ...state,
        loading: false,
        steuerdatenTabs: failure(error),
        error,
      }),
    ),
  ),
});

export const {
  name, // feature name
  reducer,
  selectGesuchsState,
  selectLastUpdate,
  selectGesuch,
  selectGesuchs,
  selectGesuchFormular,
  selectLoading,
  selectError,
  selectCache,
} = sharedDataAccessGesuchsFeature;

const getGesuchFormular = (
  gesuch: SharedModelGesuch,
): GesuchFormularType | null => {
  return gesuch.gesuchTrancheToWorkWith.gesuchFormular ?? null;
};
