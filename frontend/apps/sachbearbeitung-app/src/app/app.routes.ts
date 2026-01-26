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
        path: 'fehlgeschlagene-zahlungen',
        title: 'sachbearbeitung-app.fehlgeschlagene-zahlungen.title',
        loadChildren: () =>
          import(
            '@dv/sachbearbeitung-app/feature/fehlgeschlagene-zahlungen'
          ).then(
            (m) => m.sachbearbeitungAppFeatureFehlgeschlageneZahlungenRoutes,
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
        path: 'sachbearbeitung-app-feature-cockpit',
        canActivate: [
          hasBenutzer,
          hasRoles(
            ['V0_Sachbearbeiter', 'V0_Freigabestelle', 'V0_Jurist'],
            '/administration',
          ),
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
        providers: [SteuerdatenStore],
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
        path: 'darlehen-dashboard',
        canActivate: [hasBenutzer],
        title: 'sachbearbeitung-app.darlehen-dashboard.title',
        loadComponent: () =>
          import('@dv/sachbearbeitung-app/feature/darlehen-dashboard').then(
            (m) => m.SachbearbeitungAppFeatureDarlehenDashboardComponent,
          ),
        loadChildren: () =>
          import('@dv/sachbearbeitung-app/feature/darlehen-dashboard').then(
            (m) => m.sachbearbeitungAppFeatureDarlehenDashboardRoutes,
          ),
      },
      {
        path: 'darlehen',
        canActivate: [hasBenutzer],
        title: 'sachbearbeitung-app.darlehen.title',
        loadChildren: () =>
          import('@dv/sachbearbeitung-app/feature/darlehen').then(
            (m) => m.sachbearbeitungAppFeatureDarlehenRoutes,
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
