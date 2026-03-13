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
        path: 'dashboard',
        title: 'gesuch-app.dashboard.title',
        loadChildren: () =>
          import('@dv/gesuch-app/feature/cockpit').then(
            (m) => m.gesuchAppFeatureCockpitRoutes,
          ),
      },
      {
        path: 'ausbildung-unterbrechen',
        loadChildren: () =>
          import('@dv/shared/feature/ausbildung-unterbrechung').then(
            (m) => m.sharedFeatureAusbildungUnterbrechungRoutes,
          ),
      },
      {
        path: 'gesuch',
        loadComponent: () =>
          import('@dv/shared/feature/gesuch-form').then(
            (m) => m.SharedFeatureGesuchFormComponent,
          ),
        loadChildren: () =>
          import('@dv/shared/feature/gesuch-form').then(
            (m) => m.sharedFeatureGesuchFormRoutes,
          ),
      },
      {
        path: 'darlehen',
        loadChildren: () =>
          import('@dv/shared/feature/darlehen').then(
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
    redirectTo: 'dashboard',
  },
  ...appRoutes,
  {
    path: '**',
    redirectTo: 'dashboard',
  },
];
