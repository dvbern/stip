import { Route } from '@angular/router';

import {
  BESCHWERDEN_ROUTE,
  BUCHHALTUNG_ROUTE,
  DARLEHEN_ROUTE,
  NOTIZEN_ROUTE,
  PROTOKOLL_ROUTE,
} from '@dv/sachbearbeitung-app/model/infos';
import { PERSON } from '@dv/shared/model/gesuch-form';
import { SharedUiCommingSoonComponent } from '@dv/shared/ui/comming-soon';

export const sachbearbeitungAppFeatureInfosRoutes: Route[] = [
  {
    path: '',
    children: [
      {
        path: PROTOKOLL_ROUTE.route,
        loadChildren: () =>
          import('@dv/sachbearbeitung-app/feature/infos-protokoll').then(
            (m) => m.sachbearbeitungAppFeatureInfosProtokollRoutes,
          ),
      },
      {
        path: NOTIZEN_ROUTE.route + '/:id',
        component: SharedUiCommingSoonComponent,
      },
      {
        path: BUCHHALTUNG_ROUTE.route + '/:id',
        component: SharedUiCommingSoonComponent,
      },
      {
        path: DARLEHEN_ROUTE.route + '/:id',
        component: SharedUiCommingSoonComponent,
      },
      {
        path: BESCHWERDEN_ROUTE.route + '/:id',
        component: SharedUiCommingSoonComponent,
      },
      {
        path: ':id',
        pathMatch: 'prefix',
        redirectTo: PROTOKOLL_ROUTE.route + '/:id',
      },
      {
        path: ':id/tranche/:trancheId',
        redirectTo: PERSON.route + '/:id/tranche/:trancheId',
      },
    ],
  },
];
