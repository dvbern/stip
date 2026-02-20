import { Injectable, computed } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';

interface NavItemBase {
  id: string;
  label: string;
  icon?: string;
  active?: boolean | undefined;
  visible?: boolean | undefined;
  testId?: string;
}

export interface NavItemLink extends NavItemBase {
  type: 'link';
  route: string[];
  queryParams?: Record<string, string>;
}

export interface NavItemAction extends NavItemBase {
  type: 'action';
  action: () => void;
  disabled?: boolean;
}

export interface NavItemGroup extends NavItemBase {
  type: 'menu';
  children: NavItem[];
}

export interface NavItemSeparator extends NavItemBase {
  type: 'separator';
}

export type NavItem =
  | NavItemLink
  | NavItemAction
  | NavItemGroup
  | NavItemSeparator;

type NavigationState = {
  navigationitems: NavItem[];
};

const initialState: NavigationState = {
  navigationitems: [],
};

@Injectable({ providedIn: 'root' })
export class NavigationStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  setNavigationItems = (navigationitems: NavItem[]) =>
    patchState(this, { navigationitems });

  navigationViewSig = computed(() => this.navigationitems());
}
