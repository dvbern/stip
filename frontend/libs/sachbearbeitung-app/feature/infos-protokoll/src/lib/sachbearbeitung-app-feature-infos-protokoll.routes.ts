import { Route } from '@angular/router';

import { StatusprotokollStore } from '@dv/sachbearbeitung-app/data-access/statusprotokoll';
import { idAndTrancheIdRoutes } from '@dv/shared/util/gesuch';

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
      ...idAndTrancheIdRoutes({
        pathMatch: 'prefix',
        component: SachbearbeitungAppFeatureInfosProtokollComponent,
      }),
    ],
  },
];