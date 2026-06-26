import { Route } from '@angular/router';

import { SteuerdatenStore } from '@dv/sachbearbeitung-app/data-access/steuerdaten';
import { hasBenutzer } from '@dv/shared/pattern/global-guards';
import { hasRoles } from '@dv/shared/pattern/status-guard';

export const appRoutes: Route[] = [
  {
    path: 'unauthorized',
    loadChildren: () =>
      import('@dv/shared/feature/unauthorized').then(
        (m) => m.sharedFeatureUnauthorizedRoutes,
      ),
  },
  {
    path: '',
    canActivate: [
      hasBenutzer,
      hasRoles(
        [
          'V0_Sachbearbeiter-Admin',
          'V0_Jurist',
          'V0_Sachbearbeiter',
          'V0_Freigabestelle',
          'V0_Sozialdienst-Admin',
        ],
        '/unauthorized',
      ),
    ],
    children: [
      {
        path: 'administration',
        canActivate: [
          hasBenutzer,
          hasRoles(['V0_Sachbearbeiter-Admin', 'V0_Jurist']),
        ],
        title: 'sachbearbeitung-app.admin.title',
        loadComponent: () =>
          import('@dv/sachbearbeitung-app/feature/administration').then(
            (m) => m.SachbearbeitungAppFeatureAdministrationComponent,
          ),
        loadChildren: () =>
          import('@dv/sachbearbeitung-app/feature/administration').then(
            (m) => m.sachbearbeitungAppFeatureAdministrationRoutes,
          ),
      },
      {
        path: 'massendruck',
        title: 'sachbearbeitung-app.massendruck.title',
        loadChildren: () =>
          import('@dv/sachbearbeitung-app/feature/massendruck').then(
            (m) => m.sachbearbeitungAppFeatureMassendruckRoutes,
          ),
      },
      {
        path: 'dashboard',
        canActivate: [
          hasBenutzer,
          hasRoles(
            ['V0_Sachbearbeiter', 'V0_Freigabestelle', 'V0_Jurist'],
            '/administration',
          ),
        ],
        title: 'sachbearbeitung-app.cockpit.title',
        loadChildren: () =>
          import('@dv/sachbearbeitung-app/feature/dashboard').then(
            (m) => m.sachbearbeitungAppFeatureDashboardRoutes,
          ),
      },
      {
        path: 'gesuch',
        canActivate: [hasBenutzer],
        providers: [SteuerdatenStore],
        title: 'sachbearbeitung-app.gesuch-form.title',
        loadComponent: () =>
          import('@dv/shared/feature/gesuch-layout').then(
            (m) => m.SachbearbeitungAppFeatureGesuchLayoutComponent,
          ),
        loadChildren: () =>
          import('@dv/sachbearbeitung-app/feature/gesuch-layout-routes').then(
            (m) => m.sachbearbeitungAppFeatureGesuchLayoutRoutes,
          ),
      },
    ],
  },
];

export const routes: Route[] = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'dashboard/antraege',
  },
  ...appRoutes,
  {
    path: '**',
    redirectTo: 'dashboard/antraege',
  },
];
