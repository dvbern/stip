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
        title: 'shared.dashboard.title',
        loadComponent: () =>
          import('@dv/shared/feature/gesuchsteller-dashboard').then(
            (m) => m.SharedFeatureGesuchstellerDashboardComponent,
          ),
        loadChildren: () =>
          import('@dv/shared/feature/gesuchsteller-dashboard').then(
            (m) => m.sharedFeatureGesuchstellerDashboardRoutes,
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
        path: 'fall-dokumente',
        loadChildren: () =>
          import('@dv/shared/feature/fall-dokumente-layout').then(
            (m) => m.sharedFeatureFallDokumenteLayoutRoutes,
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
