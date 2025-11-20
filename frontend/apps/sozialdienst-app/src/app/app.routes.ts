import { Route } from '@angular/router';

import { hasBenutzer } from '@dv/shared/pattern/global-guards';
import { hasRoles } from '@dv/shared/pattern/status-guard';

export const appRoutes: Route[] = [
  {
    path: 'sozialdienst-app-feature-darlehen',
    loadChildren: () =>
      import('@dv/sozialdienst-app/feature/darlehen').then(
        (m) => m.sozialdienstAppFeatureDarlehenRoutes,
      ),
  },
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
        path: 'fall/:id',
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
          import('@dv/sozialdienst-app/feature/gesuch-form').then(
            (m) => m.SozialdienstAppFeatureGesuchFormComponent,
          ),
        loadChildren: () =>
          import('@dv/sozialdienst-app/feature/gesuch-form').then(
            (m) => m.sozialdienstAppFeatureGesuchFormRoutes,
          ),
      },
      {
        path: 'darlehen',
        loadChildren: () =>
          import('@dv/sozialdienst-app/feature/darlehen').then(
            (m) => m.sozialdienstAppFeatureDarlehenRoutes,
          ),
      },
      {
        path: 'administration',
        canActivate: [hasBenutzer, hasRoles(['V0_Sozialdienst-Admin'])],
        title: 'sachbearbeitung-app.admin.title',
        loadComponent: () =>
          import('@dv/sozialdienst-app/feature/administration').then(
            (m) => m.SachbearbeitungAppFeatureAdministrationComponent,
          ),
        loadChildren: () =>
          import('@dv/sozialdienst-app/feature/administration').then(
            (m) => m.sachbearbeitungAppFeatureAdministrationRoutes,
          ),
      },
      {
        path: 'auszahlung',
        loadChildren: () =>
          import('@dv/sozialdienst-app/feature/auszahlung').then(
            (m) => m.sozialdienstAppFeatureAuszahlungRoutes,
          ),
      },
    ],
  },
];
