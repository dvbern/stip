import { Route } from '@angular/router';

import {
  CHILD_OPTION_SOZIALDIENST,
  OPTION_SOZIALDIENST,
} from '@dv/sachbearbeitung-app/model/administration';
import { SozialdienstStore } from '@dv/shared/data-access/sozialdienst';

import { SozialdienstDetailComponent } from './sozialdienst-detail/sozialdienst-detail.component';
import { SozialdienstOverviewComponent } from './sozialdienst-overview/sozialdienst-overview.component';

export const sachbearbeitungAppFeatureAdministrationSozialdienstRoutes: Route[] =
  [
    {
      path: '',
      pathMatch: 'prefix',
      providers: [SozialdienstStore],
      children: [
        {
          data: {
            option: CHILD_OPTION_SOZIALDIENST(
              'sachbearbeitung-app.admin.sozialdienst.route.create',
            ),
          },
          title: 'sachbearbeitung-app.admin.sozialdienst.route.create',
          path: 'create',
          component: SozialdienstDetailComponent,
        },
        {
          data: {
            option: CHILD_OPTION_SOZIALDIENST(
              'sachbearbeitung-app.admin.sozialdienst.route.edit',
            ),
          },
          path: 'edit/:id',
          component: SozialdienstDetailComponent,
        },
        {
          path: '',
          data: { option: OPTION_SOZIALDIENST },
          component: SozialdienstOverviewComponent,
          title: 'sachbearbeitung-app.admin.sozialdienst.route.overview',
        },
      ],
    },
  ];
