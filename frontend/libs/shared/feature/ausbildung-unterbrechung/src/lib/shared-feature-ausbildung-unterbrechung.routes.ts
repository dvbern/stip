import { Route } from '@angular/router';

import { AusbildungStore } from '@dv/shared/data-access/ausbildung';

import { SharedFeatureAusbildungUnterbrechungComponent } from './shared-feature-ausbildung-unterbrechung/shared-feature-ausbildung-unterbrechung.component';

export const sharedFeatureAusbildungUnterbrechungRoutes: Route[] = [
  {
    path: '',
    title: 'shared.ausbildung-unterbrechen.title',
    pathMatch: 'prefix',
    providers: [AusbildungStore],
    children: [
      {
        path: ':ausbildungUnterbruchId',
        component: SharedFeatureAusbildungUnterbrechungComponent,
      },
      {
        path: ':ausbildungUnterbruchId/fall/:fallId',
        component: SharedFeatureAusbildungUnterbrechungComponent,
      },
    ],
  },
];
