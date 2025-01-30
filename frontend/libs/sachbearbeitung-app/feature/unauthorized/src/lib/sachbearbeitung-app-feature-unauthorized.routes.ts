import { Route } from '@angular/router';

import { SachbearbeitungAppFeatureUnauthorizedComponent } from './sachbearbeitung-app-feature-unauthorized/sachbearbeitung-app-feature-unauthorized.component';

export const sachbearbeitungAppFeatureUnauthorizedRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    title: 'sachbearbeitung-app.unauthorized.title',
    children: [
      { path: '', component: SachbearbeitungAppFeatureUnauthorizedComponent },
    ],
  },
];
