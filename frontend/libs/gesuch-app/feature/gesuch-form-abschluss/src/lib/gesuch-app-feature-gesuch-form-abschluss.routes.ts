import { Route } from '@angular/router';

import { idAndTrancheIdRoutes } from '@dv/shared/util/gesuch';

import { GesuchAppFeatureGesuchFormAbschlussComponent } from './gesuch-app-feature-gesuch-form-abschluss/gesuch-app-feature-gesuch-form-abschluss.component';

export const gesuchAppFeatureGesuchFormAbschlussRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    children: [
      ...idAndTrancheIdRoutes({
        title: 'shared.abschluss.title',
        component: GesuchAppFeatureGesuchFormAbschlussComponent,
      }),
      // add more routes here (siblings)
      // it is also possible to add nested routes as children
      // of this feature root component (or even lazy loaded sub features)
    ],
  },
];
