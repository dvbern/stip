import { Route } from '@angular/router';

import { SharedFeatureGesuchFormElternSteuerdatenComponent } from './shared-feature-gesuch-form-eltern-steuerdaten/shared-feature-gesuch-form-eltern-steuerdaten.component';

export const sharedFeatureGesuchFormElternSteuerdatenRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [
      // feature specific services and other providers
      // always remove { providedIn: 'root' } from the feature specific services
    ],
    children: [
      {
        path: '',
        component: SharedFeatureGesuchFormElternSteuerdatenComponent,
      },
      // add more routes here (siblings)
      // it is also possible to add nested routes as children
      // of this feature root component (or even lazy loaded sub features)
    ],
  },
];
