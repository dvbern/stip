import { createFeature, createReducer, on } from '@ngrx/store';

import { SharedEventGesuchFormAbschluss } from '@dv/shared/event/gesuch-form-abschluss';
import { SharedModelError } from '@dv/shared/model/error';

import { GesuchAppDataAccessAbschlussApiEvents } from './gesuch-app-data-access-abschluss.events';

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

export const gesuchAppDataAccessAbschlussFeature = createFeature({
  name: 'abschluss',
  reducer: createReducer(
    initialState,
    on(
      GesuchAppDataAccessAbschlussApiEvents.gesuchCheckSuccess,
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
      GesuchAppDataAccessAbschlussApiEvents.gesuchCheckFailure,
      (state, { error }): State => ({
        ...state,
        checkResult: { success: false, error },
        loading: true,
        error: undefined,
      }),
    ),
    on(
      GesuchAppDataAccessAbschlussApiEvents.gesuchAbschliessen,
      (state): State => ({
        ...state,
        checkResult: undefined,
        loading: true,
        error: undefined,
      }),
    ),
    on(
      SharedEventGesuchFormAbschluss.init,
      GesuchAppDataAccessAbschlussApiEvents.abschlussSuccess,
      (state): State => ({
        ...state,
        checkResult: undefined,
        loading: false,
        error: undefined,
      }),
    ),
    on(
      GesuchAppDataAccessAbschlussApiEvents.abschlussFailure,
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
} = gesuchAppDataAccessAbschlussFeature;

const hasNoValidationErrors = (error: SharedModelError | undefined): boolean =>
  !error ||
  (error.type === 'validationError' && error.validationErrors.length === 0);
