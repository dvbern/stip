import { NavItem, NavMenuItem } from './navigation-types';

export const sozBaseNavItems: NavItem[] = [
  {
    type: 'link',
    id: 'antraege',
    label: { key: 'shared.header.dashboard' },
    icon: 'list',
    route: ['/dashboard'],
    testId: 'antraege-nav-item',
  },
];

export const sozAdminNavItems: NavItem[] = [
  {
    type: 'link',
    id: 'administration',
    label: { key: 'shared.header.administration' },
    icon: 'settings',
    route: ['/administration'],
    rolesAllowed: ['V0_Sozialdienst-Admin'],
    testId: 'admin-nav-item',
  },
];

export const sozBaseMenuItems: NavMenuItem[] = [
  {
    type: 'link',
    id: 'profile',
    route: ['/profile'],
    label: { key: 'shared.menu.profile' },
    testId: 'profile-menu-item',
  },
];

export const gsBaseNavItems: NavItem[] = [
  {
    type: 'link',
    id: 'dashboard',
    icon: 'dashboard',
    label: { key: 'shared.dashboard.title' },
    route: ['/dashboard'],
    testId: 'dashboard-nav-item',
  },
];

export const gsBaseMenuItems: NavMenuItem[] = [
  {
    type: 'link',
    id: 'profile',
    route: ['/profile'],
    label: { key: 'shared.menu.profile' },
    testId: 'profile-menu-item',
  },
];
