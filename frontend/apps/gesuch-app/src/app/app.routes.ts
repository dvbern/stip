import { Route } from '@angular/router';

import { hasBenutzer } from '@dv/shared/pattern/global-guards';

export const appRoutes: Route[] = [
  {
    path: '',
    canActivate: [hasBenutzer],
    children: [
      {
        path: 'gesuch-app-feature-cockpit',
        title: 'gesuch-app.dashboard.title',
        loadChildren: () =>
          import('@dv/gesuch-app/feature/cockpit').then(
            (m) => m.gesuchAppFeatureCockpitRoutes,
          ),
      },
      {
        path: 'gesuch',
        loadComponent: () =>
          import('@dv/gesuch-app/feature/gesuch-form').then(
            (m) => m.GesuchAppFeatureGesuchFormComponent,
          ),
        loadChildren: () =>
          import('@dv/gesuch-app/feature/gesuch-form').then(
            (m) => m.gesuchAppFeatureGesuchFormRoutes,
          ),
      },
      {
        path: 'darlehen',
        loadChildren: () =>
          import('@dv/gesuch-app/feature/darlehen').then(
            (m) => m.gesuchAppFeatureDarlehenRoutes,
          ),
      },
      {
        path: 'auszahlung',
        loadChildren: () =>
          import('@dv/gesuch-app/feature/auszahlung').then(
            (m) => m.gesuchAppFeatureAuszahlungRoutes,
          ),
      },
    ],
  },
];

export const routes: Route[] = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'gesuch-app-feature-cockpit',
  },
  ...appRoutes,
  {
    path: '**',
    redirectTo: 'gesuch-app-feature-cockpit',
  },
];
