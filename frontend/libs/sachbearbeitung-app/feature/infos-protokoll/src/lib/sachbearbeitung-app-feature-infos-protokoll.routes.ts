import { Route } from '@angular/router';

import { StatusprotokollStore } from '@dv/sachbearbeitung-app/data-access/statusprotokoll';
import { PROTOKOLL } from '@dv/shared/model/gesuch-form';
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
        data: {
          option: PROTOKOLL,
        },
        pathMatch: 'prefix',
        component: SachbearbeitungAppFeatureInfosProtokollComponent,
      }),
    ],
  },
];
