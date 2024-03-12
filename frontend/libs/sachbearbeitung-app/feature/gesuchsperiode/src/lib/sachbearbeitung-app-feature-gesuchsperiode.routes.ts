import { Route } from '@angular/router';

import { SachbearbeitungAppFeatureGesuchsperiodeComponent } from './sachbearbeitung-app-feature-gesuchsperiode/sachbearbeitung-app-feature-gesuchsperiode.component';

export const sachbearbeitungAppFeatureGesuchsperiodeRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [
      // feature specific services and other providers
      // always remove { providedIn: 'root' } from the feature specific services
    ],
    children: [
      { path: '', component: SachbearbeitungAppFeatureGesuchsperiodeComponent },
      // add more routes here (siblings)
      // it is also possible to add nested routes as children
      // of this feature root component (or even lazy loaded sub features)
    ],
  },
];
