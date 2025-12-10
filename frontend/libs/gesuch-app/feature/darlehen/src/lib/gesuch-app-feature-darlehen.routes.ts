import { inject } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  RedirectCommand,
  ResolveFn,
  Route,
  Router,
} from '@angular/router';
import { catchError, map, of, switchMap } from 'rxjs';

import { DarlehenStore } from '@dv/shared/data-access/darlehen';
import { Darlehen, DarlehenService } from '@dv/shared/model/gesuch';
import {
  failure,
  isFailure,
  isSuccess,
  success,
} from '@dv/shared/util/remote-data';

import { GesuchAppFeatureDarlehenComponent } from './gesuch-app-feature-darlehen/gesuch-app-feature-darlehen.component';

const darlehenResolver: ResolveFn<Darlehen> = (
  route: ActivatedRouteSnapshot,
) => {
  const darlehenStore = inject(DarlehenStore);
  const darlehenService = inject(DarlehenService);
  const fallId = route.paramMap.get('fallId');
  const router = inject(Router);
  const redirectToRoot = new RedirectCommand(router.parseUrl('/'));

  if (!fallId) {
    return redirectToRoot;
  }

  return darlehenService.getDarlehenGs$({ fallId }).pipe(
    map((data) => success(data)),
    catchError((err) => of(failure(err))),
    switchMap((rd) => {
      if (isFailure(rd)) {
        darlehenStore.setDarlehen(rd);
        return of(redirectToRoot);
      }

      if (isSuccess(rd) && rd.data) {
        darlehenStore.setDarlehen(rd);
        return of(rd.data);
      }

      // No Darlehen found (response data is null, status 204) --> create new Darlehen
      return darlehenService.createDarlehen$({ fallId }).pipe(
        map((data) => {
          darlehenStore.setDarlehen(success(data));
          return data;
        }),
        catchError((err) => {
          darlehenStore.setDarlehen(failure(err));
          return of(redirectToRoot);
        }),
      );
    }),
  );
};

export const gesuchAppFeatureDarlehenRoutes: Route[] = [
  {
    path: ':fallId',
    pathMatch: 'prefix',
    providers: [DarlehenStore],
    resolve: { darlehen: darlehenResolver },
    component: GesuchAppFeatureDarlehenComponent,
    title: 'shared.darlehen.title',
  },
];
