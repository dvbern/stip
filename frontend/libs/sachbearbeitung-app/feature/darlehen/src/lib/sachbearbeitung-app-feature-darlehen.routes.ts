import { Route } from '@angular/router';

import { SachbearbeitungAppFeatureDarlehenComponent } from './sachbearbeitung-app-feature-darlehen/sachbearbeitung-app-feature-darlehen.component';

export const sachbearbeitungAppFeatureDarlehenRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [
      // feature specific services and other providers
      // always remove { providedIn: 'root' } from the feature specific services
    ],
    children: [
      { path: '', component: SachbearbeitungAppFeatureDarlehenComponent },
      // add more routes here (siblings)
      // it is also possible to add nested routes as children
      // of this feature root component (or even lazy loaded sub features)
    ],
  },
];
