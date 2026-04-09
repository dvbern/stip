import { Route } from '@angular/router';

import { SachbearbeitungAppFeatureDashboardComponent } from './sachbearbeitung-app-feature-dashboard/sachbearbeitung-app-feature-dashboard.component';

export const sachbearbeitungAppFeatureDashboardRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    component: SachbearbeitungAppFeatureDashboardComponent,
    providers: [],
    children: [
      {
        path: 'fehlgeschlagene-zahlungen',
        title: 'sachbearbeitung-app.fehlgeschlagene-zahlungen.title',
        loadChildren: () =>
          import('@dv/sachbearbeitung-app/feature/fehlgeschlagene-zahlungen').then(
            (m) => m.sachbearbeitungAppFeatureFehlgeschlageneZahlungenRoutes,
          ),
      },
      {
        path: 'antraege',
        title: 'sachbearbeitung-app.cockpit.title',
        loadComponent: () =>
          import('@dv/sachbearbeitung-app/feature/cockpit').then(
            (m) => m.SachbearbeitungAppFeatureCockpitComponent,
          ),
        loadChildren: () =>
          import('@dv/sachbearbeitung-app/feature/cockpit').then(
            (m) => m.sachbearbeitungAppFeatureCockpitRoutes,
          ),
      },
    ],
  },
];
