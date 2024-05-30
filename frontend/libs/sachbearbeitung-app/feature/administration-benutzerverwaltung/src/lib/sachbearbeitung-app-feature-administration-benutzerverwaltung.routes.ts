import { Route } from '@angular/router';

import { BenutzerverwaltungStore } from '@dv/sachbearbeitung-app/data-access/benutzerverwaltung';
import { CHILD_OPTION_BENUTZER_ERSTELLEN } from '@dv/sachbearbeitung-app/model/administration';

import { SachbearbeitungAppFeatureAdministrationBenutzerverwaltungComponent } from './sachbearbeitung-app-feature-administration-benutzerverwaltung/sachbearbeitung-app-feature-administration-benutzerverwaltung.component';

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
          path: 'erstellen',
          component:
            SachbearbeitungAppFeatureAdministrationBenutzerverwaltungComponent,
        },
        {
          path: '',
          pathMatch: 'full',
          redirectTo: 'erstellen',
        },
      ],
    },
  ];
