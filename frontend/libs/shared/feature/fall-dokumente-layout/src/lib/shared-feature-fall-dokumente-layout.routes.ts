import { Route } from '@angular/router';

import { FallDokumenteStore } from '@dv/shared/data-access/fall-dokumente';

import { SharedFeatureFallDokumenteLayoutComponent } from './shared-feature-fall-dokumente-layout/shared-feature-fall-dokumente-layout.component';

export const sharedFeatureFallDokumenteLayoutRoutes: Route[] = [
  {
    path: ':fallId',
    pathMatch: 'prefix',
    title: 'shared.fallDokumente.title',
    providers: [FallDokumenteStore],
    component: SharedFeatureFallDokumenteLayoutComponent,
    children: [
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'stipendien',
      },
      {
        path: 'stipendien',
        loadComponent: () =>
          import('./components/fall-dokumente.component').then(
            (m) => m.FallDokumenteComponent,
          ),
      },
      {
        path: 'darlehen',
        loadComponent: () =>
          import('./components/darlehen-dokumente.component').then(
            (m) => m.DarlehenDokumenteComponent,
          ),
      },
    ],
  },
];
