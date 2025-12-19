import { Route } from '@angular/router';

import { DashboardStore } from '@dv/shared/data-access/dashboard';
import { hasBenutzer } from '@dv/shared/pattern/global-guards';

export const appRoutes: Route[] = [
  {
    path: '',
    canActivate: [hasBenutzer],
    providers: [DashboardStore],
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
          import('@dv/shared/feature/darlehen-feature').then(
            (m) => m.sharedFeatureDarlehenFeatureRoutes,
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
