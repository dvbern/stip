import { Route } from '@angular/router';
import { provideEffects } from '@ngrx/effects';
import { provideState } from '@ngrx/store';

import {
  sharedDataAccessAbschlussEffects,
  sharedDataAccessAbschlussFeature,
} from '@dv/shared/data-access/abschluss';
import { idAndTrancheIdRoutes } from '@dv/shared/util/gesuch';

import { SharedFeatureGesuchFormAbschlussComponent } from './shared-feature-gesuch-form-abschluss/shared-feature-gesuch-form-abschluss.component';

export const sharedFeatureGesuchFormAbschlussRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [
      // feature specific services and other providers
      // always remove { providedIn: 'root' } from the feature specific services
      provideState(sharedDataAccessAbschlussFeature),
      provideEffects(sharedDataAccessAbschlussEffects),
    ],
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
