import { createSelector } from '@ngrx/store';

import { selectVersion } from '@dv/shared/data-access/config';
import { selectSharedDataAccessGesuchsperiodesView } from '@dv/shared/data-access/gesuchsperiode';

export const selectGesuchAppFeatureCockpitView = createSelector(
  selectSharedDataAccessGesuchsperiodesView,
  selectVersion,
  (gesuchsPerioden, version) => {
    return {
      ...gesuchsPerioden,
      gesuchsperiodes: [] as any[],
      version,
    };
  },
);
