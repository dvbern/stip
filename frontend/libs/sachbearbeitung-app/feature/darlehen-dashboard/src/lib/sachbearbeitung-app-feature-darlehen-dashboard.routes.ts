import { Route } from '@angular/router';

import { SachbearbeitungAppFeatureDarlehenDashboardComponent } from './sachbearbeitung-app-feature-darlehen-dashboard/sachbearbeitung-app-feature-darlehen-dashboard.component';

export const sachbearbeitungAppFeatureDarlehenDashboardRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [
      // feature specific services and other providers
      // always remove { providedIn: 'root' } from the feature specific services
    ],
    children: [
      {
        path: '',
        component: SachbearbeitungAppFeatureDarlehenDashboardComponent,
        // title: 'sachbearbeitung-app.darlehen-dashboard.title',
      },
      // add more routes here (siblings)
      // it is also possible to add nested routes as children
      // of this feature root component (or even lazy loaded sub features)
    ],
  },
];
