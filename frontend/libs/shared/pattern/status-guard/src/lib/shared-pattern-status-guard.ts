import { inject } from '@angular/core';
import { CanActivateFn, RedirectCommand, Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { map, switchMap } from 'rxjs';

import { selectRouteId } from '@dv/shared/data-access/gesuch';
import { GlobalNotificationStore } from '@dv/shared/global/notification';
import { PermissionStore } from '@dv/shared/global/permission';
import { BenutzerVerwaltungRole } from '@dv/shared/model/benutzer';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import { GesuchService } from '@dv/shared/model/gesuch';
import {
  Permission,
  getGesuchPermissions,
} from '@dv/shared/model/permission-state';
import { capitalized } from '@dv/shared/model/type-util';

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

        return gesuchService
          .getGesuchInfo$({ gesuchId })
          .pipe(
            map(({ gesuchStatus }) =>
              getGesuchPermissions({ gesuchStatus }, config.appType)[
                `can${capitalized(permission)}`
              ]
                ? true
                : new RedirectCommand(router.parseUrl('/')),
            ),
          );
      }),
    );
  };

export const hasRoles =
  (roles: BenutzerVerwaltungRole[], redirectUrl = '/'): CanActivateFn =>
  () => {
    const permissionStore = inject(PermissionStore);
    const notification = inject(GlobalNotificationStore);

    const roleMap = permissionStore.permissionsMapSig();
    return roles.some((role) => roleMap?.[role])
      ? true
      : (notification.handleForbiddenError(),
        new RedirectCommand(inject(Router).parseUrl(redirectUrl)));
  };
