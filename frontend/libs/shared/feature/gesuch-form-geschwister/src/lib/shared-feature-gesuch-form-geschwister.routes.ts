import { Route } from '@angular/router';

import { routeWithUnsavedChangesGuard } from '@dv/shared/pattern/unsaved-guard';
import { idAndTrancheIdRoutes } from '@dv/shared/util/gesuch';

import { SharedFeatureGesuchFormGeschwisterComponent } from './shared-feature-gesuch-form-geschwister/shared-feature-gesuch-form-geschwister.component';

export const gesuchAppFeatureGesuchFormGeschwisterRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [
      // feature specific services and other providers
      // always remove { providedIn: 'root' } from the feature specific services
    ],
    children: [
      ...idAndTrancheIdRoutes(
        routeWithUnsavedChangesGuard({
          title: 'shared.geschwister.title',
          component: SharedFeatureGesuchFormGeschwisterComponent,
          runGuardsAndResolvers: 'always',
        }),
      ),
      // add more routes here (siblings)
      // it is also possible to add nested routes as children
      // of this feature root component (or even lazy loaded sub features)
    ],
  },
];
