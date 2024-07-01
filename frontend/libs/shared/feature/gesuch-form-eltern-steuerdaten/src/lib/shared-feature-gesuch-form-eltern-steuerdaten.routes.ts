import { Route } from '@angular/router';

import { SharedFeatureGesuchFormElternSteuerdatenComponent } from './shared-feature-gesuch-form-eltern-steuerdaten/shared-feature-gesuch-form-eltern-steuerdaten.component';

export const sharedFeatureGesuchFormElternSteuerdatenRoutes: Route[] = [
  {
    path: ':id',
    pathMatch: 'prefix',
    component: SharedFeatureGesuchFormElternSteuerdatenComponent,
  },
];
