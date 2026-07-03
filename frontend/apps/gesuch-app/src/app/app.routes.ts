import { Route } from '@angular/router';

import { DashboardStore } from '@dv/shared/data-access/dashboard';
import { NotificationStore } from '@dv/shared/data-access/notification';
import { hasBenutzer } from '@dv/shared/pattern/global-guards';

export const appRoutes: Route[] = [
  {
    path: '',
    canActivate: [hasBenutzer],
    providers: [DashboardStore],
    children: [
      {
        path: 'dashboard',
        title: 'shared.dashboard.title',
        loadChildren: () =>
          import('@dv/shared/feature/gesuchsteller-dashboard').then(
            (m) => m.sharedFeatureGesuchstellerDashboardRoutes,
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
        path: 'fall-dokumente',
        loadChildren: () =>
          import('@dv/shared/feature/fall-dokumente-layout').then(
            (m) => m.sharedFeatureFallDokumenteLayoutRoutes,
          ),
      },
      {
        path: 'auszahlung',
        loadChildren: () =>
          import('@dv/gesuch-app/feature/auszahlung').then(
            (m) => m.gesuchAppFeatureAuszahlungRoutes,
          ),
      },
      {
        path: 'nachrichten',
        title: 'shared.nachrichten.title',
        providers: [NotificationStore],
        loadChildren: () =>
          import('@dv/shared/feature/notifications').then(
            (m) => m.sharedFeatureNotificationsRoutes,
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
