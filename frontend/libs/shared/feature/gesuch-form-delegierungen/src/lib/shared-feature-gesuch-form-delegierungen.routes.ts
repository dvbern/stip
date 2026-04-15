import { Route } from '@angular/router';

import { DelegationStore } from '@dv/shared/data-access/delegation';
import { idAndTrancheIdRoutes } from '@dv/shared/util/gesuch';

import { SharedFeatureGesuchFormDelegierungenComponent } from './shared-feature-gesuch-form-delegierungen/shared-feature-gesuch-form-delegierungen.component';

export const sharedFeatureGesuchFormDelegierungenRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [DelegationStore],
    children: [
      ...idAndTrancheIdRoutes({
        path: '',
        component: SharedFeatureGesuchFormDelegierungenComponent,
      }),
    ],
  },
];
