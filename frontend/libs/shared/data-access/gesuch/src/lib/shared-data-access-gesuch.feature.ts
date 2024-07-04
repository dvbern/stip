import { createFeature, createReducer, on } from '@ngrx/store';

import { SharedEventGesuchFormAbschluss } from '@dv/shared/event/gesuch-form-abschluss';
import { SharedEventGesuchFormEducation } from '@dv/shared/event/gesuch-form-education';
import { SharedEventGesuchFormEinnahmenkosten } from '@dv/shared/event/gesuch-form-einnahmenkosten';
import { SharedEventGesuchFormEltern } from '@dv/shared/event/gesuch-form-eltern';
import { SharedEventGesuchFormElternSteuerdaten } from '@dv/shared/event/gesuch-form-eltern-steuerdaten';
import { SharedEventGesuchFormFamiliensituation } from '@dv/shared/event/gesuch-form-familiensituation';
import { SharedEventGesuchFormGeschwister } from '@dv/shared/event/gesuch-form-geschwister';
import { SharedEventGesuchFormKinder } from '@dv/shared/event/gesuch-form-kinder';
import { SharedEventGesuchFormLebenslauf } from '@dv/shared/event/gesuch-form-lebenslauf';
import { SharedEventGesuchFormPerson } from '@dv/shared/event/gesuch-form-person';
import { SharedModelError, ValidationWarning } from '@dv/shared/model/error';
import {
  SharedModelGesuch,
  SharedModelGesuchFormular,
  SteuerdatenTyp,
  ValidationError,
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
  validations: {
    errors: ValidationError[];
    warnings?: ValidationWarning[];
    hasDocuments: boolean | null;
  } | null;
  gesuch: SharedModelGesuch | null;
  gesuchFormular: SharedModelGesuchFormular | null;
  gesuchs: SharedModelGesuch[];
  cache: {
    gesuchId: string | null;
    gesuchFormular: SharedModelGesuchFormular | null;
  };
  steuerdatenTabs: CachedRemoteData<SteuerdatenTyp[]>;
  lastUpdate: string | null;
  loading: boolean;
  error: SharedModelError | undefined;
}

const initialState: State = {
  validations: null,
  gesuch: null,
  gesuchFormular: null,
  gesuchs: [],
  cache: {
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

    on(
      SharedDataAccessGesuchEvents.init,
      (state): State => ({
        ...state,
        gesuchs: [],
        // Allow cached gesuchFormular to be used if gesuchFormular is null
        // (e.g. while navigating between steps and the navbar shouldn't be updated)
        cache: {
          ...state.cache,
          gesuchFormular: null,
        },
      }),
    ),

    on(
      SharedDataAccessGesuchEvents.init,
      SharedEventGesuchFormPerson.init,
      SharedEventGesuchFormEducation.init,
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
      SharedDataAccessGesuchEvents.removeTriggered,
      SharedDataAccessGesuchEvents.loadAll,
      SharedDataAccessGesuchEvents.loadAllDebounced,
      SharedEventGesuchFormPerson.saveTriggered,
      SharedEventGesuchFormEducation.saveTriggered,
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
      SharedDataAccessGesuchEvents.gesuchLoadedSuccess,
      (state, { gesuch }): State => {
        const gesuchFormular = getGesuchFormular(gesuch);
        return {
          ...state,
          gesuch,
          gesuchFormular: gesuchFormular,
          steuerdatenTabs: success(
            gesuch.gesuchTrancheToWorkWith?.gesuchFormular?.steuerdatenTabs ??
              [],
          ),
          cache: {
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
      SharedDataAccessGesuchEvents.gesuchRemovedSuccess,
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
      SharedDataAccessGesuchEvents.gesuchValidationSuccess,
      (state, { error }): State => ({
        ...state,
        validations:
          error.type === 'validationError'
            ? {
                errors: error.validationErrors,
                warnings: error.validationWarnings,
                hasDocuments: error.hasDocuments,
              }
            : null,
        loading: false,
        error: error.type === 'validationError' ? undefined : error,
      }),
    ),

    on(
      SharedDataAccessGesuchEvents.gesuchsLoadedFailure,
      SharedDataAccessGesuchEvents.gesuchLoadedFailure,
      SharedDataAccessGesuchEvents.gesuchCreatedFailure,
      SharedDataAccessGesuchEvents.gesuchUpdatedFailure,
      SharedDataAccessGesuchEvents.gesuchUpdatedSubformFailure,
      SharedDataAccessGesuchEvents.gesuchRemovedFailure,
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
  selectValidations,
} = sharedDataAccessGesuchsFeature;

const getGesuchFormular = (
  gesuch: SharedModelGesuch,
): SharedModelGesuchFormular | null => {
  return gesuch.gesuchTrancheToWorkWith.gesuchFormular ?? null;
};
