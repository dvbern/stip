import { Route } from '@angular/router';

import { baseGesuchFormRoutes } from '@dv/shared/feature/gesuch-form-routes';
import { ELTERN_STEUERDATEN_STEPS } from '@dv/shared/model/gesuch-form';

export const sachbearbeitungAppFeatureGesuchFormRoutes: Route[] = [
  ...baseGesuchFormRoutes,
  ...Object.values(ELTERN_STEUERDATEN_STEPS).map((step) => ({
    path: step.route,
    resolve: {
      step: () => step,
    },
    title: step.translationKey,
    loadChildren: () =>
      import(
        '@dv/sachbearbeitung-app/feature/gesuch-form-eltern-steuerdaten'
      ).then((m) => m.sachbearbeitungAppFeatureGesuchFormSteuerdatenRoutes),
  })),
];
