import { AppType } from '@dv/shared/model/config';
import {
  GesuchTranche,
  GesuchTrancheStatus,
  Gesuchstatus,
  SharedModelGesuch,
} from '@dv/shared/model/gesuch';

const permissions = {
  /** Writable */
  W: 0,
  /** Verfügung einsehen */
  V: 1,
  /** Fehlende Dokumente hochladen */
  D: 2,
} as const;

const hasPermission = (p: Permissions, perm: keyof typeof permissions) =>
  p.charAt(permissions[perm]) === perm;
const _canWrite = (p: Permissions) => hasPermission(p, 'W');
const _canViewVerfuegung = (p: Permissions) => hasPermission(p, 'V');

type P<T extends keyof typeof permissions> = T | ' ';
/**
 * Permissions for the gesuch and tranche interactions
 *
 * * {@link permissions.W} - Write
 * * {@link permissions.V} - Verfuegung einsehen
 * * {@link permissions.D} - Fehlende Dokumente hochladen
 */
type Permissions = `${P<'W'>}${P<'V'>}`;

const gs = 'gesuch-app' satisfies AppType;
const sb = 'sachbearbeitung-app' satisfies AppType;

/**
 * Define the readonly state for the gesuch based on the status.
 * The readonly state depends on the app type
 *
 * * Format is: { [Gesuchstatus]: { [AppType]: 'WV' | 'W ' | ..., ... other AppTypes } }
 */
export const readWriteStatusByAppType = {
  IN_BEARBEITUNG_GS /**                */: { [gs]: 'W ', [sb]: '  ' },
  EINGEREICHT /**                      */: { [gs]: '  ', [sb]: '  ' },
  BEREIT_FUER_BEARBEITUNG /**          */: { [gs]: '  ', [sb]: ' V' },
  IN_BEARBEITUNG_SB /**                */: { [gs]: '  ', [sb]: 'WV' },
  IN_FREIGABE /**                      */: { [gs]: '  ', [sb]: ' V' },
  ABKLAERUNG_DURCH_RECHSTABTEILUNG /** */: { [gs]: '  ', [sb]: ' V' },
  ANSPRUCH_MANUELL_PRUEFEN /**         */: { [gs]: '  ', [sb]: ' V' },
  // TODO KSTIP-1599: Fehlende Dokumente -> GS+SB nur readonly, GS kann Dokumente hochladen
  FEHLENDE_DOKUMENTE /**               */: { [gs]: 'W ', [sb]: ' V' },
  GESUCH_ABGELEHNT /**                 */: { [gs]: '  ', [sb]: ' V' },
  JURISTISCHE_ABKLAERUNG /**           */: { [gs]: '  ', [sb]: ' V' },
  KEIN_STIPENDIENANSPRUCH /**          */: { [gs]: '  ', [sb]: ' V' },
  NICHT_ANSPRUCHSBERECHTIGT /**        */: { [gs]: '  ', [sb]: ' V' },
  NICHT_BEITRAGSBERECHTIGT /**         */: { [gs]: '  ', [sb]: ' V' },
  STIPENDIENANSPRUCH /**               */: { [gs]: '  ', [sb]: ' V' },
  WARTEN_AUF_UNTERSCHRIFTENBLATT /**   */: { [gs]: '  ', [sb]: ' V' },
  VERSANDBEREIT /**                    */: { [gs]: '  ', [sb]: ' V' },
  VERFUEGT /**                         */: { [gs]: '  ', [sb]: ' V' },
  VERSENDET /**                        */: { [gs]: '  ', [sb]: ' V' },
} as const satisfies Record<Gesuchstatus, Record<AppType, Permissions>>;

/**
 * Same as `readWriteStatusByAppType` but for the tranches
 *
 * @see {@link readWriteStatusByAppType}
 */
export const trancheReadWritestatusByAppType = {
  IN_BEARBEITUNG_GS: /**  */ { [gs]: 'W ', [sb]: '  ' },
  UEBERPRUEFEN: /**       */ { [gs]: '  ', [sb]: 'W ' },
  AKZEPTIERT: /**         */ { [gs]: '  ', [sb]: '  ' },
  ABGELEHNT: /**          */ { [gs]: 'W ', [sb]: '  ' },
  MANUELLE_AENDERUNG: /** */ { [gs]: '  ', [sb]: '  ' },
} as const satisfies Record<GesuchTrancheStatus, Record<AppType, Permissions>>;

export const isGesuchReadonly = (
  gesuch: SharedModelGesuch | null,
  appType?: AppType,
) => {
  if (!gesuch || !appType) return false;

  const state = readWriteStatusByAppType[gesuch.gesuchStatus][appType];
  return !_canWrite(state);
};

export const canViewVerfuegung = (
  gesuch: SharedModelGesuch | null,
  appType?: AppType,
) => {
  if (!gesuch || !appType) return false;

  const state = readWriteStatusByAppType[gesuch.gesuchStatus][appType];
  return _canViewVerfuegung(state);
};

export const isTrancheReadonly = (
  tranche: GesuchTranche | null,
  appType?: AppType,
) => {
  if (!tranche || !appType) return false;

  const state = trancheReadWritestatusByAppType[tranche.status][appType];
  return !_canWrite(state);
};
