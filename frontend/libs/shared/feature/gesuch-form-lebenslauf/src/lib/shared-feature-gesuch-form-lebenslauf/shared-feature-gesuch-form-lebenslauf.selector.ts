import { createSelector } from '@ngrx/store';

import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';

export const selectSharedFeatureGesuchFormLebenslaufVew = createSelector(
  selectSharedDataAccessGesuchsView,
  (gesuchsView) => ({
    loading: gesuchsView.loading,
    gesuch: gesuchsView.gesuch,
    gesuchFormular: gesuchsView.gesuchFormular,
    ausbildung: gesuchsView.gesuchFormular?.ausbildung,
    lebenslaufItems: (gesuchsView.gesuchFormular?.lebenslaufItems ?? []).filter(
      (each) => each?.id,
    ),
    readonly: gesuchsView.readonly,
    gesuchPermissions: gesuchsView.gesuchPermissions,
  }),
);
