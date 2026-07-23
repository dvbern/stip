export type StatusColor =
  | 'primary'
  | 'warn'
  | 'accent'
  | 'success'
  | 'info'
  | 'danger';

export const trancheRoutes = [
  'tranche',
  'aenderung',
  'initial',
  'eingereicht',
] as const;
export type TrancheRoute = (typeof trancheRoutes)[number];
export const aenderungRoutes = [
  'aenderung',
  'initial',
  'eingereicht',
] satisfies TrancheRoute[];
export const darlehenRoutes = ['darlehen'] as const;
export type DarlehenRoute = (typeof darlehenRoutes)[number];
export const getTrancheRoute = <T extends TrancheRoute>(route: T) => route;
