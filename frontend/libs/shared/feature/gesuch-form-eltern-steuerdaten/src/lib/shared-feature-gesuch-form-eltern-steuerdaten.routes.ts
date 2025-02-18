import { Route } from '@angular/router';

import { SteuerdatenStore } from '@dv/shared/data-access/steuerdaten';
import { idAndTrancheIdRoutes } from '@dv/shared/util/gesuch';

import { SharedFeatureGesuchFormElternSteuerdatenComponent } from './shared-feature-gesuch-form-eltern-steuerdaten/shared-feature-gesuch-form-eltern-steuerdaten.component';

export const sharedFeatureGesuchFormElternSteuerdatenRoutes: Route[] = [
  ...idAndTrancheIdRoutes({
    providers: [SteuerdatenStore],
    pathMatch: 'prefix',
    component: SharedFeatureGesuchFormElternSteuerdatenComponent,
  }),
];
