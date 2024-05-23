import { Route } from '@angular/router';

import {
  OPTION_AUSBILDUNGSSTAETTE,
  OPTION_BUCHSTABEN_ZUTEILUNG,
  OPTION_GESUCHSPERIODEN,
} from '@dv/sachbearbeitung-app/model/administration';

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
    title: OPTION_BUCHSTABEN_ZUTEILUNG.titleTranslationKey,
    loadChildren: () =>
      import(
        '@dv/sachbearbeitung-app/feature/administration-buchstaben-zuteilung'
      ).then(
        (m) =>
          m.sachbearbeitungAppFeatureAdministrationBuchstabenZuteilungRoutes,
      ),
  },
  {
    path: OPTION_GESUCHSPERIODEN.route,
    title: OPTION_GESUCHSPERIODEN.titleTranslationKey,
    loadChildren: () =>
      import(
        '@dv/sachbearbeitung-app/feature/administration-gesuchsperiode'
      ).then((m) => m.sachbearbeitungAppFeatureGesuchsperiodeRoutes),
  },
  {
    path: 'benutzerverwaltung',
    loadChildren: () =>
      import(
        '@dv/sachbearbeitung-app/feature/administration-benutzerverwaltung'
      ).then(
        (m) =>
          m.sachbearbeitungAppFeatureAdministrationBenutzerverwaltungRoutes,
      ),
  },
  {
    path: '',
    pathMatch: 'prefix',
    redirectTo: OPTION_AUSBILDUNGSSTAETTE.route,
  },
];
