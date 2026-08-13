import { Route } from '@angular/router';

import { BerechnungStore } from '@dv/shared/data-access/berechnung';
import { SteuerdatenStore } from '@dv/shared/data-access/steuerdaten';

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
    path: ':gesuchId/darlehen',
    title: 'shared.darlehen.title',
    loadChildren: () =>
      import('@dv/shared/feature/darlehen-form').then(
        (m) => m.sharedFeatureDarlehenFormRoutes,
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
    providers: [SteuerdatenStore],
    loadComponent: () =>
      import('@dv/shared/feature/gesuch-form').then(
        (m) => m.SharedFeatureGesuchFormComponent,
      ),
    loadChildren: () =>
      import('@dv/sachbearbeitung-app/feature/gesuch-form').then(
        (m) => m.sachbearbeitungAppFeatureGesuchFormRoutes,
      ),
  },
];
