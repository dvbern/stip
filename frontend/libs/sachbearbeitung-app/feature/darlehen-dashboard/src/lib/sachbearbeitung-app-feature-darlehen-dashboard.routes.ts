import { Route } from '@angular/router';

import { SachbearbeitungAppFeatureDarlehenDashboardComponent } from './sachbearbeitung-app-feature-darlehen-dashboard/sachbearbeitung-app-feature-darlehen-dashboard.component';

export const sachbearbeitungAppFeatureDarlehenDashboardRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    children: [
      {
        path: '',
        component: SachbearbeitungAppFeatureDarlehenDashboardComponent,
      },
    ],
  },
];
