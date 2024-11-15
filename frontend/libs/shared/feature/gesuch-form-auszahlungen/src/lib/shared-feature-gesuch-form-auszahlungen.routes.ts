import { Route } from '@angular/router';

import { routeWithUnsavedChangesGuard } from '@dv/shared/pattern/unsaved-guard';
import { idAndTrancheIdRoutes } from '@dv/shared/util/gesuch';

import { SharedFeatureGesuchFormAuszahlungenComponent } from './shared-feature-gesuch-form-auszahlungen/shared-feature-gesuch-form-auszahlungen.component';

export const gesuchAppFeatureGesuchFormAuszahlungenRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [],
    children: [
      ...idAndTrancheIdRoutes(
        routeWithUnsavedChangesGuard({
          title: 'shared.auszahlung.title',
          component: SharedFeatureGesuchFormAuszahlungenComponent,
        }),
      ),
      // add more routes here (siblings)
      // it is also possible to add nested routes as children
      // of this feature root component (or even lazy loaded sub features)
    ],
  },
];
