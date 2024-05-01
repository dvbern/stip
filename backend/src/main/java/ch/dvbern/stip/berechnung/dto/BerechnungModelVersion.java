package ch.dvbern.stip.berechnung.dto;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BerechnungModelVersion {
    int value();
}
