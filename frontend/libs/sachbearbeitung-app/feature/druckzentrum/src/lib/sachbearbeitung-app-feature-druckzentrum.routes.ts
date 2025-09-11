import { Route } from '@angular/router';

import { SachbearbeitungAppFeatureDruckzentrumComponent } from './sachbearbeitung-app-feature-druckzentrum/sachbearbeitung-app-feature-druckzentrum.component';

export const sachbearbeitungAppFeatureDruckzentrumRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [
      // feature specific services and other providers
      // always remove { providedIn: 'root' } from the feature specific services
    ],
    children: [
      { path: '', component: SachbearbeitungAppFeatureDruckzentrumComponent },
      // add more routes here (siblings)
      // it is also possible to add nested routes as children
      // of this feature root component (or even lazy loaded sub features)
    ],
  },
];
