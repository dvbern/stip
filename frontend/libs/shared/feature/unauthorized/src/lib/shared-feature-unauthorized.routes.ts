import { Route } from '@angular/router';

import { SharedFeatureUnauthorizedComponent } from './shared-feature-unauthorized/shared-feature-unauthorized.component';

export const sharedFeatureUnauthorizedRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    title: 'shared.unauthorized.title',
    children: [{ path: '', component: SharedFeatureUnauthorizedComponent }],
  },
];
