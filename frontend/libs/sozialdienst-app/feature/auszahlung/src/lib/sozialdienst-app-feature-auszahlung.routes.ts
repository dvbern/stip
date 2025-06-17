import { Route } from '@angular/router';

import { AuszahlungStore } from '@dv/shared/data-access/auszahlung';
import { routeWithUnsavedChangesGuard } from '@dv/shared/pattern/unsaved-guard';

import { SozialdienstAppFeatureAuszahlungComponent } from './sozialdienst-app-feature-auszahlung/sozialdienst-app-feature-auszahlung.component';

export const sozialdienstAppFeatureAuszahlungRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [AuszahlungStore],
    children: [
      routeWithUnsavedChangesGuard({
        path: ':fallId',
        title: 'shared.auszahlung.title',
        component: SozialdienstAppFeatureAuszahlungComponent,
      }),
    ],
  },
];
