import { Route } from '@angular/router';

import { SachbearbeitungAppFeatureGesuchFormSteuerdatenComponent } from './sachbearbeitung-app-feature-gesuch-form-steuerdaten/sachbearbeitung-app-feature-gesuch-form-steuerdaten.component';

export const sachbearbeitungAppFeatureGesuchFormSteuerdatenRoutes: Route[] = [
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
        component: SachbearbeitungAppFeatureGesuchFormSteuerdatenComponent,
      },
      // add more routes here (siblings)
      // it is also possible to add nested routes as children
      // of this feature root component (or even lazy loaded sub features)
    ],
  },
];
