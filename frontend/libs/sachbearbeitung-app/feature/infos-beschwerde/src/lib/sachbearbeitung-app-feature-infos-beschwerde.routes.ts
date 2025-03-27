import { Route } from '@angular/router';

import { BESCHWERDEN_ROUTE } from '@dv/sachbearbeitung-app/model/infos';

import { SachbearbeitungAppFeatureInfosBeschwerdeComponent } from './sachbearbeitung-app-feature-infos-beschwerde/sachbearbeitung-app-feature-infos-beschwerde.component';

export const sachbearbeitungAppFeatureInfosBeschwerdeRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [],
    children: [
      {
        path: '',
        data: { option: BESCHWERDEN_ROUTE },
        component: SachbearbeitungAppFeatureInfosBeschwerdeComponent,
      },
    ],
  },
];
