import { Route } from '@angular/router';

import { BeschwerdeStore } from '@dv/sachbearbeitung-app/data-access/beschwerde';
import { BESCHWERDEN_ROUTE } from '@dv/sachbearbeitung-app/model/infos';

import { SachbearbeitungAppFeatureInfosBeschwerdeComponent } from './sachbearbeitung-app-feature-infos-beschwerde/sachbearbeitung-app-feature-infos-beschwerde.component';

export const sachbearbeitungAppFeatureInfosBeschwerdeRoutes: Route[] = [
  {
    path: ':id',
    pathMatch: 'prefix',
    providers: [BeschwerdeStore],
    component: SachbearbeitungAppFeatureInfosBeschwerdeComponent,
    data: { option: BESCHWERDEN_ROUTE },
    children: [
      {
        path: 'verlauf',
        loadComponent: () =>
          import('./components/verlauf.component').then(
            (m) => m.VerlaufComponent,
          ),
      },
      {
        path: 'verwaltung',
        loadComponent: () =>
          import('./components/verwaltung.component').then(
            (m) => m.VerwaltungComponent,
          ),
      },
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'verlauf',
      },
    ],
  },
];
