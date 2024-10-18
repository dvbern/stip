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
    const periodeErfassbarMitGesuch = gesuchsPerioden.gesuchsperiodes
      .filter((p) => p.erfassbar)
      .find((p) =>
        gesuche.find((gesuch) => p.id === gesuch.gesuchsperiode?.id),
      );

    if (!periodeErfassbarMitGesuch) {
      return {
        ...gesuchsPerioden,
        gesuchsperiodes: gesuchsPerioden.gesuchsperiodes.map((p) => ({
          ...p,
          gesuchLoading: gesucheLoading,
          gesuch: gesuche.find((gesuch) => p.id === gesuch.gesuchsperiode?.id),
        })),
        version,
      };
    }

    return {
      ...gesuchsPerioden,
      gesuchsperiodes: gesuchsPerioden.gesuchsperiodes
        .map((p) => ({
          ...p,
          gesuchLoading: gesucheLoading,
          gesuch: gesuche.find((gesuch) => p.id === gesuch.gesuchsperiode?.id),
        }))
        // this filter is the key difference. do not show the herbst / fruehling periode if there is a gesuch
        // for either of them. Feel free to find a better solution to implement this logic.
        .filter((p) => p.gesuch),
      version,
    };
  },
);
