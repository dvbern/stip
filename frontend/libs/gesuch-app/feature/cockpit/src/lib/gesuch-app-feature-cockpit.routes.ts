import { Route } from '@angular/router';

import { GesuchAppFeatureCockpitComponent } from './gesuch-app-feature-cockpit/gesuch-app-feature-cockpit.component';

export const gesuchAppFeatureCockpitRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    children: [
      {
        path: '',
        component: GesuchAppFeatureCockpitComponent,
        title: 'gesuch-app.dashboard.title',
      },
    ],
  },
];
