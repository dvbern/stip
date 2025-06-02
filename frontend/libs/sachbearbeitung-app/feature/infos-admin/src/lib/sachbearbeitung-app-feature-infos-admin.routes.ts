import { Route } from '@angular/router';

import { InfosAdminStore } from '@dv/sachbearbeitung-app/data-access/infos-admin';
import { INFO_ADMIN_ROUTE } from '@dv/sachbearbeitung-app/model/infos';

import { SachbearbeitungAppFeatureInfosAdminComponent } from './sachbearbeitung-app-feature-infos-admin/sachbearbeitung-app-feature-infos-admin.component';

export const sachbearbeitungAppFeatureInfosAdminRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [InfosAdminStore],
    children: [
      {
        path: ':id',
        data: { option: INFO_ADMIN_ROUTE },
        pathMatch: 'prefix',
        component: SachbearbeitungAppFeatureInfosAdminComponent,
      },
    ],
  },
];
