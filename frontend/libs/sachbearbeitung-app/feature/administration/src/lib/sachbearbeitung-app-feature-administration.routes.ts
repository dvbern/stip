import { Route } from '@angular/router';

import {
  OPTION_AUSBILDUNGSSTAETTE,
  OPTION_BUCHSTABEN_ZUTEILUNG,
  OPTION_MORE,
} from '@dv/sachbearbeitung-app/model/administration';
import { SharedUiCommingSoonComponent } from '@dv/shared/ui/comming-soon';

export const sachbearbeitungAppFeatureAdministrationRoutes: Route[] = [
  {
    path: OPTION_AUSBILDUNGSSTAETTE.route,
    loadChildren: () =>
      import(
        '@dv/sachbearbeitung-app/feature/administration-ausbildungsstaette'
      ).then(
        (m) =>
          m.sachbearbeitungAppFeatureAdministrationAusbildungsstaetteRoutes,
      ),
  },
  {
    path: OPTION_BUCHSTABEN_ZUTEILUNG.route,
    loadChildren: () =>
      import(
        '@dv/sachbearbeitung-app/feature/administration-buchstaben-zuteilung'
      ).then(
        (m) =>
          m.sachbearbeitungAppFeatureAdministrationBuchstabenZuteilungRoutes,
      ),
  },
  {
    path: OPTION_MORE.route,
    loadComponent: () => SharedUiCommingSoonComponent,
    data: { option: OPTION_MORE },
  },
  {
    path: '',
    pathMatch: 'prefix',
    redirectTo: OPTION_AUSBILDUNGSSTAETTE.route,
  },
];
