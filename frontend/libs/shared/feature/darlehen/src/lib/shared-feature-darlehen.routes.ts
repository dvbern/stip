import { Route } from '@angular/router';

import { routeWithUnsavedChangesGuard } from '@dv/shared/pattern/unsaved-guard';

import { SharedFeatureDarlehenFeatureComponent } from './shared-feature-darlehen/shared-feature-darlehen.component';

export const sharedFeatureDarlehenFeatureRoutes: Route[] = [
  routeWithUnsavedChangesGuard({
    path: ':darlehenId',
    pathMatch: 'prefix',
    component: SharedFeatureDarlehenFeatureComponent,
    title: 'shared.darlehen.title',
    data: { shouldReuseRoute: false },
  }),
];
