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

import { SachbearbeitungAppFeatureDarlehenComponent } from './sachbearbeitung-app-feature-darlehen/sachbearbeitung-app-feature-darlehen.component';

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

  return darlehenService.getDarlehenSb$({ darlehenId }).pipe(
    map((data) => success(data)),
    catchError((err) => of(failure(err))),
    switchMap((rd) => {
      if (isFailure(rd)) {
        darlehenStore.setDarlehen(rd);
        return of(redirectToRoot);
      }

      // if (isSuccess(rd) && rd.data) {
      if (isSuccess(rd)) {
        darlehenStore.setDarlehen(rd);
        return of(rd.data);
      }

      return of(redirectToRoot);
    }),
  );
};

export const sachbearbeitungAppFeatureDarlehenRoutes: Route[] = [
  {
    path: ':darlehenId',
    pathMatch: 'prefix',
    component: SachbearbeitungAppFeatureDarlehenComponent,
    resolve: { darlehen: darlehenResolver },
    providers: [
      // feature specific services and other providers
      // always remove { providedIn: 'root' } from the feature specific services
    ],
    // children: [
    //   { path: '', component: SachbearbeitungAppFeatureDarlehenComponent },
    //   // add more routes here (siblings)
    //   // it is also possible to add nested routes as children
    //   // of this feature root component (or even lazy loaded sub features)
    // ],
  },
];
