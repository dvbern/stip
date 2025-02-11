import { inject } from '@angular/core';
import { CanActivateFn, RedirectCommand, Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { map, switchMap } from 'rxjs';

import { selectRouteId } from '@dv/shared/data-access/gesuch';
import { GlobalNotificationStore } from '@dv/shared/global/notification';
import { PermissionStore } from '@dv/shared/global/permission';
import { AvailableBenutzerRole, RolesMap } from '@dv/shared/model/benutzer';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import { GesuchService } from '@dv/shared/model/gesuch';
import {
  Permission,
  getGesuchPermissions,
} from '@dv/shared/model/permission-state';
import { capitalized, isDefined } from '@dv/shared/model/type-util';

export const isAllowedTo =
  (permission: Permission): CanActivateFn =>
  () => {
    const gesuchService = inject(GesuchService);
    const store = inject(Store);
    const router = inject(Router);
    const config = inject(SharedModelCompileTimeConfig);

    return store.select(selectRouteId).pipe(
      switchMap((gesuchId) => {
        if (!gesuchId) {
          console.error('No gesuchId found in route');
          return [false];
        }

        return gesuchService.getGesuchInfo$({ gesuchId }).pipe(
          map(({ gesuchStatus }) =>
            getGesuchPermissions({ gesuchStatus }, config.appType, {
              Gesuchsteller: true,
            })[`can${capitalized(permission)}`]
              ? true
              : new RedirectCommand(router.parseUrl('/')),
          ),
        );
      }),
    );
  };

export const hasRoles =
  (roles: AvailableBenutzerRole[], redirectUrl?: string): CanActivateFn =>
  async () => {
    const permissionStore = inject(PermissionStore);
    const router = inject(Router);
    const notification = inject(GlobalNotificationStore);

    const roleMap = await permissionStore.getRolesMap();
    if (!isDefined(roleMap)) {
      return false;
    }
    if (roles.some((role) => roleMap?.[role])) {
      return true;
    }
    return (
      failSafeSozialdienstAdmin(roleMap, router) ||
      handleForbidden(redirectUrl, notification, router)
    );
  };

const failSafeSozialdienstAdmin = (rolesMap: RolesMap, router: Router) => {
  return rolesMap['Sozialdienst-Admin']
    ? new RedirectCommand(
        router.parseUrl('/administration/sozialdienst-benutzer'),
      )
    : false;
};

const handleForbidden = (
  redirectUrl = '/',
  notification: GlobalNotificationStore,
  router: Router,
) => {
  return (
    notification.handleForbiddenError(),
    new RedirectCommand(router.parseUrl(redirectUrl))
  );
};
