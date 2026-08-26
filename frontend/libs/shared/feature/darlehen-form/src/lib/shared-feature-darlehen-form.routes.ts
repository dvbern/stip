import { Route } from '@angular/router';

import { routeWithUnsavedChangesGuard } from '@dv/shared/pattern/unsaved-guard';

import { SharedFeatureDarlehenFormComponent } from './shared-feature-darlehen-form/shared-feature-darlehen-form.component';

export const sharedFeatureDarlehenFormRoutes: Route[] = [
  // todo: @scph wollen wir die reihenfolge der routes hierarchisch anpassen?
  {
    path: 'fall/:fallId',
    pathMatch: 'prefix',
    component: SharedFeatureDarlehenFormComponent,
    title: 'shared.darlehen.title',
    data: { shouldReuseRoute: false },
  },
  routeWithUnsavedChangesGuard({
    path: ':darlehenId/fall/:fallId',
    pathMatch: 'prefix',
    component: SharedFeatureDarlehenFormComponent,
    title: 'shared.darlehen.title',
    data: { shouldReuseRoute: false },
  }),
];
