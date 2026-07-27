import { Route } from '@angular/router';

import { SharedFeatureVerfuegungBerechnungComponent } from './shared-feature-verfuegung-berechnung/shared-feature-verfuegung-berechnung.component';

export const sharedFeatureVerfuegungBerechnungRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    children: [
      {
        path: '',
        component: SharedFeatureVerfuegungBerechnungComponent,
      },
    ],
  },
];
