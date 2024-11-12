import { inject } from '@angular/core';
import { CanActivateFn, RedirectCommand, Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { filter, map, switchMap, take } from 'rxjs';

import {
  SharedDataAccessGesuchEvents,
  selectSharedDataAccessGesuchsView,
} from '@dv/shared/data-access/gesuch';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import { canViewVerfuegung } from '@dv/shared/util/permission-state';
import { isDefined } from '@dv/shared/util-fn/type-guards';

export const allowVerfuegung: CanActivateFn = () => {
  const store = inject(Store);
  const router = inject(Router);
  const config = inject(SharedModelCompileTimeConfig);

  return getLatestGesuch$(store).pipe(
    map((gesuch) =>
      canViewVerfuegung(gesuch, config.appType)
        ? true
        : new RedirectCommand(router.parseUrl('/')),
    ),
  );
};

const loadAndGetGesuch$ = (store: Store) => {
  store.dispatch(SharedDataAccessGesuchEvents.loadGesuch());

  return store.select(selectSharedDataAccessGesuchsView).pipe(
    filter(({ gesuch }) => isDefined(gesuch)),
    map(({ gesuch }) => gesuch),
    take(1),
  );
};

const getLatestGesuch$ = (store: Store) => {
  return store.select(selectSharedDataAccessGesuchsView).pipe(
    take(1),
    switchMap(({ gesuch }) =>
      isDefined(gesuch) ? [gesuch] : loadAndGetGesuch$(store),
    ),
    filter((gesuch) => isDefined(gesuch)),
  );
};
