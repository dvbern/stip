import { Route } from '@angular/router';

import { SozialdienstStore } from '@dv/shared/data-access/sozialdienst';

import { SozialdienstMitarbeiterDetailComponent } from './detail/sozialdienst-mitarbeiter-detail.component';
import { SozialdienstMitarbeiterOverviewComponent } from './overview/sozialdienst-mitarbeiter-overview.component';

export const sachbearbeitungAppFeatureAdministrationSozialdienstBenutzerRoutes: Route[] =
  [
    {
      path: '',
      pathMatch: 'prefix',
      providers: [SozialdienstStore],
      children: [
        {
          path: '',
          component: SozialdienstMitarbeiterOverviewComponent,
        },
        {
          path: ':id',
          component: SozialdienstMitarbeiterDetailComponent,
        },
      ],
    },
  ];
