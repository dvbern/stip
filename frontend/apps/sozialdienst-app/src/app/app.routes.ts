import { Route } from '@angular/router';

import { hasBenutzer } from '@dv/shared/pattern/global-guards';

export const appRoutes: Route[] = [
  {
    path: '',
    canActivate: [hasBenutzer],
    children: [
      {
        path: '',
        title: 'sozialdienst-app.dashboard.title',
        loadChildren: () =>
          import('@dv/sozialdienst-app/feature/cockpit').then(
            (m) => m.sozialdienstAppFeatureCockpitRoutes,
          ),
      },
      {
        path: 'gesuch',
        loadComponent: () =>
          import('@dv/sozialdienst-app/feature/gesuch-form').then(
            (m) => m.SozialdienstAppFeatureGesuchFormComponent,
          ),
        loadChildren: () =>
          import('@dv/sozialdienst-app/feature/gesuch-form').then(
            (m) => m.sozialdienstAppFeatureGesuchFormRoutes,
          ),
      },
    ],
  },
];
