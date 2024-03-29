package ch.dvbern.stip.api.gesuch.type;

import lombok.Getter;

@Getter
public enum GesuchStatusChangeEvent {
    EINREICHEN,
    FEHLERHAFT_EINGEREICHT,
    BEREIT_FUER_BEARBEITUNG,
    NEGATIVER_ENTSCHEID,
    IN_BEARBEITUNG,
    IN_FREIGABE,
    IN_REVIEW,
    ABKLAERUNG_MIT_GS,
    FEHLENDE_DOKUMENTE,
    FEHLENDE_DOKUMENTE_NACHFRIST,
    ZURUECKGEZOGEN,
    ZURUECKGEWIESEN,
    WARTEN_AUF_UNTERSCHRIFTENBLATT,
    VERFUEGT,
    STIPENDIUM_AKZEPTIERT,
    STIPENDIUM_AUSBEZAHLT
}
