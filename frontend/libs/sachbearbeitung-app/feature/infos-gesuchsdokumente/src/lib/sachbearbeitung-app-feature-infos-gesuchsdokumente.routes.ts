import { Route } from '@angular/router';

import { InfosGesuchsdokumenteStore } from '@dv/sachbearbeitung-app/data-access/infos-gesuchsdokumente';
import { INFO_ADMIN_DOKUMENTE_ROUTE } from '@dv/sachbearbeitung-app/model/infos';

import { SachbearbeitungAppFeatureInfosGesuchsDokumenteComponent } from './sachbearbeitung-app-feature-infos-gesuchsdokumente/sachbearbeitung-app-feature-infos-gesuchsdokumente.component';

export const sachbearbeitungAppFeatureInfosGesuchsdokumenteRoutes: Route[] = [
  {
    path: ':id',
    pathMatch: 'prefix',
    data: { option: INFO_ADMIN_DOKUMENTE_ROUTE },
    providers: [InfosGesuchsdokumenteStore],
    component: SachbearbeitungAppFeatureInfosGesuchsDokumenteComponent,
    children: [
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'stipendien',
      },
      {
        path: 'stipendien',
        loadComponent: () =>
          import(
            './components/stipenedien-dokumente/stipendien-dokumente.component'
          ).then((m) => m.StipendienDokumenteComponent),
      },
      {
        path: 'darlehen',
        loadComponent: () =>
          import(
            './components/darlehen-dokumente/darlehen-dokumente.component'
          ).then((m) => m.DarlehenDokumenteComponent),
      },
      {
        path: 'datenschutzbriefe',
        loadComponent: () =>
          import(
            './components/datenschutzbriefe-dokumente/datenschutzbriefe-dokumente.component'
          ).then((m) => m.DatenschutzbriefeDokumenteComponent),
      },
    ],
  },
];
