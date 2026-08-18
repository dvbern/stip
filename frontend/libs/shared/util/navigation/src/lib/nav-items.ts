import { NavItem, NavMenuItem } from './navigation-types';

export const sozialdienstBaseNavItems: NavItem[] = [
  {
    type: 'link',
    id: 'antraege',
    label: { key: 'shared.header.dashboard' },
    icon: 'list',
    route: ['/dashboard'],
  },
];

export const sozialdienstAdminNavItems: NavItem[] = [
  {
    type: 'link',
    id: 'administration',
    label: { key: 'shared.header.administration' },
    icon: 'settings',
    route: ['/administration'],
    rolesAllowed: ['V0_Sozialdienst-Admin'],
  },
];

export const sozialdienstBaseMenuItems: NavMenuItem[] = [
  {
    type: 'link',
    id: 'profile',
    route: ['/profile'],
    label: { key: 'shared.menu.profile' },
  },
];

export const gesuchBaseNavItems: NavItem[] = [
  {
    type: 'link',
    id: 'dashboard',
    icon: 'dashboard',
    label: { key: 'shared.dashboard.title' },
    route: ['/dashboard'],
  },
];

export const gesuchBaseMenuItems: NavMenuItem[] = [
  {
    type: 'link',
    id: 'profile',
    route: ['/profile'],
    label: { key: 'shared.menu.profile' },
  },
];
