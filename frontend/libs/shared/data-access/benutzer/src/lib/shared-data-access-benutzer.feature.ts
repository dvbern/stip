import { createFeature, createReducer, on } from '@ngrx/store';

import { RolesMap } from '@dv/shared/model/benutzer';
import { Benutzer } from '@dv/shared/model/gesuch';
import {
  RemoteData,
  failure,
  initial,
  pending,
  success,
} from '@dv/shared/util/remote-data';

import { SharedDataAccessBenutzerApiEvents } from './shared-data-access-benutzer.events';

export interface State {
  rolesMap: RolesMap;
  currentBenutzerRd: RemoteData<Benutzer>;
  lastFetchTs: number | null;
}

const initialState: State = {
  rolesMap: {} satisfies RolesMap,
  currentBenutzerRd: initial(),
  lastFetchTs: null,
};

export const sharedDataAccessBenutzersFeature = createFeature({
  name: 'benutzers',
  reducer: createReducer(
    initialState,

    on(
      SharedDataAccessBenutzerApiEvents.setCurrentBenutzerPending,
      (state): State => ({
        ...state,
        currentBenutzerRd: pending(),
      }),
    ),

    on(
      SharedDataAccessBenutzerApiEvents.currentBenutzerLoadedSuccess,
      (state, { benutzer }): State => ({
        ...state,
        currentBenutzerRd: success(benutzer),
        lastFetchTs: new Date().getTime(),
      }),
    ),

    on(
      SharedDataAccessBenutzerApiEvents.currentBenutzerLoadedFailure,
      // add other failure events here (if handled the same way)
      (state, { error }): State => ({
        ...state,
        currentBenutzerRd: failure(error),
        lastFetchTs: null,
      }),
    ),

    on(
      SharedDataAccessBenutzerApiEvents.setRolesMap,
      (state, { rolesMap }): State => ({
        ...state,
        rolesMap,
      }),
    ),
  ),
});

export const {
  name, // feature name
  reducer,
  selectBenutzersState,
  selectCurrentBenutzerRd,
  selectLastFetchTs,
} = sharedDataAccessBenutzersFeature;
