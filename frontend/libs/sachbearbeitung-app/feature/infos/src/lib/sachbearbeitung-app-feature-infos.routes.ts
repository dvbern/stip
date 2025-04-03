import { Route } from '@angular/router';

import {
  BESCHWERDEN_ROUTE,
  BUCHHALTUNG_ROUTE,
  DARLEHEN_ROUTE,
  INFO_ADMIN_ROUTE,
  NOTIZEN_ROUTE,
  PROTOKOLL_ROUTE,
} from '@dv/sachbearbeitung-app/model/infos';
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
        path: NOTIZEN_ROUTE.route,
        loadChildren: () =>
          import('@dv/sachbearbeitung-app/feature/infos-notizen').then(
            (m) => m.sachbearbeitungAppFeatureInfosNotizenRoutes,
          ),
      },
      {
        path: INFO_ADMIN_ROUTE.route,
        loadChildren: () =>
          import('@dv/sachbearbeitung-app/feature/infos-admin').then(
            (m) => m.sachbearbeitungAppFeatureInfosAdminRoutes,
          ),
      },
      {
        path: BESCHWERDEN_ROUTE.route + '/:id',
        loadChildren: () =>
          import('@dv/sachbearbeitung-app/feature/infos-beschwerde').then(
            (m) => m.sachbearbeitungAppFeatureInfosBeschwerdeRoutes,
          ),
      },
      {
        path: BUCHHALTUNG_ROUTE.route + '/:id',
        loadChildren: () =>
          import('@dv/sachbearbeitung-app/feature/infos-buchhaltung').then(
            (m) => m.sachbearbeitungAppFeatureInfosBuchhaltungRoutes,
          ),
      },
      {
        path: DARLEHEN_ROUTE.route + '/:id',
        component: SharedUiCommingSoonComponent,
      },
      {
        path: ':id',
        pathMatch: 'prefix',
        redirectTo: PROTOKOLL_ROUTE.route + '/:id',
      },
      {
        path: ':id/tranche/:trancheId',
        redirectTo: PROTOKOLL_ROUTE.route + '/:id/tranche/:trancheId',
      },
    ],
  },
];
