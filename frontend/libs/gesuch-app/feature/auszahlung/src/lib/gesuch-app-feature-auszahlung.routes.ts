import { Route } from '@angular/router';

import { AuszahlungStore } from '@dv/shared/data-access/auszahlung';
import { routeWithUnsavedChangesGuard } from '@dv/shared/pattern/unsaved-guard';

import { GesuchAppFeatureAuszahlungComponent } from './gesuch-app-feature-auszahlung/gesuch-app-feature-auszahlung.component';

export const gesuchAppFeatureAuszahlungRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [AuszahlungStore],
    children: [
      routeWithUnsavedChangesGuard({
        path: ':fallId',
        title: 'shared.auszahlung.title',
        component: GesuchAppFeatureAuszahlungComponent,
      }),
    ],
  },
];
