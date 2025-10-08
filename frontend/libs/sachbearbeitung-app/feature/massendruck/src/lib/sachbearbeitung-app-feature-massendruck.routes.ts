import { Route } from '@angular/router';

import { MassendruckDetailComponent } from './components/massendruck-detail.component';
import { SachbearbeitungAppFeatureMassendruckComponent } from './massendruck/sachbearbeitung-app-feature-massendruck.component';

export const sachbearbeitungAppFeatureMassendruckRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [
      // feature specific services and other providers
      // always remove { providedIn: 'root' } from the feature specific services
    ],
    children: [
      { path: '', component: SachbearbeitungAppFeatureMassendruckComponent },
      { path: 'druckauftrag/:id', component: MassendruckDetailComponent },
      // add more routes here (siblings)
      // it is also possible to add nested routes as children
      // of this feature root component (or even lazy loaded sub features)
    ],
  },
];
