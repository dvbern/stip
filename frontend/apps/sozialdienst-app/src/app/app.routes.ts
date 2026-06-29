import { Route } from '@angular/router';

import { DashboardStore } from '@dv/shared/data-access/dashboard';
import { NotificationStore } from '@dv/shared/data-access/notification';
import { hasBenutzer } from '@dv/shared/pattern/global-guards';
import { hasRoles } from '@dv/shared/pattern/status-guard';

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
          import('@dv/sozialdienst-app/feature/cockpit').then(
            (m) => m.sozialdienstAppFeatureCockpitRoutes,
          ),
      },
      {
        path: 'fall/:fallId',
        loadComponent: () =>
          import('@dv/sozialdienst-app/feature/gesuch-cockpit').then(
            (m) => m.SozialdienstAppFeatureGesuchCockpitComponent,
          ),
        loadChildren: () =>
          import('@dv/sozialdienst-app/feature/gesuch-cockpit').then(
            (m) => m.sozialdienstAppFeatureGesuchCockpitRoutes,
          ),
      },
      {
        path: 'gesuch',
        loadComponent: () =>
          import('@dv/shared/feature/gesuch-layout').then(
            (m) => m.SharedFeatureGesuchLayoutComponent,
          ),
        loadChildren: () =>
          import('@dv/shared/feature/gesuch-layout-routes').then(
            (m) => m.sharedFeatureGesuchLayoutRoutes,
          ),
      },
      {
        path: 'darlehen',
        loadChildren: () =>
          import('@dv/shared/feature/darlehen-form').then(
            (m) => m.sharedFeatureDarlehenFeatureRoutes,
          ),
      },
      {
        path: 'administration',
        canActivate: [hasBenutzer, hasRoles(['V0_Sozialdienst-Admin'])],
        title: 'sozialdienst-app.admin.title',
        loadComponent: () =>
          import('@dv/sozialdienst-app/feature/administration').then(
            (m) => m.SozialdienstAppFeatureAdministrationComponent,
          ),
        loadChildren: () =>
          import('@dv/sozialdienst-app/feature/administration').then(
            (m) => m.sozialdienstAppFeatureAdministrationRoutes,
          ),
      },
      {
        path: 'auszahlung',
        loadChildren: () =>
          import('@dv/sozialdienst-app/feature/auszahlung').then(
            (m) => m.sozialdienstAppFeatureAuszahlungRoutes,
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
