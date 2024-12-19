import { Route } from '@angular/router';

import { SharedFeatureDownloadComponent } from './shared-feature-download/shared-feature-download.component';

export const sharedFeatureDownloadRoutes: Route[] = [
  {
    path: ':dokumentId',
    pathMatch: 'prefix',
    providers: [],
    children: [{ path: '', component: SharedFeatureDownloadComponent }],
  },
];
