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
        data: {
          // reinitialize when navigated to the same route
          shouldReuseRoute: false,
        },
      }),
    ],
  },
];
