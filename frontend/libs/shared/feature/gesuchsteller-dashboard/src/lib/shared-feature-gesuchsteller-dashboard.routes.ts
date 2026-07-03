import { Route } from '@angular/router';

import { SharedFeatureGesuchstellerDashboardComponent } from './shared-feature-gesuchsteller-dashboard/shared-feature-gesuchsteller-dashboard.component';

export const sharedFeatureGesuchstellerDashboardRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    children: [
      { path: '', component: SharedFeatureGesuchstellerDashboardComponent },
    ],
  },
];
