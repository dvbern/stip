import { inject } from '@angular/core';
import { toObservable } from '@angular/core/rxjs-interop';
import {
  ActivatedRouteSnapshot,
  RedirectCommand,
  ResolveFn,
  Route,
  Router,
  RouterStateSnapshot,
} from '@angular/router';
import { filter, map } from 'rxjs';

import { DarlehenStore } from '@dv/shared/data-access/darlehen';
import { Darlehen } from '@dv/shared/model/gesuch';
import { isFailure, isSuccess } from '@dv/shared/util/remote-data';

import { GesuchAppFeatureDarlehenComponent } from './gesuch-app-feature-darlehen/gesuch-app-feature-darlehen.component';

const darlehenResolver: ResolveFn<Darlehen> = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot,
) => {
  const darlehenStore = inject(DarlehenStore);
  const fallId = route.paramMap.get('fallId');
  const router = inject(Router);

  if (!fallId) {
    const route = router.parseUrl('/');
    return new RedirectCommand(route);
  }

  const obs = toObservable(darlehenStore.getDarlehenRequest).pipe(
    filter((rd) => isSuccess(rd) || isFailure(rd)),
    map((requestRd) => {
      if (isSuccess(requestRd) && requestRd.data) {
        return requestRd.data;
      } else {
        // Handle error or missing data case
        const route = router.parseUrl('/');
        return new RedirectCommand(route);
      }
    }),
  );

  darlehenStore.getDarlehenGs$({ fallId });

  return obs;
};

export const gesuchAppFeatureDarlehenRoutes: Route[] = [
  {
    path: ':fallId',
    pathMatch: 'prefix',
    providers: [DarlehenStore],
    resolve: { darlehen: darlehenResolver },
    component: GesuchAppFeatureDarlehenComponent,
    title: 'shared.darlehen.title',
    // children: [
    //   {
    //     path: ':fallId',
    //   },
    // ],
  },
];
