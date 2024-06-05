import { Route } from '@angular/router';

import { BenutzerverwaltungStore } from '@dv/sachbearbeitung-app/data-access/benutzerverwaltung';
import {
  CHILD_OPTION_BENUTZER_ERSTELLEN as CHILD_OPTION_BENUTZER_CREATE,
  CHILD_OPTION_BENUTZER_OVERVIEW,
} from '@dv/sachbearbeitung-app/model/administration';

import { BenutzerErstellungComponent } from './benutzer-erstellung/benutzer-erstellung.component';
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
            option: CHILD_OPTION_BENUTZER_OVERVIEW(
              'sachbearbeitung-app.admin.benutzerverwaltung.route.overview',
            ),
          },
          title: 'sachbearbeitung-app.admin.benutzerverwaltung.route.overview',
          path: 'overview',
          component: BenutzerOverviewComponent,
        },
        {
          data: {
            option: CHILD_OPTION_BENUTZER_CREATE(
              'sachbearbeitung-app.admin.benutzerverwaltung.route.create',
            ),
          },
          title: 'sachbearbeitung-app.admin.benutzerverwaltung.route.create',
          path: 'erstellen',
          component: BenutzerErstellungComponent,
        },
        {
          path: '',
          pathMatch: 'full',
          redirectTo: 'overview',
        },
      ],
    },
  ];
