import { createFeature, createReducer, on } from '@ngrx/store';

import { SharedEventGesuchFormAbschluss } from '@dv/shared/event/gesuch-form-abschluss';
import { SharedModelError } from '@dv/shared/model/error';

import { SharedDataAccessAbschlussApiEvents } from './gesuch-app-data-access-abschluss.events';

export interface State {
  checkResult:
    | { success: boolean; error: SharedModelError | undefined }
    | undefined;
  loading: boolean;
  error: SharedModelError | undefined;
}

const initialState: State = {
  checkResult: { success: false, error: undefined },
  loading: false,
  error: undefined,
};

export const sharedDataAccessAbschlussFeature = createFeature({
  name: 'abschluss',
  reducer: createReducer(
    initialState,
    on(
      SharedDataAccessAbschlussApiEvents.check,
      (state): State => ({
        ...state,
        checkResult: undefined,
        loading: true,
        error: undefined,
      }),
    ),
    on(
      SharedDataAccessAbschlussApiEvents.gesuchCheckSuccess,
      (state, { error }): State => ({
        ...state,
        checkResult: {
          success: hasNoValidationErrors(error),
          error,
        },
        loading: false,
        error: error.type === 'validationError' ? undefined : error,
      }),
    ),
    on(
      SharedDataAccessAbschlussApiEvents.gesuchCheckFailure,
      (state, { error }): State => ({
        ...state,
        checkResult: { success: false, error },
        loading: false,
        error: undefined,
      }),
    ),
    on(
      SharedDataAccessAbschlussApiEvents.gesuchAbschliessen,
      (state): State => ({
        ...state,
        checkResult: undefined,
        loading: true,
        error: undefined,
      }),
    ),
    on(
      SharedEventGesuchFormAbschluss.init,
      SharedDataAccessAbschlussApiEvents.abschlussSuccess,
      (state): State => ({
        ...state,
        checkResult: undefined,
        loading: false,
        error: undefined,
      }),
    ),
    on(
      SharedDataAccessAbschlussApiEvents.abschlussFailure,
      (state, { error }): State => ({
        ...state,
        checkResult: undefined,
        loading: false,
        error,
      }),
    ),
  ),
});

export const {
  name, // feature name
  reducer,
  selectAbschlussState,
  selectLoading,
  selectError,
} = sharedDataAccessAbschlussFeature;

const hasNoValidationErrors = (error: SharedModelError | undefined): boolean =>
  !error ||
  (error.type === 'validationError' && error.validationErrors.length === 0);
