import { Route } from '@angular/router';

import { BerechnungStore } from '@dv/shared/data-access/berechnung';

export const sachbearbeitungAppFeatureGesuchLayoutRoutes: Route[] = [
  // todo: rename to berechnung => im UX Sammeltask erledigen
  {
    path: 'verfuegung',
    title: 'shared.verfuegung.title',
    providers: [BerechnungStore],
    loadComponent: () =>
      import('@dv/shared/feature/verfuegung').then(
        (m) => m.SharedFeatureVerfuegungComponent,
      ),
    loadChildren: () =>
      import('@dv/shared/feature/verfuegung').then(
        (m) => m.sharedFeatureVerfuegungRoutes,
      ),
  },
  {
    path: 'darlehen',
    title: 'shared.darlehen.title',
    loadChildren: () =>
      import('@dv/shared/feature/darlehen').then(
        (m) => m.sharedFeatureDarlehenRoutes,
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
    path: '',
    title: 'shared.gesuch-form.title',
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
