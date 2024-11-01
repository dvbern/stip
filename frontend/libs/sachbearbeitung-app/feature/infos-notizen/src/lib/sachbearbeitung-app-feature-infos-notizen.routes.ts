import { Route } from '@angular/router';

import { NotizStore } from '@dv/sachbearbeitung-app/data-access/notiz';
import {
  NOTIZEN_ROUTE,
  NOTIZEN_ROUTE_DETAIL,
} from '@dv/sachbearbeitung-app/model/infos';

import { SachbearbeitungAppFeatureInfosNotizenComponent } from './sachbearbeitung-app-feature-infos-notizen/sachbearbeitung-app-feature-infos-notizen.component';
import { SachbearbeitungAppFeatureInfosNotizenDetailComponent } from './sachbearbeitung-app-feature-infos-notizen-detail/sachbearbeitung-app-feature-infos-notizen-detail.component';

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
        path: NOTIZEN_ROUTE_DETAIL.route,
        data: {
          option: NOTIZEN_ROUTE_DETAIL,
        },
        pathMatch: 'prefix',
        component: SachbearbeitungAppFeatureInfosNotizenDetailComponent,
      },
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
