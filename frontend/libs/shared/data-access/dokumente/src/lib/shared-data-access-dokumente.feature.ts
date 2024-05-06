import { createFeature, createReducer, on } from '@ngrx/store';

import { SharedEventGesuchDokumente } from '@dv/shared/event/gesuch-dokumente';
import { SharedModelError } from '@dv/shared/model/error';
import { DokumentTyp, GesuchDokument } from '@dv/shared/model/gesuch';

import { SharedDataAccessDokumenteApiEvents } from './shared-data-access-dokumente.events';

export interface State {
  dokumentes: GesuchDokument[];
  requiredDocumentTypes: DokumentTyp[];
  loading: boolean;
  error: SharedModelError | undefined;
}

const initialState: State = {
  dokumentes: [],
  requiredDocumentTypes: [],
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
      SharedDataAccessDokumenteApiEvents.getRequiredDocumentTypeSuccess,
      (state, { requiredDocumentTypes }): State => ({
        ...state,
        requiredDocumentTypes,
        loading: false,
        error: undefined,
      }),
    ),
    on(
      SharedDataAccessDokumenteApiEvents.dokumentesLoadedFailure,
      (state, { error }): State => ({
        ...state,
        dokumentes: [],
        loading: false,
        error,
      }),
    ),
    on(
      SharedDataAccessDokumenteApiEvents.getRequiredDocumentTypeFailure,
      (state, { error }): State => ({
        ...state,
        requiredDocumentTypes: [],
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
