import { Route } from '@angular/router';

import { baseGesuchFormRoutes } from '@dv/shared/feature/gesuch-form-routes';
import { ABSCHLUSS } from '@dv/shared/model/gesuch-form';

export const sozialdienstAppFeatureGesuchFormRoutes: Route[] = [
  ...baseGesuchFormRoutes,
  {
    path: ABSCHLUSS.route,
    resolve: {
      step: () => ABSCHLUSS,
    },
    title: ABSCHLUSS.translationKey,
    loadChildren: () =>
      import('@dv/shared-feature-gesuch-form-abschluss').then(
        (m) => m.gesuchAppFeatureGesuchFormAbschlussRoutes,
      ),
  },
];
