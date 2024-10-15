import { Route } from '@angular/router';

import { hasBenutzer } from '@dv/shared/pattern/global-guards';

export const appRoutes: Route[] = [
  {
    path: 'sachbearbeitung-app-feature-infos-protokoll',
    loadChildren: () =>
      import('@dv/sachbearbeitung-app/feature/infos-protokoll').then(
        (m) => m.sachbearbeitungAppFeatureInfosProtokollRoutes,
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
  {
    path: 'download',
    canActivate: [hasBenutzer],
    loadChildren: () =>
      import('@dv/shared/feature/download').then(
        (m) => m.sharedFeatureDownloadRoutes,
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
