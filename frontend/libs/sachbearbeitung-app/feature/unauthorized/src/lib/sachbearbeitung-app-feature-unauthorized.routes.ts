import { Route } from '@angular/router';

import { SachbearbeitungAppFeatureUnauthorizedComponent } from './sachbearbeitung-app-feature-unauthorized/sachbearbeitung-app-feature-unauthorized.component';

export const sachbearbeitungAppFeatureUnauthorizedRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    children: [
      { path: '', component: SachbearbeitungAppFeatureUnauthorizedComponent },
    ],
  },
];
