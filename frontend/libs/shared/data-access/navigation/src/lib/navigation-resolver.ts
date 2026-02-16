import { inject } from '@angular/core';
import { toObservable } from '@angular/core/rxjs-interop';
import { ActivatedRouteSnapshot, ResolveFn } from '@angular/router';
import { filter, firstValueFrom } from 'rxjs';

import { DarlehenStore } from '@dv/shared/data-access/darlehen';

import { NavigationStore } from './shared-data-access-navigation.store';

export const navigationResolver: ResolveFn<void> = async (
  route: ActivatedRouteSnapshot,
) => {
  const darlehenStore = inject(DarlehenStore);
  const navigationStore = inject(NavigationStore);
};
