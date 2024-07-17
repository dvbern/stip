import { Gesuchstatus, SharedModelGesuch } from '@dv/shared/model/gesuch';

// Define States in which the gesuch is readonly for the GS
export const gesuchStellerReadonlyStatusMap: Record<Gesuchstatus, boolean> = {
  ABKLAERUNG_DURCH_RECHSTABTEILUNG: true,
  ANSPRUCH_MANUELL_PRUEFEN: true,
  BEREIT_FUER_BEARBEITUNG: true,
  EINGEREICHT: true,
  FEHLENDE_DOKUMENTE: false,
  GESUCH_ABGELEHNT: true,
  IN_BEARBEITUNG_GS: false,
  IN_BEARBEITUNG_SB: true,
  IN_FREIGABE: true,
  JOUR_FIX: true,
  KEIN_STIPENDIENANSPRUCH: true,
  NICHT_ANSPRUCHSBERECHTIGT: true,
  NICHT_BEITRAGSBERECHTIGT: true,
  STIPENDIENANSPRUCH: true,
  VERSANDBEREIT: true,
  VERFUEGT: true,
  VERSENDET: true,
  WARTEN_AUF_UNTERSCHRIFTENBLATT: true,
};

// Define States in which the gesuch is readonly for the SB
export const gesuchSachbearbeiterReadonlyStatusMap: Record<
  Gesuchstatus,
  boolean
> = {
  ABKLAERUNG_DURCH_RECHSTABTEILUNG: true,
  ANSPRUCH_MANUELL_PRUEFEN: true,
  BEREIT_FUER_BEARBEITUNG: true,
  EINGEREICHT: true,
  FEHLENDE_DOKUMENTE: true,
  GESUCH_ABGELEHNT: true,
  IN_BEARBEITUNG_GS: true,
  IN_BEARBEITUNG_SB: false,
  IN_FREIGABE: true,
  JOUR_FIX: true,
  KEIN_STIPENDIENANSPRUCH: true,
  NICHT_ANSPRUCHSBERECHTIGT: true,
  NICHT_BEITRAGSBERECHTIGT: true,
  STIPENDIENANSPRUCH: true,
  VERSANDBEREIT: true,
  VERFUEGT: true,
  VERSENDET: true,
  WARTEN_AUF_UNTERSCHRIFTENBLATT: true,
};

export const setGesuchFromularReadonly = (
  gesuch: SharedModelGesuch | null,
  isGesuchApp: boolean,
  isSachbearbeitungApp: boolean,
) => {
  if (!gesuch) return false;

  if (isGesuchApp) {
    return gesuchStellerReadonlyStatusMap[gesuch.gesuchStatus];
  }

  if (isSachbearbeitungApp) {
    return gesuchSachbearbeiterReadonlyStatusMap[gesuch.gesuchStatus];
  }

  throw new Error('setReadOnly: Unknown gesuchStatus');
};
