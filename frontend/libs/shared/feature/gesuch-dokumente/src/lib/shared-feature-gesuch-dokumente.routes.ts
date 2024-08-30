import { Route } from '@angular/router';

import { idAndTrancheIdRoutes } from '@dv/shared/util/gesuch';

import { SharedFeatureGesuchDokumenteComponent } from './shared-feature-gesuch-dokumente/shared-feature-gesuch-dokumente.component';

export const sharedFeatureGesuchDokumenteRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [],
    children: [
      ...idAndTrancheIdRoutes({
        title: 'shared.dokumente.title',
        component: SharedFeatureGesuchDokumenteComponent,
      }),
    ],
  },
];
