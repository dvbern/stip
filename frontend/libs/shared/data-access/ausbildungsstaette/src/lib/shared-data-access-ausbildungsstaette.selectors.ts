import { createSelector } from '@ngrx/store';
import { AusbildungsLand } from '@dv/shared/model/ausbildung';
import { Ausbildungsstaette } from '@dv/shared/model/gesuch';

import { gesuchAppDataAccessAusbildungsstaettesFeature } from './shared-data-access-ausbildungsstaette.feature';

export const selectSharedDataAccessAusbildungsstaettesView = createSelector(
  gesuchAppDataAccessAusbildungsstaettesFeature.selectAusbildungsstaettesState,
  (state) => {
    const ausbildungsstaetteByLand: Record<
      AusbildungsLand,
      Ausbildungsstaette[]
    > = {
      AUSLAND: [],
      CH: [],
    };
    state.ausbildungsstaettes.forEach((ausbildungsstaette) => {
      const groups = {
        AUSLAND: ausbildungsstaette.ausbildungsgaenge?.filter(
          (g) => g.ausbildungsort === 'AUSLAND',
        ),
        CH: ausbildungsstaette.ausbildungsgaenge?.filter(
          (g) => g.ausbildungsort !== 'AUSLAND',
        ),
      };
      Object.entries(groups).forEach(([ausbildungsland, ausbildungsgaenge]) => {
        if (ausbildungsgaenge?.length) {
          ausbildungsstaetteByLand[ausbildungsland as AusbildungsLand].push({
            ...ausbildungsstaette,
            ausbildungsgaenge,
          });
        }
      });
    });
    return {
      ...state,
      ausbildungsstaettes: state.ausbildungsstaettes,
      ausbildungsstaetteByLand,
    };
  },
);
