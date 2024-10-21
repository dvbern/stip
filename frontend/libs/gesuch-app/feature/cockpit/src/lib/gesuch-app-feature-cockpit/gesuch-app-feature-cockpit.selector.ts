import { createSelector } from '@ngrx/store';

import { selectVersion } from '@dv/shared/data-access/config';
import { sharedDataAccessGesuchsFeature } from '@dv/shared/data-access/gesuch';
import { selectSharedDataAccessGesuchsperiodesView } from '@dv/shared/data-access/gesuchsperiode';

export const selectGesuchAppFeatureCockpitView = createSelector(
  selectSharedDataAccessGesuchsperiodesView,
  sharedDataAccessGesuchsFeature.selectGsDashboard,
  sharedDataAccessGesuchsFeature.selectLoading,
  selectVersion,
  (gesuchsPerioden, gesuche, gesucheLoading, version) => {
    const isPeriodeErfassbarMitGesuch = gesuchsPerioden.gesuchsperiodes
      .filter((p) => p.erfassbar)
      .some((p) =>
        gesuche.some((gesuch) => p.id === gesuch.gesuchsperiode?.id),
      );
    const gesuchsperiodes = gesuchsPerioden.gesuchsperiodes.map((p) => ({
      ...p,
      gesuchLoading: gesucheLoading,
      gesuch: gesuche.find((gesuch) => p.id === gesuch.gesuchsperiode?.id),
    }));

    return {
      ...gesuchsPerioden,
      gesuchsperiodes: isPeriodeErfassbarMitGesuch
        ? // Do not show the herbst / fruehling periode if there is a gesuch for either of them
          gesuchsperiodes.filter((p) => p.gesuch)
        : gesuchsperiodes,
      version,
    };
  },
);
