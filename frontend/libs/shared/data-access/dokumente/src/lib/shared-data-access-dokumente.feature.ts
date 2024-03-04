import { createFeature, createReducer, on } from '@ngrx/store';

import { SharedEventGesuchDokumente } from '@dv/shared/event/gesuch-dokumente';
import { SharedModelError } from '@dv/shared/model/error';
import { Dokument } from '@dv/shared/model/gesuch';

import { SharedDataAccessDokumenteApiEvents } from './shared-data-access-dokumente.events';

export interface State {
  dokumentes: Dokument[];
  loading: boolean;
  error: SharedModelError | undefined;
}

const initialState: State = {
  dokumentes: [],
  loading: false,
  error: undefined,
};

export const sharedDataAccessDokumentesFeature = createFeature({
  name: 'dokumentes',
  reducer: createReducer(
    initialState,

    on(
      SharedEventGesuchDokumente.init,
      (state): State => ({
        ...state,
        loading: true,
        error: undefined,
      }),
    ),

    on(
      SharedDataAccessDokumenteApiEvents.dokumentesLoadedSuccess,
      (state, { dokumentes }): State => ({
        ...state,
        dokumentes,
        loading: false,
        error: undefined,
      }),
    ),
    on(
      SharedDataAccessDokumenteApiEvents.dokumentesLoadedFailure,
      // add other failure events here (if handled the same way)
      (state, { error }): State => ({
        ...state,
        dokumentes: [],
        loading: false,
        error,
      }),
    ),
  ),
});

export const {
  name, // feature name
  reducer,
  selectDokumentesState,
  selectDokumentes,
  selectLoading,
  selectError,
} = sharedDataAccessDokumentesFeature;
