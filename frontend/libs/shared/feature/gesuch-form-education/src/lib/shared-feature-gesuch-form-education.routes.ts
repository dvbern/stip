import { Route } from '@angular/router';

import { AusbildungsstaetteStore } from '@dv/shared/data-access/ausbildungsstaette';
import { routeWithUnsavedChangesGuard } from '@dv/shared/pattern/unsaved-guard';
import { idAndTrancheIdRoutes } from '@dv/shared/util/gesuch';

import { SharedFeatureGesuchFormEducationComponent } from './shared-feature-gesuch-form-education/shared-feature-gesuch-form-education.component';

export const gesuchAppFeatureGesuchFormEducationRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [AusbildungsstaetteStore],
    children: [
      ...idAndTrancheIdRoutes(
        routeWithUnsavedChangesGuard({
          title: 'shared.education.title',
          component: SharedFeatureGesuchFormEducationComponent,
        }),
      ),
      // add more routes here (siblings)
      // it is also possible to add nested routes as children
      // of this feature root component (or even lazy loaded sub features)
    ],
  },
];
