import { Route } from '@angular/router';

import { GesuchAppFeatureDarlehenComponent } from './gesuch-app-feature-darlehen/gesuch-app-feature-darlehen.component';

export const gesuchAppFeatureDarlehenRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [],
    children: [
      {
        path: ':fallId',
        title: 'shared.darlehen.title',
        component: GesuchAppFeatureDarlehenComponent,
      },
    ],
  },
];
