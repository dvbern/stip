import { Route } from '@angular/router';

import { GesuchAppFeatureAenderungsentryComponent } from './gesuch-app-feature-aenderungsentry/gesuch-app-feature-aenderungsentry.component';

export const gesuchAppFeatureAenderungsentryRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [
      // feature specific services and other providers
      // always remove { providedIn: 'root' } from the feature specific services
    ],
    children: [
      { path: '', component: GesuchAppFeatureAenderungsentryComponent },
      // add more routes here (siblings)
      // it is also possible to add nested routes as children
      // of this feature root component (or even lazy loaded sub features)
    ],
  },
];
