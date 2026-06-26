import { Route } from '@angular/router';

import { routeWithUnsavedChangesGuard } from '@dv/shared/pattern/unsaved-guard';

import { SharedFeatureDarlehenComponent } from './shared-feature-darlehen/shared-feature-darlehen.component';

export const sharedFeatureDarlehenRoutes: Route[] = [
  routeWithUnsavedChangesGuard({
    path: ':darlehenId/gesuch/:gesuchId',
    pathMatch: 'prefix',
    component: SharedFeatureDarlehenComponent,
    title: 'shared.darlehen.title',
    data: { shouldReuseRoute: false },
  }),
];
