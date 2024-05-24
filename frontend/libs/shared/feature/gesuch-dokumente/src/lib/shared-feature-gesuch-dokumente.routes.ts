import { Route } from '@angular/router';
import { provideEffects } from '@ngrx/effects';
import { provideState } from '@ngrx/store';

import {
  sharedDataAccessDokumenteEffects,
  sharedDataAccessDokumentesFeature,
} from '@dv/shared/data-access/dokumente';

import { SharedFeatureGesuchDokumenteComponent } from './shared-feature-gesuch-dokumente/shared-feature-gesuch-dokumente.component';

export const sharedAppFeatureGesuchDokumenteRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    providers: [
      provideState(sharedDataAccessDokumentesFeature),
      provideEffects(sharedDataAccessDokumenteEffects),
    ],
    children: [
      {
        path: ':id',
        title: 'shared.dokumente.title',
        component: SharedFeatureGesuchDokumenteComponent,
      },
      // add more routes here (siblings)
      // it is also possible to add nested routes as children
      // of this feature root component (or even lazy loaded sub features)
    ],
  },
];
