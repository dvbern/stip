import { Route } from '@angular/router';

import { SharedFeatureGesuchDokumenteComponent } from './shared-feature-gesuch-dokumente/shared-feature-gesuch-dokumente.component';

export const sharedFeatureGesuchDokumenteRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [],
    children: [
      {
        path: ':id',
        title: 'shared.dokumente.title',
        component: SharedFeatureGesuchDokumenteComponent,
      },
      // add more routes here (siblings)
      // it is also possible to add nested routes as children
      // of this feature root component (or even lazy loaded sub features)
    ],
  },
];
