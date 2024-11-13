import { Route } from '@angular/router';

import { hasBenutzer } from '@dv/shared/pattern/global-guards';

export const appRoutes: Route[] = [
  {
    path: 'gesuch-app-feature-gesuch-form-abschluss',
    loadChildren: () =>
      import('@dv/gesuch-app/feature/gesuch-form-abschluss').then(
        (m) => m.gesuchAppFeatureGesuchFormAbschlussRoutes,
      ),
  },
  {
    path: 'gesuch-app-feature-cockpit',
    canActivate: [hasBenutzer],
    title: 'gesuch-app.dashboard.title',
    loadChildren: () =>
      import('@dv/gesuch-app/feature/cockpit').then(
        (m) => m.gesuchAppFeatureCockpitRoutes,
      ),
  },
  {
    path: 'gesuch',
    canActivate: [hasBenutzer],
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
    redirectTo: 'gesuch-app-feature-cockpit',
  },
  ...appRoutes,
  {
    path: '**',
    redirectTo: 'gesuch-app-feature-cockpit',
  },
];
