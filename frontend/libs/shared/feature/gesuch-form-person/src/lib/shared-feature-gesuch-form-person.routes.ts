import { Route } from '@angular/router';
import { provideEffects } from '@ngrx/effects';
import { provideState } from '@ngrx/store';

import {
  sharedDataAccessStammdatenEffects,
  sharedDataAccessStammdatensFeature,
} from '@dv/shared/data-access/stammdaten';
import { routeWithUnsavedChangesGuard } from '@dv/shared/pattern/unsaved-guard';
import { idAndTrancheIdRoutes } from '@dv/shared/util/gesuch';

import { SharedFeatureGesuchFormPersonComponent } from './shared-feature-gesuch-form-person/shared-feature-gesuch-form-person.component';

export const gesuchAppFeatureGesuchFormPersonRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [
      // ausbildungsstaette needed for the planned ausbildung at the bottom of lebenslauf
      provideState(sharedDataAccessStammdatensFeature),
      provideEffects(sharedDataAccessStammdatenEffects),
    ],
    data: {
      shouldReuseRoute: false,
    },
    children: [
      ...idAndTrancheIdRoutes(
        routeWithUnsavedChangesGuard({
          title: 'shared.person.title',
          component: SharedFeatureGesuchFormPersonComponent,
        }),
      ),
      // add more routes here (siblings)
      // it is also possible to add nested routes as children
      // of this feature root component (or even lazy loaded sub features)
    ],
  },
];
