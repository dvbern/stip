import { Route } from '@angular/router';

import { routeWithUnsavedChangesGuard } from '@dv/shared/pattern/unsaved-guard';
import { idAndTrancheIdRoutes } from '@dv/shared/util/gesuch';

import { SharedFeatureGesuchFormElternSteuererklaerungComponent } from './shared-feature-gesuch-form-eltern-steuererklaerung/shared-feature-gesuch-form-eltern-steuererklaerung.component';

export const sharedFeatureGesuchFormElternSteuererklaerungRoutes: Route[] = [
  ...idAndTrancheIdRoutes(
    routeWithUnsavedChangesGuard({
      pathMatch: 'prefix',
      component: SharedFeatureGesuchFormElternSteuererklaerungComponent,
    }),
  ),
];
