import { Route } from '@angular/router';

import { SozialdienstAppFeatureGesuchCockpitComponent } from './sozialdienst-app-feature-gesuch-cockpit/sozialdienst-app-feature-gesuch-cockpit.component';

export const sozialdienstAppFeatureGesuchCockpitRoutes: Route[] = [
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
        component: SozialdienstAppFeatureGesuchCockpitComponent,
        //todo: add correct title
        title: 'gesuch-app.dashboard.title',
      },
      // add more routes here (siblings)
      // it is also possible to add nested routes as children
      // of this feature root component (or even lazy loaded sub features)
    ],
  },
];
