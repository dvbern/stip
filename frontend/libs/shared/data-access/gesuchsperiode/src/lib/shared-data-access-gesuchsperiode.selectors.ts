import { createSelector } from '@ngrx/store';
import {
  differenceInDays,
  endOfDay,
  format,
  getMonth,
  isAfter,
  isWithinInterval,
} from 'date-fns';

import {
  Gesuchsperiode,
  GesuchsperiodeSemester,
} from '@dv/shared/model/gesuch';

import { sharedDataAccessGesuchsperiodesFeature } from './shared-data-access-gesuchsperiode.feature';

export const selectSharedDataAccessGesuchsperiodesView = createSelector(
  sharedDataAccessGesuchsperiodesFeature.selectGesuchsperiodesState,
  (state) => ({
    ...state,
    gesuchsperiodes: state.gesuchsperiodes.map(prepareGesuchsperiode),
  }),
);

type GesuchsperiodeDateProperties = Pick<
  Gesuchsperiode,
  | 'aufschaltterminStart'
  | 'aufschaltterminStopp'
  | 'einreichefristNormal'
  | 'einreichefristReduziert'
  | 'gesuchsperiodeStart'
  | 'gesuchsperiodeStopp'
>;

export const prepareGesuchsperiode = <T extends GesuchsperiodeDateProperties>(
  periode: T,
) => {
  const reduzierterBeitrag = isAfter(
    new Date(),
    endOfDay(new Date(periode.einreichefristNormal)),
  );
  const einreichefristDays = differenceInDays(
    endOfDay(
      new Date(
        reduzierterBeitrag
          ? periode.einreichefristReduziert
          : periode.einreichefristNormal,
      ),
    ),
    new Date(),
  );

  return {
    ...periode,
    semester:
      getMonth(Date.parse(periode.gesuchsperiodeStart)) === 6
        ? GesuchsperiodeSemester.HERBST
        : GesuchsperiodeSemester.FRUEHLING,
    yearsLabel: [
      format(Date.parse(periode.gesuchsperiodeStart), 'yy'),
      format(Date.parse(periode.gesuchsperiodeStopp), 'yy'),
    ].join('/'),
    reduzierterBeitrag,
    einreichefristAbgelaufen: isAfter(
      new Date(),
      endOfDay(new Date(periode.einreichefristReduziert)),
    ),
    erfassbar: isWithinInterval(new Date(), {
      start: new Date(periode.aufschaltterminStart),
      end: endOfDay(new Date(periode.aufschaltterminStopp)),
    }),
    einreichefristDays: einreichefristDays >= 0 ? einreichefristDays : null,
  };
};
