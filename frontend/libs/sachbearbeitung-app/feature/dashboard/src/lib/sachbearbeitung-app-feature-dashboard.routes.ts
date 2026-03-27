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
        path: 'darlehen',
        title: 'sachbearbeitung-app.darlehen-dashboard.title',
        loadComponent: () =>
          import('@dv/sachbearbeitung-app/feature/darlehen-dashboard').then(
            (m) => m.SachbearbeitungAppFeatureDarlehenDashboardComponent,
          ),
        loadChildren: () =>
          import('@dv/sachbearbeitung-app/feature/darlehen-dashboard').then(
            (m) => m.sachbearbeitungAppFeatureDarlehenDashboardRoutes,
          ),
      },
      {
        path: 'fehlgeschlagene-zahlungen',
        title: 'sachbearbeitung-app.fehlgeschlagene-zahlungen.title',
        loadChildren: () =>
          import('@dv/sachbearbeitung-app/feature/fehlgeschlagene-zahlungen').then(
            (m) => m.sachbearbeitungAppFeatureFehlgeschlageneZahlungenRoutes,
          ),
      },
      // {
      //   path: ':tab',
      //   title: 'sachbearbeitung-app.gesuche.title',
      //   loadComponent: () =>
      //     import('@dv/sachbearbeitung-app/feature/gesuche').then(
      //       (m) => m.SachbearbeitungAppFeatureGesucheComponent,
      //     ),
      //   loadChildren: () =>
      //     import('@dv/sachbearbeitung-app/feature/gesuche').then(
      //       (m) => m.sachbearbeitungAppFeatureGesucheRoutes,
      //     ),
      // },
      {
        path: 'gesuche',
        title: 'sachbearbeitung-app.gesuche.title',
        loadComponent: () =>
          import('@dv/sachbearbeitung-app/feature/gesuche').then(
            (m) => m.SachbearbeitungAppFeatureGesucheComponent,
          ),
        loadChildren: () =>
          import('@dv/sachbearbeitung-app/feature/gesuche').then(
            (m) => m.sachbearbeitungAppFeatureGesucheRoutes,
          ),
      },
      {
        path: ':tab',
        redirectTo: '',
      },
    ],
  },
];
