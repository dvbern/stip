import { NavItem } from './navigation-types';

export const sozialdienstBaseMenuItems: NavItem[] = [
  {
    type: 'link',
    id: 'antraege',
    label: { key: 'shared.header.dashboard' },
    icon: 'list',
    route: ['/dashboard'],
  },
  {
    type: 'link',
    id: 'administration',
    label: { key: 'shared.header.administration' },
    icon: 'settings',
    route: ['/administration'],
    rolesAllowed: ['V0_Sozialdienst-Admin'],
  },
];

export const gesuchBaseMenuItems: NavItem[] = [
  {
    type: 'link',
    id: 'dashboard',
    icon: 'dashboard',
    label: { key: 'shared.dashboard.title' },
    route: ['/dashboard'],
  },
];
