import { Route } from '@angular/router';

import { OPTION_BUCHSTABEN_ZUTEILUNG } from '@dv/sachbearbeitung-app/model/administration';

import { SachbearbeitungAppFeatureAdministrationBuchstabenZuteilungComponent } from './sachbearbeitung-app-feature-administration-buchstaben-zuteilung/sachbearbeitung-app-feature-administration-buchstaben-zuteilung.component';

export const sachbearbeitungAppFeatureAdministrationBuchstabenZuteilungRoutes: Route[] =
  [
    {
      path: '',
      pathMatch: 'prefix',
      data: { option: OPTION_BUCHSTABEN_ZUTEILUNG },
      providers: [
        // feature specific services and other providers
        // always remove { providedIn: 'root' } from the feature specific services
      ],
      children: [
        {
          path: '',
          component:
            SachbearbeitungAppFeatureAdministrationBuchstabenZuteilungComponent,
        },
        // add more routes here (siblings)
        // it is also possible to add nested routes as children
        // of this feature root component (or even lazy loaded sub features)
      ],
    },
  ];
