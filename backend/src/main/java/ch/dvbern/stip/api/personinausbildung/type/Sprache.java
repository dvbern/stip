package ch.dvbern.stip.api.personinausbildung.type;

import lombok.Getter;

import java.util.Locale;

@Getter
public enum Sprache {
    DEUTSCH(Locale.GERMAN),
    FRANZOESISCH(Locale.FRENCH);

    private final Locale locale;

    Sprache(final Locale locale) {
        this.locale = locale;
    }
}
