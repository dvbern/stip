import { Route } from '@angular/router';

import { idAndTrancheIdRoutes } from '@dv/shared/util/gesuch';

import { SharedFeatureGesuchFormElternSteuerdatenComponent } from './shared-feature-gesuch-form-eltern-steuerdaten/shared-feature-gesuch-form-eltern-steuerdaten.component';

export const sharedFeatureGesuchFormElternSteuerdatenRoutes: Route[] = [
  ...idAndTrancheIdRoutes({
    pathMatch: 'prefix',
    component: SharedFeatureGesuchFormElternSteuerdatenComponent,
  }),
];
