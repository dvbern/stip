import { Route } from '@angular/router';

import { hasBenutzer } from '@dv/shared/pattern/global-guards';
import { hasRoles } from '@dv/shared/pattern/status-guard';

export const appRoutes: Route[] = [
  {
    path: 'sachbearbeitung-app-feature-infos-admin',
    loadChildren: () =>
      import('@dv/sachbearbeitung-app/feature/infos-admin').then(
        (m) => m.sachbearbeitungAppFeatureInfosAdminRoutes,
      ),
  },
  {
    path: 'unauthorized',
    loadChildren: () =>
      import('@dv/sachbearbeitung-app/feature/unauthorized').then(
        (m) => m.sachbearbeitungAppFeatureUnauthorizedRoutes,
      ),
  },
  {
    path: '',
    canActivate: [
      hasBenutzer,
      hasRoles(
        ['Admin', 'Jurist', 'Sachbearbeiter', 'Sozialdienst-Admin'],
        '/unauthorized',
      ),
    ],
    children: [
      {
        path: 'sachbearbeitung-app-feature-infos-notizen',
        loadChildren: () =>
          import('@dv/sachbearbeitung-app/feature/infos-notizen').then(
            (m) => m.sachbearbeitungAppFeatureInfosNotizenRoutes,
          ),
      },
      {
        path: 'sachbearbeitung-app-feature-administration-sozialdienst',
        loadChildren: () =>
          import(
            '@dv/sachbearbeitung-app/feature/administration-sozialdienst'
          ).then(
            (m) => m.sachbearbeitungAppFeatureAdministrationSozialdienstRoutes,
          ),
      },
      {
        path: 'sachbearbeitung-app-feature-infos-protokoll',
        loadChildren: () =>
          import('@dv/sachbearbeitung-app/feature/infos-protokoll').then(
            (m) => m.sachbearbeitungAppFeatureInfosProtokollRoutes,
          ),
      },
      {
        path: 'administration',
        canActivate: [hasBenutzer, hasRoles(['Admin', 'Sozialdienst-Admin'])],
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
        path: 'sachbearbeitung-app-feature-cockpit',
        canActivate: [
          hasBenutzer,
          hasRoles(['Sachbearbeiter'], '/administration'),
        ],
        title: 'sachbearbeitung-app.cockpit.title',
        loadChildren: () =>
          import('@dv/sachbearbeitung-app/feature/cockpit').then(
            (m) => m.sachbearbeitungAppFeatureCockpitRoutes,
          ),
      },
      {
        path: 'gesuch',
        canActivate: [hasBenutzer],
        title: 'sachbearbeitung-app.gesuch-form.title',
        loadComponent: () =>
          import('@dv/sachbearbeitung-app/feature/gesuch-form').then(
            (m) => m.SachbearbeitungAppFeatureGesuchFormComponent,
          ),
        loadChildren: () =>
          import('@dv/sachbearbeitung-app/feature/gesuch-form').then(
            (m) => m.sachbearbeitungAppFeatureGesuchFormRoutes,
          ),
      },
      {
        path: 'verfuegung',
        // TODO: @spse check what to do if the verfügung page is opened by a SB directly
        // if everything is fine, remove the isAllowedTo Helper function as it is not needed anymore
        // otherwise check how to change the guard function to cover the case
        canActivate: [hasBenutzer],
        title: 'sachbearbeitung-app.verfuegung.title',
        loadComponent: () =>
          import('@dv/sachbearbeitung-app/feature/verfuegung').then(
            (m) => m.SachbearbeitungAppFeatureVerfuegungComponent,
          ),
        loadChildren: () =>
          import('@dv/sachbearbeitung-app/feature/verfuegung').then(
            (m) => m.sachbearbeitungAppFeatureVerfuegungRoutes,
          ),
      },
      {
        path: 'infos',
        canActivate: [hasBenutzer],
        title: 'sachbearbeitung-app.infos.title',
        loadComponent: () =>
          import('@dv/sachbearbeitung-app/feature/infos').then(
            (m) => m.SachbearbeitungAppFeatureInfosComponent,
          ),
        loadChildren: () =>
          import('@dv/sachbearbeitung-app/feature/infos').then(
            (m) => m.sachbearbeitungAppFeatureInfosRoutes,
          ),
      },
    ],
  },
];

export const routes: Route[] = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'sachbearbeitung-app-feature-cockpit',
  },
  ...appRoutes,
  {
    path: '**',
    redirectTo: 'sachbearbeitung-app-feature-cockpit',
  },
];
