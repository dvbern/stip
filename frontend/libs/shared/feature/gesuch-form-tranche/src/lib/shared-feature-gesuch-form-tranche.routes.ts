import { Route } from '@angular/router';

import {} from '@dv/shared/feature/gesuch-form-geschwister';
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
      {
        path: ':id',
        title: 'shared.tranche.title',
        component: SharedFeatureGesuchFormTrancheComponent,
        runGuardsAndResolvers: 'always',
        data: {
          // reinitialize when navigated to the same route
          shouldReuseRoute: false,
        },
      },
      // add more routes here (siblings)
      // it is also possible to add nested routes as children
      // of this feature root component (or even lazy loaded sub features)
    ],
  },
];
