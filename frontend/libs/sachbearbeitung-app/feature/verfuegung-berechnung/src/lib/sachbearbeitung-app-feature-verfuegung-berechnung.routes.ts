import { Route } from '@angular/router';

import { BerechnungStore } from '@dv/sachbearbeitung-app/data-access/berechnung';

import { SachbearbeitungAppFeatureVerfuegungBerechnungComponent } from './sachbearbeitung-app-feature-verfuegung-berechnung/sachbearbeitung-app-feature-verfuegung-berechnung.component';

export const sachbearbeitungAppFeatureVerfuegungBerechnungRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [BerechnungStore],
    children: [
      {
        path: '',
        component: SachbearbeitungAppFeatureVerfuegungBerechnungComponent,
      },
    ],
  },
];
