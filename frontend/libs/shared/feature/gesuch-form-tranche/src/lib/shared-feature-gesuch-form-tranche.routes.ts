import { Route } from '@angular/router';

import { idAndTrancheIdRoutes } from '@dv/shared/util/gesuch';

import { SharedFeatureGesuchFormTrancheComponent } from './shared-feature-gesuch-form-tranche/shared-feature-gesuch-form-tranche.component';

export const sharedFeatureGesuchFormTrancheRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    children: [
      ...idAndTrancheIdRoutes({
        title: 'shared.tranche.title',
        component: SharedFeatureGesuchFormTrancheComponent,
      }),
      // add more routes here (siblings)
      // it is also possible to add nested routes as children
      // of this feature root component (or even lazy loaded sub features)
    ],
  },
];
