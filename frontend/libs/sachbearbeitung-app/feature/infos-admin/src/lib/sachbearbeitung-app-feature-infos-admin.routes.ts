import { Route } from '@angular/router';

import { AusbildungAdminStore } from '@dv/sachbearbeitung-app/data-access/ausbildung-admin';
import { INFO_ADMIN_ROUTE } from '@dv/sachbearbeitung-app/model/infos';

import { SachbearbeitungAppFeatureInfosAdminComponent } from './sachbearbeitung-app-feature-infos-admin/sachbearbeitung-app-feature-infos-admin.component';

export const sachbearbeitungAppFeatureInfosAdminRoutes: Route[] = [
  {
    path: ':id',
    pathMatch: 'prefix',
    data: { option: INFO_ADMIN_ROUTE },
    providers: [],
    component: SachbearbeitungAppFeatureInfosAdminComponent,
    children: [
      {
        path: 'ausbildung-abbrechen',
        loadComponent: () =>
          import('./components/ausbildung-abbrechen.component').then(
            (m) => m.AusbildungAbbrechenComponent,
          ),
      },
      {
        path: 'ausbildung-unterbrechen',
        providers: [AusbildungAdminStore],
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
        redirectTo: 'ausbildung-abschliessen',
      },
    ],
  },
];
