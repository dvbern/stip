import { Route } from '@angular/router';

import { BerechnungStore } from '@dv/shared/data-access/berechnung';
import { GesuchInfoStore } from '@dv/shared/data-access/gesuch-info';
import { SteuerdatenStore } from '@dv/shared/data-access/steuerdaten';

export const sharedFeatureGesuchLayoutRoutes: Route[] = [
  // todo: rename to berechnung => im UX Sammeltask erledigen
  {
    path: 'verfuegung',
    title: 'shared.verfuegung.title',
    providers: [BerechnungStore, GesuchInfoStore],
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
      import('@dv/shared/feature/darlehen').then(
        (m) => m.sharedFeatureDarlehenRoutes,
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
      import('@dv/shared/feature/gesuch-form').then(
        (m) => m.sharedFeatureGesuchFormRoutes,
      ),
  },
];
