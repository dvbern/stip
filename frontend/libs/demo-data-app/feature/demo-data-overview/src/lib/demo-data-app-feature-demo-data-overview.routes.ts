import { Route } from '@angular/router';

import { DemoDataStore } from '@dv/demo-data-app/data-access/demo-data';

import { DemoDataAppFeatureDemoDataOverviewComponent } from './demo-data-app-feature-demo-data-overview/demo-data-app-feature-demo-data-overview.component';

export const demoDataAppFeatureDemoDataOverviewRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    title: 'demo-data-app.overview.title',
    providers: [DemoDataStore],
    children: [
      { path: '', component: DemoDataAppFeatureDemoDataOverviewComponent },
    ],
  },
];
