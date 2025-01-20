import { Route } from '@angular/router';

import { SharedFeatureDownloadComponent } from './shared-feature-download/shared-feature-download.component';

export const sharedFeatureDownloadRoutes: Route[] = [
  {
    path: ':id/:type',
    pathMatch: 'prefix',
    providers: [],
    children: [
      {
        path: '',
        component: SharedFeatureDownloadComponent,
        title: 'shared.file.download.message',
      },
    ],
  },
];
