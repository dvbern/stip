import { Route } from '@angular/router';

import { hasBenutzer } from '@dv/shared/pattern/global-guards';
import { hasRoles } from '@dv/shared/pattern/status-guard';

export const appRoutes: Route[] = [
  {
    path: '',
    canActivate: [
      hasBenutzer,
      hasRoles(['V0_DEMO_DATA_APPLY'], '/unauthorized'),
    ],
    children: [
      {
        path: '',
        loadChildren: () =>
          import('@dv/demo-data-app/feature/demo-data-overview').then(
            (m) => m.demoDataAppFeatureDemoDataOverviewRoutes,
          ),
      },
    ],
  },
  {
    path: 'unauthorized',
    loadChildren: () =>
      import('@dv/shared/feature/unauthorized').then(
        (m) => m.sharedFeatureUnauthorizedRoutes,
      ),
  },
];
