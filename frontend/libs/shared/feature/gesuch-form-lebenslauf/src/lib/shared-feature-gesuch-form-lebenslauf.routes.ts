import { Route } from '@angular/router';
import { provideEffects } from '@ngrx/effects';
import { provideState } from '@ngrx/store';

import {
  gesuchAppDataAccessAusbildungsstaetteEffects,
  gesuchAppDataAccessAusbildungsstaettesFeature,
} from '@dv/shared/data-access/ausbildungsstaette';
import { routeWithUnsavedChangesGuard } from '@dv/shared/pattern/unsaved-guard';

import { SharedFeatureGesuchFormLebenslaufComponent } from './shared-feature-gesuch-form-lebenslauf/shared-feature-gesuch-form-lebenslauf.component';

export const gesuchAppFeatureGesuchFormLebenslaufRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [
      // ausbildungsstaette needed for the planned ausbildung at the bottom of lebenslauf
      provideState(gesuchAppDataAccessAusbildungsstaettesFeature),
      provideEffects(gesuchAppDataAccessAusbildungsstaetteEffects),
    ],
    children: [
      routeWithUnsavedChangesGuard({
        path: ':id',
        title: 'shared.lebenslauf.title',
        component: SharedFeatureGesuchFormLebenslaufComponent,
        runGuardsAndResolvers: 'always',
        data: {
          // reinitialize when navigated to the same route
          shouldReuseRoute: false,
        },
      }),
    ],
  },
];
