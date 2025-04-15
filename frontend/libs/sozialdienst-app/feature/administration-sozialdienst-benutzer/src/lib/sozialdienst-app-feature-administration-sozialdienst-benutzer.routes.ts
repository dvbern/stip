import { Route } from '@angular/router';

import { SozialdienstStore } from '@dv/shared/data-access/sozialdienst';
import {
  OPTION_SOZIALDIENST_BENUTZER,
  OPTION_SOZIALDIENST_BENUTZER_DETAIL,
} from '@dv/sozialdienst-app/model/administration';

import { SozialdienstMitarbeiterDetailComponent } from './detail/sozialdienst-mitarbeiter-detail.component';
import { SozialdienstMitarbeiterOverviewComponent } from './overview/sozialdienst-mitarbeiter-overview.component';

export const sozialdienstAppFeatureAdministrationSozialdienstBenutzerRoutes: Route[] =
  [
    {
      path: '',
      pathMatch: 'prefix',
      providers: [SozialdienstStore],
      data: { option: OPTION_SOZIALDIENST_BENUTZER },
      title: 'sozialdienst-app.admin.sozialdienstBenutzer.route.overview',
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
