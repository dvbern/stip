import { Route } from '@angular/router';

import { NotizStore } from '@dv/sachbearbeitung-app/data-access/notiz';
import {
  NOTIZEN_ROUTE,
  NOTIZEN_ROUTE_CREATE,
  NOTIZEN_ROUTE_DETAIL,
} from '@dv/sachbearbeitung-app/model/infos';
import { idAndTrancheIdRoutes } from '@dv/shared/util/gesuch';

import { SachbearbeitungAppFeatureInfosNotizenComponent } from './sachbearbeitung-app-feature-infos-notizen/sachbearbeitung-app-feature-infos-notizen.component';
import { SachbearbeitungAppFeatureInfosNotizenDetailComponent } from './sachbearbeitung-app-feature-infos-notizen-detail/sachbearbeitung-app-feature-infos-notizen-detail.component';

export const sachbearbeitungAppFeatureInfosNotizenRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [
      {
        provide: NotizStore,
      },
    ],
    children: [
      {
        path: NOTIZEN_ROUTE_CREATE.route,
        data: {
          option: NOTIZEN_ROUTE_CREATE,
        },
        pathMatch: 'prefix',
        component: SachbearbeitungAppFeatureInfosNotizenDetailComponent,
      },
      {
        path: NOTIZEN_ROUTE_DETAIL.route,
        data: {
          option: NOTIZEN_ROUTE_DETAIL,
        },
        pathMatch: 'prefix',
        component: SachbearbeitungAppFeatureInfosNotizenDetailComponent,
      },
      ...idAndTrancheIdRoutes({
        data: {
          option: NOTIZEN_ROUTE,
        },
        pathMatch: 'prefix',
        component: SachbearbeitungAppFeatureInfosNotizenComponent,
      }),
    ],
  },
];
