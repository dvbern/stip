import { Route } from '@angular/router';

import { SachbearbeitungAppFeatureVerfuegungBerechnungComponent } from './sachbearbeitung-app-feature-verfuegung-berechnung/sachbearbeitung-app-feature-verfuegung-berechnung.component';

export const sachbearbeitungAppFeatureVerfuegungBerechnungRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    children: [
      {
        path: '',
        component: SachbearbeitungAppFeatureVerfuegungBerechnungComponent,
      },
    ],
  },
];
