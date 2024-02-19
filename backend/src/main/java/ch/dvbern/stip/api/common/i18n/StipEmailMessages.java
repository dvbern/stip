package ch.dvbern.stip.api.common.i18n;

import lombok.Getter;

@Getter
public enum StipEmailMessages {

    FEHLENDE_DOKUMENTE_SUBJECT("mail_subject_fehlende_dokumente"),
    NICHT_KOMPLTETT_EINGEREICHT_NACHFRIST_SUBJECT("mail_subject_nicht_komplett_eingreicht_nachfrist");


    private final String message;

    StipEmailMessages(String message) {
        this.message = message;
    }
}
