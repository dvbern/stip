import { Route } from '@angular/router';

import { OPTION_GESUCHSPERIODEN } from '@dv/sachbearbeitung-app/model/administration';

import { GesuchsjahrDetailComponent } from './gesuchsjahr-detail/gesuchsjahr-detail.component';
import { GesuchsperiodeDetailComponent } from './gesuchsperiode-detail/gesuchsperiode-detail.component';
import { GesuchsperiodeOverviewComponent } from './gesuchsperiode-overview/gesuchsperiode-overview.component';

export const sachbearbeitungAppFeatureGesuchsperiodeRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    data: { option: OPTION_GESUCHSPERIODEN },
    providers: [],
    children: [
      {
        path: '',
        component: GesuchsperiodeOverviewComponent,
        title: 'sachbearbeitung-app.admin.gesuchsperiode.route.overview',
      },
      {
        path: 'create',
        component: GesuchsperiodeDetailComponent,
        title: 'sachbearbeitung-app.admin.gesuchsperiode.route.create',
      },
      {
        path: ':id',
        component: GesuchsperiodeDetailComponent,
        title: 'sachbearbeitung-app.admin.gesuchsperiode.route.detail',
      },
      {
        path: 'jahr/create',
        component: GesuchsjahrDetailComponent,
        title: 'sachbearbeitung-app.admin.gesuchsperiode.route.create',
      },
      {
        path: 'jahr/:id',
        component: GesuchsjahrDetailComponent,
        title: 'sachbearbeitung-app.admin.gesuchsperiode.route.jahr',
      },
    ],
  },
];
