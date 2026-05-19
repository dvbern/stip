import { Route } from '@angular/router';

import {
  OPTION_AUSBILDUNGSSTAETTE,
  OPTION_BENUTZERVERWALTUNG,
  OPTION_BFS_STATISTIK,
  OPTION_BUCHSTABEN_ZUTEILUNG,
  OPTION_EU_EFTA_LAENDER,
  OPTION_GESUCHSPERIODEN,
  OPTION_SOZIALDIENST,
} from '@dv/sachbearbeitung-app/model/administration';
import { hasRoles } from '@dv/shared/pattern/status-guard';

export const sachbearbeitungAppFeatureAdministrationRoutes: Route[] = [
  {
    path: OPTION_SOZIALDIENST.route,
    canActivate: [hasRoles(OPTION_SOZIALDIENST.allowedRoles)],
    title: OPTION_SOZIALDIENST.titleTranslationKey,
    loadChildren: () =>
      import('@dv/sachbearbeitung-app/feature/administration-sozialdienst').then(
        (m) => m.sachbearbeitungAppFeatureAdministrationSozialdienstRoutes,
      ),
  },
  {
    path: OPTION_AUSBILDUNGSSTAETTE.route,
    canActivate: [hasRoles(OPTION_AUSBILDUNGSSTAETTE.allowedRoles)],
    title: OPTION_AUSBILDUNGSSTAETTE.titleTranslationKey,
    loadChildren: () =>
      import('@dv/sachbearbeitung-app/feature/administration-ausbildungsstaette').then(
        (m) =>
          m.sachbearbeitungAppFeatureAdministrationAusbildungsstaetteRoutes,
      ),
  },
  {
    path: OPTION_BFS_STATISTIK.route,
    canActivate: [hasRoles(OPTION_BFS_STATISTIK.allowedRoles)],
    title: OPTION_BFS_STATISTIK.titleTranslationKey,
    loadChildren: () =>
      import('@dv/sachbearbeitung-app/feature/administration-bfs-statistik').then(
        (m) => m.sachbearbeitungAppFeatureAdministrationBfsStatistikRoutes,
      ),
  },
  {
    path: OPTION_BUCHSTABEN_ZUTEILUNG.route,
    canActivate: [hasRoles(OPTION_BUCHSTABEN_ZUTEILUNG.allowedRoles)],
    title: OPTION_BUCHSTABEN_ZUTEILUNG.titleTranslationKey,
    loadChildren: () =>
      import('@dv/sachbearbeitung-app/feature/administration-buchstaben-zuteilung').then(
        (m) =>
          m.sachbearbeitungAppFeatureAdministrationBuchstabenZuteilungRoutes,
      ),
  },
  {
    path: OPTION_GESUCHSPERIODEN.route,
    canActivate: [hasRoles(OPTION_GESUCHSPERIODEN.allowedRoles)],
    title: OPTION_GESUCHSPERIODEN.titleTranslationKey,
    loadChildren: () =>
      import('@dv/sachbearbeitung-app/feature/administration-gesuchsperiode').then(
        (m) => m.sachbearbeitungAppFeatureGesuchsperiodeRoutes,
      ),
  },
  {
    path: OPTION_EU_EFTA_LAENDER.route,
    canActivate: [hasRoles(OPTION_EU_EFTA_LAENDER.allowedRoles)],
    title: OPTION_EU_EFTA_LAENDER.titleTranslationKey,
    loadChildren: () =>
      import('@dv/sachbearbeitung-app/feature/administration-eu-efta-laender').then(
        (m) => m.sachbearbeitungAppFeatureAdministrationEuEftaLaenderRoutes,
      ),
  },
  {
    path: OPTION_BENUTZERVERWALTUNG.route,
    canActivate: [hasRoles(OPTION_BENUTZERVERWALTUNG.allowedRoles)],
    title: OPTION_BENUTZERVERWALTUNG.titleTranslationKey,
    loadChildren: () =>
      import('@dv/sachbearbeitung-app/feature/administration-benutzerverwaltung').then(
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
