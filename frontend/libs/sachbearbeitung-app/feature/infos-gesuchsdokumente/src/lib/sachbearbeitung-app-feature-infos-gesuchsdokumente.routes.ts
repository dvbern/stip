import { Route } from '@angular/router';

import { InfosGesuchsdokumenteStore } from '@dv/sachbearbeitung-app/data-access/infos-gesuchsdokumente';
import { INFO_ADMIN_DOKUMENTE_ROUTE } from '@dv/sachbearbeitung-app/model/infos';

import { SachbearbeitungAppFeatureInfosAdminComponent } from './sachbearbeitung-app-feature-infos-gesuchsdokumente/sachbearbeitung-app-feature-infos-gesuchsdokumente.component';

export const sachbearbeitungAppFeatureInfosGesuchsdokumenteRoutes: Route[] = [
  {
    path: ':id',
    pathMatch: 'prefix',
    data: { option: INFO_ADMIN_DOKUMENTE_ROUTE },
    providers: [InfosGesuchsdokumenteStore],
    component: SachbearbeitungAppFeatureInfosAdminComponent,
  },
];
