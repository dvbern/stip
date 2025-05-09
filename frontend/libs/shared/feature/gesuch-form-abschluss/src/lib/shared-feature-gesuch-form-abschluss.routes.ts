import { Route } from '@angular/router';

import { idAndTrancheIdRoutes } from '@dv/shared/util/gesuch';

import { SharedFeatureGesuchFormAbschlussComponent } from './shared-feature-gesuch-form-abschluss/shared-feature-gesuch-form-abschluss.component';

export const sharedFeatureGesuchFormAbschlussRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    children: [
      ...idAndTrancheIdRoutes({
        title: 'shared.abschluss.title',
        component: SharedFeatureGesuchFormAbschlussComponent,
      }),
      // add more routes here (siblings)
      // it is also possible to add nested routes as children
      // of this feature root component (or even lazy loaded sub features)
    ],
  },
];
