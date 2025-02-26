import { Route } from '@angular/router';

import { SteuerdatenStore } from '@dv/shared/data-access/steuerdaten';
import { routeWithUnsavedChangesGuard } from '@dv/shared/pattern/unsaved-guard';
import { idAndTrancheIdRoutes } from '@dv/shared/util/gesuch';

import { SharedFeatureGesuchFormElternSteuererklaerungComponent } from './shared-feature-gesuch-form-eltern-steuererklaerung/shared-feature-gesuch-form-eltern-steuererklaerung.component';

export const sharedFeatureGesuchFormElternSteuererklaerungRoutes: Route[] = [
  ...idAndTrancheIdRoutes(
    routeWithUnsavedChangesGuard({
      providers: [SteuerdatenStore],
      pathMatch: 'prefix',
      component: SharedFeatureGesuchFormElternSteuererklaerungComponent,
    }),
  ),
];
