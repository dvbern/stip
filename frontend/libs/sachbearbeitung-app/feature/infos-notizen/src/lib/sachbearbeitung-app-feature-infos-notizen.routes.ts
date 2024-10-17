import { Route } from '@angular/router';

import { SachbearbeitungAppFeatureInfosNotizenComponent } from './sachbearbeitung-app-feature-infos-notizen/sachbearbeitung-app-feature-infos-notizen.component';

export const sachbearbeitungAppFeatureInfosNotizenRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [
      // feature specific services and other providers
      // always remove { providedIn: 'root' } from the feature specific services
    ],
    children: [
      { path: '', component: SachbearbeitungAppFeatureInfosNotizenComponent },
      // add more routes here (siblings)
      // it is also possible to add nested routes as children
      // of this feature root component (or even lazy loaded sub features)
    ],
  },
];
