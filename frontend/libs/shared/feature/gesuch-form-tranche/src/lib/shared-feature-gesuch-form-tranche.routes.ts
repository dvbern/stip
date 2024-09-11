import { Route } from '@angular/router';

import {} from '@dv/shared/feature/gesuch-form-geschwister';
import { idAndTrancheIdRoutes } from '@dv/shared/util/gesuch';

import { SharedFeatureGesuchFormTrancheComponent } from './shared-feature-gesuch-form-tranche/shared-feature-gesuch-form-tranche.component';

export const sharedFeatureGesuchFormTrancheRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [
      // feature specific services and other providers
      // always remove { providedIn: 'root' } from the feature specific services
    ],
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
