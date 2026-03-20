import { Route } from '@angular/router';

import {
  BERECHNUNG_ROUTE,
  OPTION_ZUSAMMENFASSUNG,
} from '@dv/sachbearbeitung-app/model/verfuegung';
import { SharedFeatureVerfuegungZusammenfassungComponent } from '@dv/shared/feature/verfuegung-zusammenfassung';
import { trancheRoutes } from '@dv/shared/model/gesuch';

export const sachbearbeitungAppFeatureVerfuegungRoutes: Route[] = [
  ...trancheRoutes.map((route) => ({
    path: `:gesuchId/${route}/:trancheId`,
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
        pathMatch: 'prefix' as const,
        redirectTo: OPTION_ZUSAMMENFASSUNG.route,
      },
    ],
  })),
];
