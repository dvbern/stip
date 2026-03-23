import { Route } from '@angular/router';

import {
  BESCHWERDEN_ROUTE,
  BUCHHALTUNG_ROUTE,
  DARLEHEN_ROUTE,
  INFO_ADMIN_DOKUMENTE_ROUTE,
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
        path: INFO_ADMIN_DOKUMENTE_ROUTE.route,
        loadChildren: () =>
          import('@dv/sachbearbeitung-app/feature/infos-gesuchsdokumente').then(
            (m) => m.sachbearbeitungAppFeatureInfosGesuchsdokumenteRoutes,
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
        path: BESCHWERDEN_ROUTE.route,
        loadChildren: () =>
          import('@dv/sachbearbeitung-app/feature/infos-beschwerde').then(
            (m) => m.sachbearbeitungAppFeatureInfosBeschwerdeRoutes,
          ),
      },
      {
        path: BUCHHALTUNG_ROUTE.route + '/:gesuchId',
        loadChildren: () =>
          import('@dv/sachbearbeitung-app/feature/infos-buchhaltung').then(
            (m) => m.sachbearbeitungAppFeatureInfosBuchhaltungRoutes,
          ),
      },
      {
        path: DARLEHEN_ROUTE.route + '/:gesuchId',
        component: SharedUiCommingSoonComponent,
      },
      {
        path: ':gesuchId',
        pathMatch: 'prefix',
        redirectTo: PROTOKOLL_ROUTE.route + '/:gesuchId',
      },
      {
        path: ':gesuchId/tranche/:trancheId',
        redirectTo: PROTOKOLL_ROUTE.route + '/:gesuchId/tranche/:trancheId',
      },
    ],
  },
];
