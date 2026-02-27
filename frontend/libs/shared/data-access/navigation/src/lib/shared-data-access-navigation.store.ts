import {
  ComponentPortal,
  DomPortal,
  TemplatePortal,
} from '@angular/cdk/portal';
import { Injectable, computed, signal } from '@angular/core';
import { UrlTree } from '@angular/router';
import { patchState, signalStore, withState } from '@ngrx/signals';

export type TranslateLabel = {
  key: string;
  context?: Record<string, string | number>;
};

interface NavItemBase {
  id: string;
  label?: TranslateLabel;
  icon?: string;
  active?: boolean | undefined;
  testId?: string;
}

export interface NavItemLink extends NavItemBase {
  type: 'link';
  route: UrlTree | (string | undefined)[];
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
  navigationItems: NavItem[];
};

const initialState: NavigationState = {
  navigationItems: [],
};

export type Portal<T = unknown> =
  | TemplatePortal
  | ComponentPortal<T>
  | DomPortal;

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
