import { Route } from '@angular/router';
import { provideEffects } from '@ngrx/effects';
import { provideState } from '@ngrx/store';

import {
  sharedDataAccessGesuchsperiodeEffects,
  sharedDataAccessGesuchsperiodesFeature,
} from '@dv/shared/data-access/gesuchsperiode';
import { NotificationStore } from '@dv/shared/data-access/notification';

import { GesuchAppFeatureCockpitComponent } from './gesuch-app-feature-cockpit/gesuch-app-feature-cockpit.component';

export const gesuchAppFeatureCockpitRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [
      NotificationStore,
      provideState(sharedDataAccessGesuchsperiodesFeature),
      provideEffects(sharedDataAccessGesuchsperiodeEffects),
    ],
    children: [
      {
        path: '',
        component: GesuchAppFeatureCockpitComponent,
        title: 'gesuch-app.cockpit.title',
      },
      // add more routes here (siblings)
      // it is also possible to add nested routes as children
      // of this feature root component (or even lazy loaded sub features)
    ],
  },
];
