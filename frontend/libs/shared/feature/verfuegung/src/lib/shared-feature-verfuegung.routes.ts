import { Route } from '@angular/router';

import { SharedFeatureVerfuegungZusammenfassungComponent } from '@dv/shared/feature/verfuegung-zusammenfassung';
import {
  BERECHNUNG_ROUTE,
  OPTION_ZUSAMMENFASSUNG,
} from '@dv/shared/model/verfuegung';

export const sharedFeatureVerfuegungRoutes: Route[] = [
  {
    path: `:gesuchId/:trancheTyp/:trancheId`,
    children: [
      {
        path: OPTION_ZUSAMMENFASSUNG.route,
        component: SharedFeatureVerfuegungZusammenfassungComponent,
      },
      {
        path: BERECHNUNG_ROUTE + '/:index',
        loadChildren: () =>
          import('@dv/shared/feature/verfuegung-berechnung').then(
            (m) => m.sharedFeatureVerfuegungBerechnungRoutes,
          ),
      },
      {
        path: '',
        pathMatch: 'prefix' as const,
        redirectTo: OPTION_ZUSAMMENFASSUNG.route,
      },
    ],
  },
];
