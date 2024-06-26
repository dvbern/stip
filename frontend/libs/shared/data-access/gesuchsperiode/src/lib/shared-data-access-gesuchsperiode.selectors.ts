import { createSelector } from '@ngrx/store';
import { format, getMonth } from 'date-fns';

import { GesuchsperiodeSemester } from '@dv/shared/model/gesuch';

import { sharedDataAccessGesuchsperiodesFeature } from './shared-data-access-gesuchsperiode.feature';

export const selectSharedDataAccessGesuchsperiodesView = createSelector(
  sharedDataAccessGesuchsperiodesFeature.selectGesuchsperiodesState,
  (state) => ({
    ...state,
    gesuchsperiodes: state.gesuchsperiodes.map((p) => ({
      ...p,
      semester:
        getMonth(Date.parse(p.gesuchsperiodeStart)) === 7
          ? GesuchsperiodeSemester.HERBST
          : GesuchsperiodeSemester.FRUEHLING,
      yearsLabel: [
        format(Date.parse(p.gesuchsperiodeStart), 'yy'),
        format(Date.parse(p.gesuchsperiodeStopp), 'yy'),
      ].join('/'),
    })),
  }),
);
