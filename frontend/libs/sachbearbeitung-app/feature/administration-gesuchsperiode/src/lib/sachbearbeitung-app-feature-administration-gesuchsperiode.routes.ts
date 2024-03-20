import { Route } from '@angular/router';

import { OPTION_GESUCHSPERIODEN } from '@dv/sachbearbeitung-app/model/administration';

import { GesuchsperiodeDetailComponent } from './gesuchsperiode-detail/gesuchsperiode-detail.component';
import { GesuchsperiodeOverviewComponent } from './gesuchsperiode-overview/gesuchsperiode-overview.component';

export const sachbearbeitungAppFeatureGesuchsperiodeRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    data: { option: OPTION_GESUCHSPERIODEN },
    providers: [],
    children: [
      { path: '', component: GesuchsperiodeOverviewComponent },
      { path: 'new', component: GesuchsperiodeDetailComponent },
      { path: ':id', component: GesuchsperiodeDetailComponent },
    ],
  },
];
