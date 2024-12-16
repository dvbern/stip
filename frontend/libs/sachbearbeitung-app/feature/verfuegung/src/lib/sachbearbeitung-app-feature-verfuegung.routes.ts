import { Route } from '@angular/router';

import {
  BERECHNUNG_ROUTE,
  OPTION_ZUSAMMENFASSUNG,
} from '@dv/sachbearbeitung-app/model/verfuegung';
import { SharedFeatureVerfuegungZusammenfassungComponent } from '@dv/shared/feature/verfuegung-zusammenfassung';

export const sachbearbeitungAppFeatureVerfuegungRoutes: Route[] = [
  {
    path: ':id',
    children: [
      {
        path: OPTION_ZUSAMMENFASSUNG.route,
        component: SharedFeatureVerfuegungZusammenfassungComponent,
      },
      {
        path: BERECHNUNG_ROUTE + '/:index',
        loadChildren: () =>
          import('@dv/sachbearbeitung-app/feature/verfuegung-berechnung').then(
            (m) => m.sachbearbeitungAppFeatureVerfuegungBerechnungRoutes,
          ),
      },
      {
        path: '',
        pathMatch: 'prefix',
        redirectTo: OPTION_ZUSAMMENFASSUNG.route,
      },
    ],
  },
];
