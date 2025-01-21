import { Route } from '@angular/router';

import {
  OPTION_AUSBILDUNGSSTAETTE,
  OPTION_BUCHSTABEN_ZUTEILUNG,
  OPTION_EU_EFTA_LAENDER,
  OPTION_GESUCHSPERIODEN,
  OPTION_SOZIALDIENST,
  OPTION_SOZIALDIENST_BENUTZER,
} from '@dv/sachbearbeitung-app/model/administration';
import { hasRoles } from '@dv/shared/pattern/status-guard';

export const sachbearbeitungAppFeatureAdministrationRoutes: Route[] = [
  {
    path: OPTION_SOZIALDIENST.route,
    canActivate: [hasRoles(['Admin'])],
    loadChildren: () =>
      import(
        '@dv/sachbearbeitung-app/feature/administration-sozialdienst'
      ).then(
        (m) => m.sachbearbeitungAppFeatureAdministrationSozialdienstRoutes,
      ),
  },
  {
    path: OPTION_SOZIALDIENST_BENUTZER.route,
    canActivate: [hasRoles(['Sozialdienst-Admin'], '/unauthorized')],
    loadChildren: () =>
      import(
        '@dv/sachbearbeitung-app/feature/administration-sozialdienst-benutzer'
      ).then(
        (m) =>
          m.sachbearbeitungAppFeatureAdministrationSozialdienstBenutzerRoutes,
      ),
  },
  {
    path: OPTION_AUSBILDUNGSSTAETTE.route,
    canActivate: [
      hasRoles(
        ['Admin'],
        '/administration/' + OPTION_SOZIALDIENST_BENUTZER.route,
      ),
    ],
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
    canActivate: [hasRoles(['Admin'])],
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
    canActivate: [hasRoles(['Admin'])],
    title: OPTION_GESUCHSPERIODEN.titleTranslationKey,
    loadChildren: () =>
      import(
        '@dv/sachbearbeitung-app/feature/administration-gesuchsperiode'
      ).then((m) => m.sachbearbeitungAppFeatureGesuchsperiodeRoutes),
  },
  {
    path: OPTION_EU_EFTA_LAENDER.route,
    canActivate: [hasRoles(['Admin'])],
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
    canActivate: [hasRoles(['Admin'])],
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
