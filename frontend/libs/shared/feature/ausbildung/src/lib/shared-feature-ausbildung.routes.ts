import { Route } from '@angular/router';

import { SharedFeatureAusbildungComponent } from './shared-feature-ausbildung/shared-feature-ausbildung.component';

export const sharedFeatureAusbildungRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [
      // feature specific services and other providers
      // always remove { providedIn: 'root' } from the feature specific services
    ],
    children: [
      { path: '', component: SharedFeatureAusbildungComponent },
      // add more routes here (siblings)
      // it is also possible to add nested routes as children
      // of this feature root component (or even lazy loaded sub features)
    ],
  },
];
