package ch.dvbern.stip.api.dokument.type;

import java.util.Map;

import ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DokumentTypMap {
    private final Map<String, DokumentTyp> valueToTypeMap = Map.of(
        Niederlassungsstatus.AUFENTHALTSBEWILLIGUNG_B.toString(), DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_B,
        Niederlassungsstatus.NIEDERLASSUNGSBEWILLIGUNG_C.toString(), DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_C,
        Niederlassungsstatus.FLUECHTLING.toString(), DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_COMPLETE
    );

    public DokumentTyp getDokumentTypFromValue(final String value) {
        return valueToTypeMap.getOrDefault(value, null);
    }
}
