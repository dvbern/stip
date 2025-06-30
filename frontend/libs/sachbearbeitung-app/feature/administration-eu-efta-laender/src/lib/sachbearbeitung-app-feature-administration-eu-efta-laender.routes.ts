import { Route } from '@angular/router';

import { OPTION_EU_EFTA_LAENDER } from '@dv/sachbearbeitung-app/model/administration';

import { SachbearbeitungAppFeatureAdministrationEuEftaLaenderComponent } from './sachbearbeitung-app-feature-administration-eu-efta-laender/sachbearbeitung-app-feature-administration-eu-efta-laender.component';

export const sachbearbeitungAppFeatureAdministrationEuEftaLaenderRoutes: Route[] =
  [
    {
      path: '',
      pathMatch: 'prefix',
      children: [
        {
          path: '',
          data: {
            option: OPTION_EU_EFTA_LAENDER,
          },
          component:
            SachbearbeitungAppFeatureAdministrationEuEftaLaenderComponent,
        },
      ],
    },
  ];
