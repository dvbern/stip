import type {
  ComponentPortal,
  DomPortal,
  TemplatePortal,
} from '@angular/cdk/portal';
import { UrlTree } from '@angular/router';

import {
  BenutzerRole,
  SozialdienstBenutzerRole,
} from '@dv/shared/model/benutzer';

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
  orientation?: 'horizontal' | 'vertical';
}

export type NavItem =
  | NavItemLink
  | NavItemAction
  | NavItemGroup
  | NavItemSeparator;
