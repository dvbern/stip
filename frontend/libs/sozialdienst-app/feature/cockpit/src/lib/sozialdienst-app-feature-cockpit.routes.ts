import { Route } from '@angular/router';

import { SozialdienstAppFeatureCockpitComponent } from './sozialdienst-app-feature-cockpit/sozialdienst-app-feature-cockpit.component';

export const sozialdienstAppFeatureCockpitRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    children: [
      { path: '', component: SozialdienstAppFeatureCockpitComponent },
      // add more routes here (siblings)
      // it is also possible to add nested routes as children
      // of this feature root component (or even lazy loaded sub features)
    ],
  },
];
