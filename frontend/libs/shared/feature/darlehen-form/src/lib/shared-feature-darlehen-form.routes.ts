import { Route } from '@angular/router';

import { routeWithUnsavedChangesGuard } from '@dv/shared/pattern/unsaved-guard';

import { SharedFeatureDarlehenFeatureComponent } from './shared-feature-darlehen-form/shared-feature-darlehen-form.component';

export const sharedFeatureDarlehenFeatureRoutes: Route[] = [
  routeWithUnsavedChangesGuard({
    path: ':darlehenId/fall/:fallId',
    pathMatch: 'prefix',
    component: SharedFeatureDarlehenFeatureComponent,
    title: 'shared.darlehen.title',
    data: { shouldReuseRoute: false },
  }),
];
