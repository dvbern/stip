import { inject } from '@angular/core';
import { Route } from '@angular/router';

import { GesuchsperiodeStore } from '@dv/sachbearbeitung-app/data-access/gesuchsperiode';
import {
  CHILD_OPTION_GESUCHSJAHRE,
  CHILD_OPTION_GESUCHSPERIODE,
  OPTION_GESUCHSPERIODEN,
} from '@dv/sachbearbeitung-app/model/administration';
import { routeWithUnsavedChangesGuard } from '@dv/shared/pattern/unsaved-guard';

import { GesuchsjahrDetailComponent } from './gesuchsjahr-detail/gesuchsjahr-detail.component';
import { GesuchsperiodeDetailComponent } from './gesuchsperiode-detail/gesuchsperiode-detail.component';
import { GesuchsperiodeOverviewComponent } from './gesuchsperiode-overview/gesuchsperiode-overview.component';

const resetResolver = {
  init: () => {
    inject(GesuchsperiodeStore).resetCurrentData();
  },
};

export const sachbearbeitungAppFeatureGesuchsperiodeRoutes: Route[] = [
  {
    path: '',
    pathMatch: 'prefix',
    data: { option: OPTION_GESUCHSPERIODEN },
    providers: [{ provide: GesuchsperiodeStore }],
    children: [
      {
        path: '',
        component: GesuchsperiodeOverviewComponent,
        title: 'sachbearbeitung-app.admin.gesuchsperiode.route.overview',
      },
      routeWithUnsavedChangesGuard({
        data: {
          option: CHILD_OPTION_GESUCHSPERIODE(
            'sachbearbeitung-app.admin.gesuchsperiode.route.create',
          ),
        },
        path: 'create',
        resolve: resetResolver,
        component: GesuchsperiodeDetailComponent,
        title: 'sachbearbeitung-app.admin.gesuchsperiode.route.create',
      }),
      routeWithUnsavedChangesGuard({
        data: {
          option: CHILD_OPTION_GESUCHSPERIODE(
            'sachbearbeitung-app.admin.gesuchsperiode.route.detail',
          ),
        },
        path: ':id',
        resolve: resetResolver,
        component: GesuchsperiodeDetailComponent,
        title: 'sachbearbeitung-app.admin.gesuchsperiode.route.detail',
      }),
      routeWithUnsavedChangesGuard({
        data: {
          option: CHILD_OPTION_GESUCHSJAHRE(
            'sachbearbeitung-app.admin.gesuchsjahr.route.create',
          ),
        },
        path: 'jahr/create',
        resolve: resetResolver,
        component: GesuchsjahrDetailComponent,
        title: 'sachbearbeitung-app.admin.gesuchsjahr.route.create',
      }),
      routeWithUnsavedChangesGuard({
        data: {
          option: CHILD_OPTION_GESUCHSJAHRE(
            'sachbearbeitung-app.admin.gesuchsjahr.route.detail',
          ),
        },
        path: 'jahr/:id',
        resolve: resetResolver,
        component: GesuchsjahrDetailComponent,
        title: 'sachbearbeitung-app.admin.gesuchsjahr.route.detail',
      }),
    ],
  },
];
