import { AppType } from '@dv/shared/model/config';
import {
  GesuchTranche,
  GesuchTrancheStatus,
  Gesuchstatus,
  SharedModelGesuch,
} from '@dv/shared/model/gesuch';

/** Readonly */
const R__ = 'readonly';
/** Writable */
const __W = 'writeable';
type Persmissions = typeof R__ | typeof __W;

const gs = 'gesuch-app' satisfies AppType;
const sb = 'sachbearbeitung-app' satisfies AppType;

/**
 * Define the readonly state for the gesuch based on the status.
 * The readonly state depends on the app type
 *
 * * Format is: { [Gesuchstatus]: { [AppType]: boolean, ... other AppTypes } }
 * * A short hand variable for the AppType and true/false is used to make the code more readable and monospaced
 */
export const readWriteStatusByAppType = {
  IN_BEARBEITUNG_GS /**                */: { [gs]: __W, [sb]: R__ },
  EINGEREICHT /**                      */: { [gs]: R__, [sb]: R__ },
  BEREIT_FUER_BEARBEITUNG /**          */: { [gs]: R__, [sb]: R__ },
  IN_BEARBEITUNG_SB /**                */: { [gs]: R__, [sb]: __W },
  IN_FREIGABE /**                      */: { [gs]: R__, [sb]: R__ },
  ABKLAERUNG_DURCH_RECHSTABTEILUNG /** */: { [gs]: R__, [sb]: R__ },
  ANSPRUCH_MANUELL_PRUEFEN /**         */: { [gs]: R__, [sb]: R__ },
  FEHLENDE_DOKUMENTE /**               */: { [gs]: __W, [sb]: R__ },
  GESUCH_ABGELEHNT /**                 */: { [gs]: R__, [sb]: R__ },
  JURISTISCHE_ABKLAERUNG /**           */: { [gs]: R__, [sb]: R__ },
  KEIN_STIPENDIENANSPRUCH /**          */: { [gs]: R__, [sb]: R__ },
  NICHT_ANSPRUCHSBERECHTIGT /**        */: { [gs]: R__, [sb]: R__ },
  NICHT_BEITRAGSBERECHTIGT /**         */: { [gs]: R__, [sb]: R__ },
  STIPENDIENANSPRUCH /**               */: { [gs]: R__, [sb]: R__ },
  WARTEN_AUF_UNTERSCHRIFTENBLATT /**   */: { [gs]: R__, [sb]: R__ },
  VERSANDBEREIT /**                    */: { [gs]: R__, [sb]: R__ },
  VERFUEGT /**                         */: { [gs]: R__, [sb]: R__ },
  VERSENDET /**                        */: { [gs]: R__, [sb]: R__ },
} as const satisfies Record<Gesuchstatus, Record<AppType, Persmissions>>;

/**
 * Same as `readWriteStatusByAppType` but for the tranches
 *
 * @see {@link readWriteStatusByAppType}
 */
export const trancheReadWritestatusByAppType = {
  IN_BEARBEITUNG_GS: /**  */ { [gs]: __W, [sb]: R__ },
  UEBERPRUEFEN: /**       */ { [gs]: R__, [sb]: __W },
  AKZEPTIERT: /**         */ { [gs]: R__, [sb]: R__ },
  ABGELEHNT: /**          */ { [gs]: __W, [sb]: R__ },
  MANUELLE_AENDERUNG: /** */ { [gs]: R__, [sb]: R__ },
} as const satisfies Record<GesuchTrancheStatus, Record<AppType, Persmissions>>;

export const isGesuchReadonly = (
  gesuch: SharedModelGesuch | null,
  appType?: AppType,
) => {
  if (!gesuch || !appType) return false;

  const state = readWriteStatusByAppType[gesuch.gesuchStatus][appType];
  return state === R__;
};

export const isTrancheReadonly = (
  tranche: GesuchTranche | null,
  appType?: AppType,
) => {
  if (!tranche || !appType) return false;

  const state = trancheReadWritestatusByAppType[tranche.status][appType];
  return state === R__;
};
