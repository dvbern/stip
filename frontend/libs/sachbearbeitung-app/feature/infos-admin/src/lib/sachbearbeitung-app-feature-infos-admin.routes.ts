import { Route } from '@angular/router';

import { AusbildungAdminStore } from '@dv/sachbearbeitung-app/data-access/ausbildung-admin';
import { SachbearbeiterDokumentsStore } from '@dv/sachbearbeitung-app/data-access/sachbearbeiter-dokuments';
import { INFO_ADMIN_ROUTE } from '@dv/sachbearbeitung-app/model/infos';
import { AusbildungStore } from '@dv/shared/data-access/ausbildung';

import { SachbearbeitungAppFeatureInfosAdminComponent } from './sachbearbeitung-app-feature-infos-admin/sachbearbeitung-app-feature-infos-admin.component';

export const sachbearbeitungAppFeatureInfosAdminRoutes: Route[] = [
  {
    path: ':gesuchId',
    pathMatch: 'prefix',
    data: { option: INFO_ADMIN_ROUTE },
    providers: [AusbildungStore],
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
        loadChildren: () =>
          import('@dv/shared/feature/ausbildung-unterbrechung').then(
            (m) => m.sharedFeatureAusbildungUnterbrechungRoutes,
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
        path: 'sachbearbeiter-dokumente',
        providers: [SachbearbeiterDokumentsStore],
        loadComponent: () =>
          import('./components/sachbearbeiter-dokumente.component').then(
            (m) => m.SachbearbeiterGesuchDokumentComponent,
          ),
      },
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'ausbildung-unterbrechen',
      },
    ],
  },
];
