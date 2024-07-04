import { Route } from '@angular/router';

import { BenutzerverwaltungStore } from '@dv/sachbearbeitung-app/data-access/benutzerverwaltung';
import {
  CHILD_OPTION_BENUTZER_ERSTELLEN,
  OPTION_BENUTZERVERWALTUNG,
} from '@dv/sachbearbeitung-app/model/administration';

import { BenutzeDetailComponent } from './benutzer-erstellung/benutzer-detail.component';
import { BenutzerOverviewComponent } from './benutzer-overview/benutzer-overview.component';

export const sachbearbeitungAppFeatureAdministrationBenutzerverwaltungRoutes: Route[] =
  [
    {
      path: '',
      pathMatch: 'prefix',
      providers: [BenutzerverwaltungStore],
      children: [
        {
          data: {
            option: CHILD_OPTION_BENUTZER_ERSTELLEN(
              'sachbearbeitung-app.admin.benutzerverwaltung.route.create',
            ),
          },
          title: 'sachbearbeitung-app.admin.benutzerverwaltung.route.create',
          path: 'create',
          component: BenutzeDetailComponent,
        },
        {
          data: {
            option: CHILD_OPTION_BENUTZER_ERSTELLEN(
              'sachbearbeitung-app.admin.benutzerverwaltung.route.edit',
            ),
          },
          path: 'edit/:id',
          component: BenutzeDetailComponent,
        },
        {
          data: { option: OPTION_BENUTZERVERWALTUNG },
          title: 'sachbearbeitung-app.admin.benutzerverwaltung.route.overview',
          path: '',
          component: BenutzerOverviewComponent,
        },
      ],
    },
  ];
