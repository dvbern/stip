import { inject } from '@angular/core';
import { CanActivateFn, RedirectCommand, Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { filter, map, switchMap, take } from 'rxjs';

import {
  SharedDataAccessGesuchEvents,
  selectSharedDataAccessGesuchsView,
} from '@dv/shared/data-access/gesuch';
import { GlobalNotificationStore } from '@dv/shared/global/notification';
import { PermissionStore } from '@dv/shared/global/permission';
import { BenutzerVerwaltungRole } from '@dv/shared/model/benutzer';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import {
  Permission,
  getGesuchPermissions,
} from '@dv/shared/model/permission-state';
import { capitalized, isDefined } from '@dv/shared/model/type-util';

export const isAllowedTo =
  (permission: Permission): CanActivateFn =>
  () => {
    const store = inject(Store);
    const router = inject(Router);
    const config = inject(SharedModelCompileTimeConfig);

    return getLatestGesuch$(store).pipe(
      map((gesuch) =>
        getGesuchPermissions(gesuch, config.appType)[
          `can${capitalized(permission)}`
        ]
          ? true
          : new RedirectCommand(router.parseUrl('/')),
      ),
    );
  };

export const hasRoles =
  (roles: BenutzerVerwaltungRole[]): CanActivateFn =>
  () => {
    const permissionStore = inject(PermissionStore);
    const notification = inject(GlobalNotificationStore);

    const roleMap = permissionStore.permissionsMapSig();
    return roles.some((role) => roleMap?.[role])
      ? true
      : (notification.handleForbiddenError(),
        new RedirectCommand(inject(Router).parseUrl('/')));
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
