import { createFeature, createReducer, on } from '@ngrx/store';

import { SharedEventGesuchFormAbschluss } from '@dv/shared/event/gesuch-form-abschluss';
import { SharedEventGesuchFormEducation } from '@dv/shared/event/gesuch-form-education';
import { SharedEventGesuchFormEinnahmenkosten } from '@dv/shared/event/gesuch-form-einnahmenkosten';
import { SharedEventGesuchFormEltern } from '@dv/shared/event/gesuch-form-eltern';
import { SharedEventGesuchFormFamiliensituation } from '@dv/shared/event/gesuch-form-familiensituation';
import { SharedEventGesuchFormGeschwister } from '@dv/shared/event/gesuch-form-geschwister';
import { SharedEventGesuchFormKinder } from '@dv/shared/event/gesuch-form-kinder';
import { SharedEventGesuchFormLebenslauf } from '@dv/shared/event/gesuch-form-lebenslauf';
import { SharedEventGesuchFormPerson } from '@dv/shared/event/gesuch-form-person';
import { SharedModelError, ValidationWarning } from '@dv/shared/model/error';
import {
  SharedModelGesuch,
  SharedModelGesuchFormular,
  ValidationError,
} from '@dv/shared/model/gesuch';

import { SharedDataAccessGesuchEvents } from './shared-data-access-gesuch.events';

export interface State {
  validations: {
    errors: ValidationError[];
    warnings?: ValidationWarning[];
  } | null;
  gesuch: SharedModelGesuch | null;
  gesuchFormular: SharedModelGesuchFormular | null;
  gesuchs: SharedModelGesuch[];
  cache: {
    gesuchFormular: SharedModelGesuchFormular | null;
  };
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
    gesuchFormular: null,
  },
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
      SharedEventGesuchFormGeschwister.init,
      SharedEventGesuchFormKinder.init,
      SharedEventGesuchFormLebenslauf.init,
      SharedEventGesuchFormEinnahmenkosten.init,
      SharedEventGesuchFormAbschluss.init,
      (state): State => ({
        ...state,
        gesuch: null,
        gesuchFormular: null,
        loading: true,
      }),
    ),

    on(
      SharedDataAccessGesuchEvents.removeTriggered,
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
          cache: {
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
        error,
      }),
    ),
  ),
});

export const {
  name, // feature name
  reducer,
  selectGesuchsState,
  selectGesuch,
  selectGesuchs,
  selectLoading,
  selectError,
} = sharedDataAccessGesuchsFeature;

const getGesuchFormular = (
  gesuch: SharedModelGesuch,
): SharedModelGesuchFormular | null => {
  return gesuch.gesuchTrancheToWorkWith.gesuchFormular ?? null;
};
