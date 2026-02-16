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
        path: '',
        pathMatch: 'full',
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
  ...appRoutes,
  {
    path: '**',
    redirectTo: '',
  },
];
