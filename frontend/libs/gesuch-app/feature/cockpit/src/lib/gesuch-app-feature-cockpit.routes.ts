import { Route } from '@angular/router';

import { DashboardStore } from '@dv/shared/data-access/dashboard';

import { GesuchAppFeatureCockpitComponent } from './gesuch-app-feature-cockpit/gesuch-app-feature-cockpit.component';

export const gesuchAppFeatureCockpitRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [DashboardStore],
    children: [
      {
        path: '',
        component: GesuchAppFeatureCockpitComponent,
        title: 'gesuch-app.dashboard.title',
      },
    ],
  },
];
