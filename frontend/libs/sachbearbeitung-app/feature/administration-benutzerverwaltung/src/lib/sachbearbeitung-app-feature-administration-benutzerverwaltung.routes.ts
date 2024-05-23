import { Route } from '@angular/router';

import { BenutzerverwaltungStore } from '@dv/sachbearbeitung-app/data-access/benutzerverwaltung';

import { SachbearbeitungAppFeatureAdministrationBenutzerverwaltungComponent } from './sachbearbeitung-app-feature-administration-benutzerverwaltung/sachbearbeitung-app-feature-administration-benutzerverwaltung.component';

export const sachbearbeitungAppFeatureAdministrationBenutzerverwaltungRoutes: Route[] =
  [
    {
      path: '',
      pathMatch: 'prefix',
      providers: [BenutzerverwaltungStore],
      children: [
        {
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
