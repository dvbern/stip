import { Route } from '@angular/router';

import { DemoDataAppFeatureDemoDataOverviewComponent } from './demo-data-app-feature-demo-data-overview/demo-data-app-feature-demo-data-overview.component';

export const demoDataAppFeatureDemoDataOverviewRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    title: 'demo-data-app.overview.title',
    children: [
      { path: '', component: DemoDataAppFeatureDemoDataOverviewComponent },
    ],
  },
];
