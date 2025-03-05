import { Route } from '@angular/router';

import { routeWithUnsavedChangesGuard } from '@dv/shared/pattern/unsaved-guard';
import { idAndTrancheIdRoutes } from '@dv/shared/util/gesuch';

import { SachbearbeitungAppFeatureGesuchFormElternSteuerdatenComponent } from './sachbearbeitung-app-feature-gesuch-form-eltern-steuerdaten/sachbearbeitung-app-feature-gesuch-form-eltern-steuerdaten.component';

export const sachbearbeitungAppFeatureGesuchFormSteuerdatenRoutes: Route[] = [
  ...idAndTrancheIdRoutes(
    routeWithUnsavedChangesGuard({
      path: '',
      component: SachbearbeitungAppFeatureGesuchFormElternSteuerdatenComponent,
    }),
  ),
  // add more routes here (siblings)
  // it is also possible to add nested routes as children
  // of this feature root component (or even lazy loaded sub features)
];
