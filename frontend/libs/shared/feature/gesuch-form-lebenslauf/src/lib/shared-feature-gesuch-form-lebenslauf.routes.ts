import { Route } from '@angular/router';

import { AusbildungsstaetteStore } from '@dv/shared/data-access/ausbildungsstaette';
import { routeWithUnsavedChangesGuard } from '@dv/shared/pattern/unsaved-guard';
import { idAndTrancheIdRoutes } from '@dv/shared/util/gesuch';

import { SharedFeatureGesuchFormLebenslaufComponent } from './shared-feature-gesuch-form-lebenslauf/shared-feature-gesuch-form-lebenslauf.component';

export const gesuchAppFeatureGesuchFormLebenslaufRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [AusbildungsstaetteStore],
    children: [
      ...idAndTrancheIdRoutes(
        routeWithUnsavedChangesGuard({
          title: 'shared.lebenslauf.title',
          component: SharedFeatureGesuchFormLebenslaufComponent,
          runGuardsAndResolvers: 'always',
        }),
      ),
    ],
  },
];
