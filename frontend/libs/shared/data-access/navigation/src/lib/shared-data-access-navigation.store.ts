import { Injectable, computed, signal } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';

import { NavItem, Portal } from '@dv/shared/model/ui';

type NavigationState = {
  navigationItems: NavItem[];
};

const initialState: NavigationState = {
  navigationItems: [],
};

@Injectable({ providedIn: 'root' })
export class NavigationStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  setNavigationItems = (navigationItems: NavItem[]) =>
    patchState(this, { navigationItems });

  navigationViewSig = computed(() => this.navigationItems());

  portalSig = signal<Portal | null>(null);

  setPortal = (portal: Portal | null) => {
    this.portalSig.set(portal);
  };
}
