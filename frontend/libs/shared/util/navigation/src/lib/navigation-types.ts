import type {
  ComponentPortal,
  DomPortal,
  TemplatePortal,
} from '@angular/cdk/portal';
import { QueryParamsHandling, UrlTree } from '@angular/router';

import {
  BenutzerRole,
  SozialdienstBenutzerRole,
} from '@dv/shared/model/benutzer';
import { SharedModelState } from '@dv/shared/model/state-colors';

export type Portal<T = unknown> =
  | TemplatePortal
  | ComponentPortal<T>
  | DomPortal;

export type TranslateLabel = {
  key: string;
  context?: Record<string, string | number>;
};

interface NavItemBase {
  id: string;
  rolesAllowed?: BenutzerRole[] | SozialdienstBenutzerRole[];
  label?: TranslateLabel;
  icon?: string;
  active?: boolean | undefined;
  routerlinkActiveOptions?: { exact: boolean };
  testId?: string;
  badge?: {
    count: number;
    type: SharedModelState;
  };
}

export interface NavItemLink extends NavItemBase {
  type: 'link';
  route: (string | undefined)[];
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
  orientation?: 'horizontal' | 'vertical';
}

export type NavItem =
  | NavItemLink
  | NavItemAction
  | NavItemGroup
  | NavItemSeparator;

export interface TabNavItem {
  key: string;
  active: boolean | undefined;
  route: UrlTree | (string | undefined)[];
  roles?: BenutzerRole[] | SozialdienstBenutzerRole[];
  queryParams?: Record<string, string | null | undefined>;
  queryParamsHandling?: QueryParamsHandling;
  testId?: string;
}
