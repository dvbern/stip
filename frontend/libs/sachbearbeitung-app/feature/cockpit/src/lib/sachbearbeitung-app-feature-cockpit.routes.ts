import { Route } from '@angular/router';

import { SachbearbeitungAppFeatureCockpitComponent } from './sachbearbeitung-app-feature-cockpit/sachbearbeitung-app-feature-cockpit.component';

export const sachbearbeitungAppFeatureCockpitRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    children: [
      {
        path: '',
        component: SachbearbeitungAppFeatureCockpitComponent,
        title: 'sachbearbeitung-app.cockpit.title',
      },
      // add more routes here (siblings)
      // it is also possible to add nested routes as children
      // of this feature root component (or even lazy loaded sub features)
    ],
  },
];
