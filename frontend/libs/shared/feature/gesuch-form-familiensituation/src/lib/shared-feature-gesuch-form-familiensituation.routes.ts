import { Route } from '@angular/router';

import { routeWithUnsavedChangesGuard } from '@dv/shared/pattern/unsaved-guard';
import { idAndTrancheIdRoutes } from '@dv/shared/util/gesuch';

import { SharedFeatureGesuchFormFamiliensituationComponent } from './shared-feature-gesuch-form-familiensituation/shared-feature-gesuch-form-familiensituation.component';

export const gesuchAppFeatureGesuchFormFamiliensituationRoutes: Route[] = [
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
          component: SharedFeatureGesuchFormFamiliensituationComponent,
        }),
      ),
      // add more routes here (siblings)
      // it is also possible to add nested routes as children
      // of this feature root component (or even lazy loaded sub features)
    ],
  },
];
