import { Route } from '@angular/router';

import { SachbearbeitungAppFeatureInfosBuchhaltungComponent } from './sachbearbeitung-app-feature-infos-buchhaltung/sachbearbeitung-app-feature-infos-buchhaltung.component';

export const sachbearbeitungAppFeatureInfosBuchhaltungRoutes: Route[] = [
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
        component: SachbearbeitungAppFeatureInfosBuchhaltungComponent,
      },
      // add more routes here (siblings)
      // it is also possible to add nested routes as children
      // of this feature root component (or even lazy loaded sub features)
    ],
  },
];
