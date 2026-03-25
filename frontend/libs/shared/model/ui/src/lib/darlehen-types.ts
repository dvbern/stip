import { DarlehenStatus } from '@dv/shared/model/gesuch';

export type DarlehenCompleteStates = 'open' | 'rejected' | 'accepted';
export const darlehenStatusMapping: Record<
  DarlehenStatus,
  DarlehenCompleteStates
> = {
  IN_BEARBEITUNG_GS: 'open',
  EINGEGEBEN: 'open',
  IN_FREIGABE: 'open',
  ABGELEHNT: 'rejected',
  AKZEPTIERT: 'accepted',
};
export const darlehenCompletedStates: DarlehenCompleteStates[] = [
  'open',
  'rejected',
  'accepted',
];
