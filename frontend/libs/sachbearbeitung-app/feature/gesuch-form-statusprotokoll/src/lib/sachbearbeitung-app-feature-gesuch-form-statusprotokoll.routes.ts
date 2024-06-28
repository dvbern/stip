import { Route } from '@angular/router';

import { StatusprotokollStore } from '@dv/sachbearbeitung-app/data-access/statusprotokoll';

import { SachbearbeitungAppFeatureGesuchFormStatusprotokollComponent } from './sachbearbeitung-app-feature-gesuch-form-statusprotokoll/sachbearbeitung-app-feature-gesuch-form-statusprotokoll.component';

export const sachbearbeitungAppFeatureGesuchFormStatusprotokollRoutes: Route[] =
  [
    {
      path: '',
      pathMatch: 'prefix',
      providers: [{ provide: StatusprotokollStore }],
      children: [
        {
          path: ':gesuchId',
          component:
            SachbearbeitungAppFeatureGesuchFormStatusprotokollComponent,
        },
        // add more routes here (siblings)
        // it is also possible to add nested routes as children
        // of this feature root component (or even lazy loaded sub features)
      ],
    },
  ];
