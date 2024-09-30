import {
  GesuchTrancheStatus,
  SharedModelGesuch,
} from '@dv/shared/model/gesuch';

export type AbschlussPhase =
  | 'NOT_READY'
  | 'READY_TO_SEND'
  | 'SUBMITTED'
  | Extract<
      GesuchTrancheStatus,
      'AKZEPTIERT' | 'ABGELEHNT' | 'MANUELLE_AENDERUNG'
    >;
export const toAbschlussPhase = (
  gesuch: SharedModelGesuch | null,
  options?: { isComplete?: boolean; checkTranche?: boolean },
): AbschlussPhase => {
  if (!gesuch) {
    return 'NOT_READY';
  }
  if (options?.checkTranche) {
    switch (gesuch.gesuchTrancheToWorkWith.status) {
      case 'IN_BEARBEITUNG_GS':
        return options?.isComplete ? 'READY_TO_SEND' : 'NOT_READY';
      case 'UEBERPRUEFEN':
        return 'SUBMITTED';
      default:
        return gesuch.gesuchTrancheToWorkWith.status;
    }
  }
  switch (gesuch.gesuchStatus) {
    case 'IN_BEARBEITUNG_GS':
      return options?.isComplete ? 'READY_TO_SEND' : 'NOT_READY';
    case 'EINGEREICHT':
      return 'SUBMITTED';
    default:
      return 'SUBMITTED';
  }
};
