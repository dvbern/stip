import { Route } from '@angular/router';

import { BerechnungStore } from '@dv/shared/data-access/berechnung';

export const sachbearbeitungAppPatternGesuchLayoutRoutes: Route[] = [
  // todo-after-merge: rename to berechnung
  {
    path: 'verfuegung',
    title: 'sachbearbeitung-app.verfuegung.title',
    providers: [BerechnungStore],
    loadComponent: () =>
      import('@dv/sachbearbeitung-app/feature/verfuegung').then(
        (m) => m.SachbearbeitungAppFeatureVerfuegungComponent,
      ),
    loadChildren: () =>
      import('@dv/sachbearbeitung-app/feature/verfuegung').then(
        (m) => m.sachbearbeitungAppFeatureVerfuegungRoutes,
      ),
  },
  {
    path: 'darlehen',
    title: 'sachbearbeitung-app.darlehen.title',
    loadChildren: () =>
      import('@dv/sachbearbeitung-app/feature/darlehen').then(
        (m) => m.sachbearbeitungAppFeatureDarlehenRoutes,
      ),
  },
  {
    path: 'infos',
    title: 'sachbearbeitung-app.infos.title',
    loadComponent: () =>
      import('@dv/sachbearbeitung-app/feature/infos').then(
        (m) => m.SachbearbeitungAppFeatureInfosComponent,
      ),
    loadChildren: () =>
      import('@dv/sachbearbeitung-app/feature/infos').then(
        (m) => m.sachbearbeitungAppFeatureInfosRoutes,
      ),
  },
  {
    // todo-after-merge: rename to formular?
    path: '',
    title: 'sachbearbeitung-app.gesuch-form.title',
    loadComponent: () =>
      import('@dv/sachbearbeitung-app/feature/gesuch-form').then(
        (m) => m.SachbearbeitungAppFeatureGesuchFormComponent,
      ),
    loadChildren: () =>
      import('@dv/sachbearbeitung-app/feature/gesuch-form').then(
        (m) => m.sachbearbeitungAppFeatureGesuchFormRoutes,
      ),
  },
];
