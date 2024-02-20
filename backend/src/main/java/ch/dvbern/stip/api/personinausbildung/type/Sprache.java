package ch.dvbern.stip.api.personinausbildung.type;

import java.util.Locale;

import lombok.Getter;

@Getter
public enum Sprache {
    DEUTSCH(Locale.GERMAN),
    FRANZOESISCH(Locale.FRENCH);

    private final Locale locale;

    Sprache(final Locale locale) {
        this.locale = locale;
    }
}
