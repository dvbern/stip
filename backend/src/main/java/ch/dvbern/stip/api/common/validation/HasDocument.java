package ch.dvbern.stip.api.common.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ch.dvbern.stip.api.dokument.type.DokumentTyp;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface HasDocument {
    DokumentTyp[] dokumentTyp();
}
