package ch.dvbern.stip.api.common.i18n;

import lombok.Getter;

@Getter
public enum StipEmailMessages {
    EINGEREICHT("eingereicht");

    private final String message;

    StipEmailMessages(String message) {
        this.message = message;
    }
}
