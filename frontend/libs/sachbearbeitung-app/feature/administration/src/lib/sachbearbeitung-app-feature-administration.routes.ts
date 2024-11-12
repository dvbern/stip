import { Route } from '@angular/router';

import {
  OPTION_AUSBILDUNGSSTAETTE,
  OPTION_BUCHSTABEN_ZUTEILUNG,
  OPTION_EU_EFTA_LAENDER,
  OPTION_GESUCHSPERIODEN,
  OPTION_SOZIALDIENST,
} from '@dv/sachbearbeitung-app/model/administration';

export const sachbearbeitungAppFeatureAdministrationRoutes: Route[] = [
  {
    path: OPTION_SOZIALDIENST.route,
    loadChildren: () =>
      import(
        '@dv/sachbearbeitung-app/feature/administration-sozialdienst'
      ).then(
        (m) => m.sachbearbeitungAppFeatureAdministrationSozialdienstRoutes,
      ),
  },
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
    path: OPTION_EU_EFTA_LAENDER.route,
    title: OPTION_EU_EFTA_LAENDER.titleTranslationKey,
    loadChildren: () =>
      import(
        '@dv/sachbearbeitung-app/feature/administration-eu-efta-laender'
      ).then(
        (m) => m.sachbearbeitungAppFeatureAdministrationEuEftaLaenderRoutes,
      ),
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
