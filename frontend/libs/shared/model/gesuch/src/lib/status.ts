export type StatusColor =
  | 'primary'
  | 'warn'
  | 'accent'
  | 'success'
  | 'caution'
  | 'info';

export const trancheRoutes = ['tranche', 'aenderung', 'initial'] as const;
export type TrancheRoute = (typeof trancheRoutes)[number];
export const aenderungRoutes = [
  'aenderung',
  'initial',
] satisfies TrancheRoute[];
export const darlehenRoutes = ['darlehen'] as const;
export type DarlehenRoute = (typeof darlehenRoutes)[number];
export const getTrancheRoute = (route: TrancheRoute) => route;
