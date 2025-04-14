import { Route } from '@angular/router';

import { hasRoles } from '@dv/shared/pattern/status-guard';
import { OPTION_SOZIALDIENST_BENUTZER } from '@dv/sozialdienst-app/model/administration';

export const sachbearbeitungAppFeatureAdministrationRoutes: Route[] = [
  {
    path: OPTION_SOZIALDIENST_BENUTZER.route,
    canActivate: [hasRoles(['V0_Sozialdienst-Admin'], '/unauthorized')],
    loadChildren: () =>
      import(
        '@dv/sozialdienst-app/feature/administration-sozialdienst-benutzer'
      ).then(
        (m) => m.sozialdienstAppFeatureAdministrationSozialdienstBenutzerRoutes,
      ),
  },
  {
    path: '',
    pathMatch: 'prefix',
    redirectTo: OPTION_SOZIALDIENST_BENUTZER.route,
  },
];
