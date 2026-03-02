import { Route } from '@angular/router';

import { AusbildungStore } from '@dv/shared/data-access/ausbildung';
import { routeWithUnsavedChangesGuard } from '@dv/shared/pattern/unsaved-guard';

import { SharedFeatureAusbildungUnterbrechungComponent } from './shared-feature-ausbildung-unterbrechung/shared-feature-ausbildung-unterbrechung.component';

export const sharedFeatureAusbildungUnterbrechungRoutes: Route[] = [
  {
    path: '',
    title: 'shared.ausbildung-unterbrechen.title',
    pathMatch: 'prefix',
    providers: [AusbildungStore],
    children: [
      routeWithUnsavedChangesGuard({
        path: ':ausbildungUnterbruchId',
        component: SharedFeatureAusbildungUnterbrechungComponent,
      }),
      routeWithUnsavedChangesGuard({
        path: ':ausbildungUnterbruchId/fall/:fallId',
        component: SharedFeatureAusbildungUnterbrechungComponent,
      }),
    ],
  },
];
