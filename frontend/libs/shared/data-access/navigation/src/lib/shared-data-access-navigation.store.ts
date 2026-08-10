import { Injectable, computed, signal } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';

import { NavItem, NavMenuItem, Portal } from '@dv/shared/util/navigation';

type NavigationState = {
  navigationItems: NavItem[];
  menuItems: NavMenuItem[];
};

const initialState: NavigationState = {
  navigationItems: [],
  menuItems: [],
};

@Injectable({ providedIn: 'root' })
export class NavigationStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  setNavigationItems = (navigationItems: NavItem[]) =>
    patchState(this, { navigationItems });

  navigationViewSig = computed(() => this.navigationItems());

  setMenuItems = (menuItems: NavMenuItem[]) => {
    patchState(this, { menuItems });
  };

  menuItemsViewSig = computed(() => this.menuItems());

  portalSig = signal<Portal | null>(null);

  setPortal = (portal: Portal | null) => {
    this.portalSig.set(portal);
  };
}
