import { inject } from '@angular/core';
import { Route } from '@angular/router';
import { SharedFeatureVerfuegungZusammenfassungComponent } from '@dv/shared/feature/verfuegung-zusammenfassung';
import { Store } from '@ngrx/store';

import {
  BERECHNUNG_ROUTE,
  OPTION_ZUSAMMENFASSUNG,
} from '@dv/sachbearbeitung-app/model/verfuegung';
import { SharedDataAccessGesuchEvents } from '@dv/shared/data-access/gesuch';

export const sachbearbeitungAppFeatureVerfuegungRoutes: Route[] = [
  {
    path: ':id',
    resolve: {
      id: () => {
        const store = inject(Store);
        store.dispatch(SharedDataAccessGesuchEvents.loadGesuch());
      },
    },
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
