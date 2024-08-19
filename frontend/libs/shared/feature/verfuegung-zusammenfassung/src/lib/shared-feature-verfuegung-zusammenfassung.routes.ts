import { Route } from '@angular/router';
import { SharedFeatureVerfuegungZusammenfassungComponent } from './shared-feature-verfuegung-zusammenfassung/shared-feature-verfuegung-zusammenfassung.component';

export const sharedFeatureVerfuegungZusammenfassungRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [
      // feature specific services and other providers
      // always remove { providedIn: 'root' } from the feature specific services
    ],
    children: [
      { path: '', component: SharedFeatureVerfuegungZusammenfassungComponent },
      // add more routes here (siblings)
      // it is also possible to add nested routes as children
      // of this feature root component (or even lazy loaded sub features)
    ],
  },
];
