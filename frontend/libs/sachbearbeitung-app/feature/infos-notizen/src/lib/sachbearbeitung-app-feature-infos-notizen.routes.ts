import { Route } from '@angular/router';

import { NotizStore } from '@dv/sachbearbeitung-app/data-access/notiz';
import { idAndTrancheIdRoutes } from '@dv/shared/util/gesuch';

import { SachbearbeitungAppFeatureInfosNotizenComponent } from './sachbearbeitung-app-feature-infos-notizen/sachbearbeitung-app-feature-infos-notizen.component';

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
      ...idAndTrancheIdRoutes({
        pathMatch: 'prefix',
        component: SachbearbeitungAppFeatureInfosNotizenComponent,
      }),
    ],
  },
];
