import { Route } from '@angular/router';

import { routeWithUnsavedChangesGuard } from '@dv/shared/pattern/unsaved-guard';

import { SachbearbeitungAppFeatureDarlehenComponent } from './sachbearbeitung-app-feature-darlehen/sachbearbeitung-app-feature-darlehen.component';

export const sachbearbeitungAppFeatureDarlehenRoutes: Route[] = [
  routeWithUnsavedChangesGuard({
    path: ':darlehenId/gesuch/:id',
    pathMatch: 'prefix',
    component: SachbearbeitungAppFeatureDarlehenComponent,
    title: 'shared.darlehen.title',
  }),
];
