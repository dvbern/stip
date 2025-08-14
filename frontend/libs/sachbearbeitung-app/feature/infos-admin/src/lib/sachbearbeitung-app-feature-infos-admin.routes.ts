import { Route } from '@angular/router';

import { InfosAdminStore } from '@dv/sachbearbeitung-app/data-access/infos-admin';
import { INFO_ADMIN_ROUTE } from '@dv/sachbearbeitung-app/model/infos';

import { SachbearbeitungAppFeatureInfosAdminComponent } from './sachbearbeitung-app-feature-infos-admin/sachbearbeitung-app-feature-infos-admin.component';

export const sachbearbeitungAppFeatureInfosAdminRoutes: Route[] = [
  {
    path: ':id',
    pathMatch: 'prefix',
    data: { option: INFO_ADMIN_ROUTE },
    providers: [InfosAdminStore],
    component: SachbearbeitungAppFeatureInfosAdminComponent,
    children: [
      {
        path: 'verfuegung',
        loadComponent: () =>
          import('./components/verfuegung.component').then(
            (m) => m.VerfuegungComponent,
          ),
      },
      {
        path: 'ausbildung-abbrechen',
        loadComponent: () =>
          import('./components/ausbildung-abbrechen.component').then(
            (m) => m.AusbildungAbbrechenComponent,
          ),
      },
      {
        path: 'ausbildung-unterbrechen',
        loadComponent: () =>
          import('./components/ausbildung-unterbrechen.component').then(
            (m) => m.AusbildungUnterbrechenComponent,
          ),
      },
      {
        path: 'ausbildung-abschliessen',
        loadComponent: () =>
          import('./components/ausbildung-abschliessen.component').then(
            (m) => m.AusbildungAbschliessenComponent,
          ),
      },
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'verfuegung',
      },
    ],
  },
];
