import {
  AppType,
  BusinessAppType,
  ensureIsBusinessAppType,
} from '@dv/shared/model/config';
import {
  GesuchTrancheStatus,
  Gesuchstatus,
  SharedModelGesuch,
} from '@dv/shared/model/gesuch';

export type AbschlussPhase =
  | 'NOT_READY'
  | 'FEHLENDE_DOKUMENTE'
  | 'READY_TO_SEND'
  | 'SUBMITTED'
  | 'AKZETPIERT'
  | 'ABGELEHNT';

const gs = 'gesuch-app' satisfies BusinessAppType;
const sb = 'sachbearbeitung-app' satisfies BusinessAppType;
const ___ = null;
const S__ = 'SUBMITTED';
const _R_ = 'READY_TO_SEND';
const __F = 'FEHLENDE_DOKUMENTE';

const AbschlussPhaseMap = {
  GESUCH_IN_BEARBEITUNG_GS /**                */: { [gs]: _R_, [sb]: ___ },
  GESUCH_EINGEREICHT /**                      */: { [gs]: S__, [sb]: ___ },
  GESUCH_ANSPRUCH_PRUEFEN /**                 */: { [gs]: S__, [sb]: ___ },
  GESUCH_BEREIT_FUER_BEARBEITUNG /**          */: { [gs]: S__, [sb]: ___ },
  GESUCH_IN_BEARBEITUNG_SB /**                */: { [gs]: S__, [sb]: _R_ },
  GESUCH_IN_FREIGABE /**                      */: { [gs]: S__, [sb]: S__ },
  GESUCH_ABKLAERUNG_DURCH_RECHSTABTEILUNG /** */: { [gs]: S__, [sb]: S__ },
  GESUCH_ANSPRUCH_MANUELL_PRUEFEN /**         */: { [gs]: S__, [sb]: S__ },
  GESUCH_FEHLENDE_DOKUMENTE /**               */: { [gs]: __F, [sb]: S__ },
  GESUCH_GESUCH_ABGELEHNT /**                 */: { [gs]: S__, [sb]: S__ },
  GESUCH_JURISTISCHE_ABKLAERUNG /**           */: { [gs]: S__, [sb]: S__ },
  GESUCH_KEIN_STIPENDIENANSPRUCH /**          */: { [gs]: S__, [sb]: S__ },
  GESUCH_NICHT_ANSPRUCHSBERECHTIGT /**        */: { [gs]: S__, [sb]: S__ },
  GESUCH_NICHT_BEITRAGSBERECHTIGT /**         */: { [gs]: S__, [sb]: S__ },
  GESUCH_STIPENDIENANSPRUCH /**               */: { [gs]: S__, [sb]: S__ },
  GESUCH_WARTEN_AUF_UNTERSCHRIFTENBLATT /**   */: { [gs]: S__, [sb]: S__ },
  GESUCH_VERFUEGT /**                         */: { [gs]: S__, [sb]: S__ },
  GESUCH_NEGATIVE_VERFUEGUNG /**              */: { [gs]: S__, [sb]: S__ },
  GESUCH_VERFUEGUNG_AM_GENERIEREN /**         */: { [gs]: S__, [sb]: S__ },
  GESUCH_VERFUEGUNG_DRUCKBEREIT /**           */: { [gs]: S__, [sb]: S__ },
  GESUCH_VERFUEGUNG_VERSANDBEREIT /**         */: { [gs]: S__, [sb]: S__ },
  GESUCH_VERFUEGUNG_VERSENDET /**             */: { [gs]: S__, [sb]: S__ },
  GESUCH_DATENSCHUTZBRIEF_AM_GENERIEREN /**   */: { [gs]: S__, [sb]: S__ },
  GESUCH_DATENSCHUTZBRIEF_DRUCKBEREIT /**     */: { [gs]: S__, [sb]: S__ },
  GESUCH_DATENSCHUTZBRIEF_VERSANDBEREIT /**   */: { [gs]: S__, [sb]: S__ },
  // ---------------------------------------------------------------------
  TRANCHE_IN_BEARBEITUNG_GS /**               */: { [gs]: _R_, [sb]: ___ },
  TRANCHE_UEBERPRUEFEN /**                    */: { [gs]: S__, [sb]: _R_ },
  TRANCHE_FEHLENDE_DOKUMENTE /**              */: { [gs]: __F, [sb]: S__ },
  TRANCHE_ABGELEHNT /**                       */: { [gs]: S__, [sb]: S__ },
  TRANCHE_AKZEPTIERT /**                      */: { [gs]: S__, [sb]: S__ },
  TRANCHE_MANUELLE_AENDERUNG /**              */: { [gs]: S__, [sb]: S__ },
} satisfies Record<
  `${`GESUCH_${Gesuchstatus}` | `TRANCHE_${GesuchTrancheStatus}`}`,
  Record<BusinessAppType, AbschlussPhase | null>
>;

export const toAbschlussPhase = (
  gesuch: SharedModelGesuch | null,
  options: {
    appType?: AppType;
    isComplete?: boolean;
    checkAenderung?: boolean;
  },
): AbschlussPhase | null => {
  const appType = options?.appType;
  if (!gesuch || !appType) {
    return 'NOT_READY';
  }
  ensureIsBusinessAppType(appType);
  const key = options.checkAenderung
    ? (`TRANCHE_${gesuch.gesuchTrancheToWorkWith.status}` as const)
    : (`GESUCH_${gesuch.gesuchStatus}` as const);
  const phase = AbschlussPhaseMap[key]?.[appType];

  if (!phase) return null;

  if (phase === 'READY_TO_SEND' && !options?.isComplete) {
    return 'NOT_READY';
  }

  if (phase === 'FEHLENDE_DOKUMENTE' && options?.isComplete) {
    return 'READY_TO_SEND';
  }

  return phase;
};
