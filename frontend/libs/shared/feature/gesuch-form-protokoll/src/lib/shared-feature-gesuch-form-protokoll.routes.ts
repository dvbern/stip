import { Route } from '@angular/router';

import { SharedFeatureGesuchFormProtokollComponent } from './shared-feature-gesuch-form-protokoll/shared-feature-gesuch-form-protokoll.component';

export const sharedFeatureGesuchFormProtokollRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [
      // feature specific services and other providers
      // always remove { providedIn: 'root' } from the feature specific services
    ],
    children: [
      { path: '', component: SharedFeatureGesuchFormProtokollComponent },
      // add more routes here (siblings)
      // it is also possible to add nested routes as children
      // of this feature root component (or even lazy loaded sub features)
    ],
  },
];
