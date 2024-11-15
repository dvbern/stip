import { Route } from '@angular/router';

import { routeWithUnsavedChangesGuard } from '@dv/shared/pattern/unsaved-guard';
import { idAndTrancheIdRoutes } from '@dv/shared/util/gesuch';

import { SharedFeatureAusbildungComponent } from './shared-feature-ausbildung/shared-feature-ausbildung.component';

export const sharedFeatureAusbildungRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [],
    children: [
      ...idAndTrancheIdRoutes(
        routeWithUnsavedChangesGuard({
          title: 'shared.ausbildung.title',
          component: SharedFeatureAusbildungComponent,
        }),
      ),
    ],
  },
];
