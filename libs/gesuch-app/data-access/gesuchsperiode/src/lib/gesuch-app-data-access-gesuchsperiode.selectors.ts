import { GesuchsperiodeSemester } from '@dv/shared/model/gesuch';
import { createSelector } from '@ngrx/store';
import { format, getMonth } from 'date-fns';
import { gesuchAppDataAccessGesuchsperiodesFeature } from './gesuch-app-data-access-gesuchsperiode.feature';

export const selectGesuchAppDataAccessGesuchsperiodesView = createSelector(
  gesuchAppDataAccessGesuchsperiodesFeature.selectGesuchsperiodesState,
  (state) => ({
    ...state,
    gesuchsperiodes: state.gesuchsperiodes.map((p) => ({
      ...p,
      semester:
        getMonth(Date.parse(p.gueltigAb)) === 7
          ? GesuchsperiodeSemester.HERBST
          : GesuchsperiodeSemester.FRUEHLING,
      yearsLabel: [
        format(Date.parse(p.gueltigAb), 'yy'),
        format(Date.parse(p.gueltigBis), 'yy'),
      ].join('/'),
    })),
  })
);
