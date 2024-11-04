import { Route } from '@angular/router';

import { NotizStore } from '@dv/sachbearbeitung-app/data-access/notiz';
import { NOTIZEN_ROUTE } from '@dv/sachbearbeitung-app/model/infos';

import { SachbearbeitungAppFeatureInfosNotizenComponent } from './sachbearbeitung-app-feature-infos-notizen/sachbearbeitung-app-feature-infos-notizen.component';

export const sachbearbeitungAppFeatureInfosNotizenRoutes: Route[] = [
  {
    path: ':id',
    pathMatch: 'prefix',
    providers: [
      {
        provide: NotizStore,
      },
    ],
    children: [
      {
        path: '',
        data: {
          option: NOTIZEN_ROUTE,
        },
        pathMatch: 'prefix',
        component: SachbearbeitungAppFeatureInfosNotizenComponent,
      },
    ],
  },
];
