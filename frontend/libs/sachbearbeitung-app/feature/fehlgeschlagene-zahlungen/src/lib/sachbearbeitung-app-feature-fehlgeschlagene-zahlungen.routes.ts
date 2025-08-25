import { Route } from '@angular/router';

import { SachbearbeitungAppFeatureFehlgeschlageneZahlungenComponent } from './sachbearbeitung-app-feature-fehlgeschlagene-zahlungen/sachbearbeitung-app-feature-fehlgeschlagene-zahlungen.component';

export const sachbearbeitungAppFeatureFehlgeschlageneZahlungenRoutes: Route[] =
  [
    {
      path: '',
      pathMatch: 'prefix',
      providers: [
        // feature specific services and other providers
        // always remove { providedIn: 'root' } from the feature specific services
      ],
      children: [
        {
          path: '',
          component: SachbearbeitungAppFeatureFehlgeschlageneZahlungenComponent,
        },
        // add more routes here (siblings)
        // it is also possible to add nested routes as children
        // of this feature root component (or even lazy loaded sub features)
      ],
    },
  ];
