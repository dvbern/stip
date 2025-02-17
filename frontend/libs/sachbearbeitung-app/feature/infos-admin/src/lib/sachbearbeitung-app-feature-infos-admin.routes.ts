import { Route } from '@angular/router';

import { INFO_ADMIN_ROUTE } from '@dv/sachbearbeitung-app/model/infos';
import { idAndTrancheIdRoutes } from '@dv/shared/util/gesuch';

import { SachbearbeitungAppFeatureInfosAdminComponent } from './sachbearbeitung-app-feature-infos-admin/sachbearbeitung-app-feature-infos-admin.component';

export const sachbearbeitungAppFeatureInfosAdminRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [],
    children: [
      // @scph brauchen wir hier wirklich die idAndTrancheIdRoutes?
      ...idAndTrancheIdRoutes({
        data: { option: INFO_ADMIN_ROUTE },
        pathMatch: 'prefix',
        component: SachbearbeitungAppFeatureInfosAdminComponent,
      }),
    ],
  },
];
