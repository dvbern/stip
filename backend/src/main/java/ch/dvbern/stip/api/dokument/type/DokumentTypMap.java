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

    private final Map<Niederlassungsstatus, DokumentTyp> niederlassungsstatusDokumentTypMap = Map.of(
        Niederlassungsstatus.AUFENTHALTSBEWILLIGUNG_B, DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_B,
        Niederlassungsstatus.NIEDERLASSUNGSBEWILLIGUNG_C, DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_C,
        Niederlassungsstatus.FLUECHTLING, DokumentTyp.PERSON_NIEDERLASSUNGSSTATUS_COMPLETE
    );

//    private final Map<Class<?>, Map<Object, DokumentTyp>> mapOfMaps = Map.of(
//        Niederlassungsstatus.class, niederlassungsstatusDokumentTypMap
//    );

    public DokumentTyp getDokumentTypFromValue(final String value) {
        return valueToTypeMap.getOrDefault(value, null);
    }

//    public DokumentTyp foo(@Nonnull final Object value) {
//        if (mapOfMaps.containsKey(value.getClass())) {
//            return mapOfMaps.get(value.getClass()).getOrDefault(value, null);
//        }
//
//		if (value.getClass().equals(Niederlassungsstatus.class)) {
//			return niederlassungsstatusDokumentTypMap.getOrDefault(value, null);
//		}
//
//
//		throw new IllegalStateException("Unexpected value: " + value.getClass());
//	}
}
