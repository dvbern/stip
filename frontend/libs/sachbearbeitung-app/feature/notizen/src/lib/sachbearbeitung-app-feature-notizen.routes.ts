import { Route } from '@angular/router';
import { SachbearbeitungAppFeatureNotizenComponent } from './sachbearbeitung-app-feature-notizen/sachbearbeitung-app-feature-notizen.component';

export const sachbearbeitungAppFeatureNotizenRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [
      // feature specific services and other providers
      // always remove { providedIn: 'root' } from the feature specific services
    ],
    children: [
      { path: '', component: SachbearbeitungAppFeatureNotizenComponent },
      // add more routes here (siblings)
      // it is also possible to add nested routes as children
      // of this feature root component (or even lazy loaded sub features)
    ],
  },
];
