/* eslint-disable @typescript-eslint/no-unused-vars */
import { inject } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  ResolveFn,
  RouterStateSnapshot,
} from '@angular/router';

import { DarlehenStore } from '@dv/shared/data-access/darlehen';
import { DarlehenService } from '@dv/shared/model/gesuch';

import { NavigationStore } from './shared-data-access-navigation.store';

// todo: use or delete
export const navigationResolver: ResolveFn<void> = async (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot,
) => {
  const darlehenStore = inject(DarlehenStore);
  const darlehenServcie = inject(DarlehenService);
  const navigationStore = inject(NavigationStore);
};
