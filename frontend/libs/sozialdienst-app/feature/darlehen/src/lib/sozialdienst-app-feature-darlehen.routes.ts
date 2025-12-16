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
import { routeWithUnsavedChangesGuard } from '@dv/shared/pattern/unsaved-guard';
import {
  failure,
  isFailure,
  isSuccess,
  success,
} from '@dv/shared/util/remote-data';

import { SozialdienstAppFeatureDarlehenComponent } from './sozialdienst-app-feature-darlehen/sozialdienst-app-feature-darlehen.component';

// todo: duplicate code with gesuch-app, move to shared location
const darlehenResolver: ResolveFn<Darlehen> = (
  route: ActivatedRouteSnapshot,
) => {
  const darlehenStore = inject(DarlehenStore);
  const darlehenService = inject(DarlehenService);
  const darlehenId = route.paramMap.get('darlehenId');
  const router = inject(Router);
  const redirectToRoot = new RedirectCommand(router.parseUrl('/'));

  if (!darlehenId) {
    return redirectToRoot;
  }

  return darlehenService.getDarlehenGs$({ darlehenId }).pipe(
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

      return of(redirectToRoot);
    }),
  );
};

export const sozialdienstAppFeatureDarlehenRoutes: Route[] = [
  routeWithUnsavedChangesGuard({
    path: ':darlehenId',
    pathMatch: 'prefix',
    resolve: { darlehen: darlehenResolver },
    component: SozialdienstAppFeatureDarlehenComponent,
    title: 'shared.darlehen.title',
  }),
];
