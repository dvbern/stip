import { Route } from '@angular/router';

import { BenutzerverwaltungStore } from '@dv/sachbearbeitung-app/data-access/benutzerverwaltung';
import {
  CHILD_OPTION_BENUTZER_ERSTELLEN,
  OPTION_BENUTZERVERWALTUNG,
} from '@dv/sachbearbeitung-app/model/administration';
import { SharedUiCommingSoonComponent } from '@dv/shared/ui/comming-soon';

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
            option: CHILD_OPTION_BENUTZER_ERSTELLEN(
              'sachbearbeitung-app.admin.benutzerverwaltung.route.create',
            ),
          },
          title: 'sachbearbeitung-app.admin.benutzerverwaltung.route.create',
          path: 'create',
          component: BenutzerErstellungComponent,
        },
        {
          data: { option: OPTION_BENUTZERVERWALTUNG },
          path: 'edit/:id',
          loadComponent: () => SharedUiCommingSoonComponent,
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
