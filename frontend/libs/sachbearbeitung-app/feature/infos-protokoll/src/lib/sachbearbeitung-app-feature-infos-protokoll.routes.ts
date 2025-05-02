import { Route } from '@angular/router';

import { StatusprotokollStore } from '@dv/sachbearbeitung-app/data-access/statusprotokoll';
import { PROTOKOLL_ROUTE } from '@dv/sachbearbeitung-app/model/infos';

import { SachbearbeitungAppFeatureInfosProtokollComponent } from './sachbearbeitung-app-feature-infos-protokoll/sachbearbeitung-app-feature-infos-protokoll.component';

export const sachbearbeitungAppFeatureInfosProtokollRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [
      {
        provide: StatusprotokollStore,
      },
    ],
    children: [
      {
        path: ':id',
        data: {
          option: PROTOKOLL_ROUTE,
        },
        pathMatch: 'prefix',
        component: SachbearbeitungAppFeatureInfosProtokollComponent,
      },
    ],
  },
];
