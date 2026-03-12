import { NavItem } from './navigation-types';

export const sozialdienstBaseMenuItems: NavItem[] = [
  {
    type: 'link',
    id: 'antraege',
    label: { key: 'sozialdienst-app.header.antraege' },
    icon: 'list',
    route: ['/dashboard'],
  },
  {
    type: 'link',
    id: 'administration',
    label: { key: 'sozialdienst-app.header.administration' },
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
    label: { key: 'gesuch-app.dashboard.title' },
    route: ['/dashboard'],
  },
];
