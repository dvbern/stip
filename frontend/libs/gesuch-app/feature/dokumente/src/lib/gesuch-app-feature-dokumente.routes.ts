import { Route } from '@angular/router';

import { GesuchAppFeatureDokumenteComponent } from './gesuch-app-feature-dokumente/gesuch-app-feature-dokumente.component';

export const gesuchAppFeatureDokumenteRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [
      // feature specific services and other providers
      // always remove { providedIn: 'root' } from the feature specific services
    ],
    children: [
      { path: '', component: GesuchAppFeatureDokumenteComponent },
      // add more routes here (siblings)
      // it is also possible to add nested routes as children
      // of this feature root component (or even lazy loaded sub features)
    ],
  },
];
