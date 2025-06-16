import { Route } from '@angular/router';

import { AuszahlungStore } from '@dv/shared/data-access/auszahlung';
import { routeWithUnsavedChangesGuard } from '@dv/shared/pattern/unsaved-guard';
import { idAndTrancheIdRoutes } from '@dv/shared/util/gesuch';

import { SharedFeatureGesuchFormAuszahlungComponent } from './shared-feature-gesuch-form-auszahlung/shared-feature-gesuch-form-auszahlung.component';

export const sharedFeatureGesuchFormAuszahlungRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [AuszahlungStore],
    children: [
      ...idAndTrancheIdRoutes(
        routeWithUnsavedChangesGuard({
          title: 'shared.auszahlung.title',
          component: SharedFeatureGesuchFormAuszahlungComponent,
        }),
      ),
    ],
  },
];
