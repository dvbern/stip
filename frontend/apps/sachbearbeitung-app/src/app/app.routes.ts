import { Route } from '@angular/router';

import { hasBenutzer } from '@dv/shared/pattern/global-guards';

export const appRoutes: Route[] = [
  {
    path: 'sachbearbeitung-app-feature-gesuchsperiode',
    loadChildren: () =>
      import('@dv/sachbearbeitung-app/feature/gesuchsperiode').then(
        (m) => m.sachbearbeitungAppFeatureGesuchsperiodeRoutes,
      ),
  },
  {
    path: 'sachbearbeitung-app-feature-administration-buchstaben-zuteilung',
    loadChildren: () =>
      import(
        '@dv/sachbearbeitung-app/feature/administration-buchstaben-zuteilung'
      ).then(
        (m) =>
          m.sachbearbeitungAppFeatureAdministrationBuchstabenZuteilungRoutes,
      ),
  },
  {
    path: 'sachbearbeitung-app-feature-administration',
    loadChildren: () =>
      import('@dv/sachbearbeitung-app/feature/administration').then(
        (m) => m.sachbearbeitungAppFeatureAdministrationRoutes,
      ),
  },
  {
    path: 'sachbearbeitung-app-feature-administration-buchstaben-zuteilung',
    loadChildren: () =>
      import(
        '@dv/sachbearbeitung-app/feature/administration-buchstaben-zuteilung'
      ).then(
        (m) =>
          m.sachbearbeitungAppFeatureAdministrationBuchstabenZuteilungRoutes,
      ),
  },
  {
    path: 'administration',
    canActivate: [hasBenutzer],
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
    canActivate: [hasBenutzer],
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
