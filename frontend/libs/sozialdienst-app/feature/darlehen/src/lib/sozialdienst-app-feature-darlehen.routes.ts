import { Route } from '@angular/router';
import { SozialdienstAppFeatureDarlehenComponent } from './sozialdienst-app-feature-darlehen/sozialdienst-app-feature-darlehen.component';

export const sozialdienstAppFeatureDarlehenRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [
      // feature specific services and other providers
      // always remove { providedIn: 'root' } from the feature specific services
    ],
    children: [
      { path: '', component: SozialdienstAppFeatureDarlehenComponent },
      // add more routes here (siblings)
      // it is also possible to add nested routes as children
      // of this feature root component (or even lazy loaded sub features)
    ],
  },
];
