import { Route } from '@angular/router';

import {
  OPTION_SOZIALDIENST_BENUTZER,
  OPTION_SOZIALDIENST_BENUTZER_DETAIL,
} from '@dv/sachbearbeitung-app/model/administration';
import { SozialdienstStore } from '@dv/shared/data-access/sozialdienst';

import { SozialdienstMitarbeiterDetailComponent } from './detail/sozialdienst-mitarbeiter-detail.component';
import { SozialdienstMitarbeiterOverviewComponent } from './overview/sozialdienst-mitarbeiter-overview.component';

export const sachbearbeitungAppFeatureAdministrationSozialdienstBenutzerRoutes: Route[] =
  [
    {
      path: '',
      pathMatch: 'prefix',
      providers: [SozialdienstStore],
      data: { option: OPTION_SOZIALDIENST_BENUTZER },
      children: [
        {
          path: '',
          component: SozialdienstMitarbeiterOverviewComponent,
        },
        {
          path: 'edit/:id',
          data: {
            option: OPTION_SOZIALDIENST_BENUTZER_DETAIL,
          },
          component: SozialdienstMitarbeiterDetailComponent,
        },
        {
          path: 'create',
          data: {
            option: OPTION_SOZIALDIENST_BENUTZER_DETAIL,
          },
          component: SozialdienstMitarbeiterDetailComponent,
        },
      ],
    },
  ];
