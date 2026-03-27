import { Route } from '@angular/router';

import { SachbearbeitungAppFeatureGesucheComponent } from './sachbearbeitung-app-feature-gesuche/sachbearbeitung-app-feature-gesuche.component';

// todo-discuss-scph: all that redundancy a problem?
export const sachbearbeitungAppFeatureGesucheRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [],
    children: [
      { path: '', component: SachbearbeitungAppFeatureGesucheComponent },
    ],
  },
];
