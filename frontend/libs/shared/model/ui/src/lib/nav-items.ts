import {
  ComponentPortal,
  DomPortal,
  TemplatePortal,
} from '@angular/cdk/portal';
import { UrlTree } from '@angular/router';

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
