package ch.dvbern.stip.berechnung.dto.v1;

import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@Value
public class PersonInAusbildungV1 {
    String wohnsitz;
    Integer wohnsitzAnteilVater;
    Integer wohnsitzAnteilMutter;

    public static PersonInAusbildungV1 fromPersonInAusbildung(PersonInAusbildung personInAusbildung) {
        return new PersonInAusbildungV1Builder()
            .wohnsitz(personInAusbildung.getWohnsitz().toString())
            .wohnsitzAnteilVater(personInAusbildung.getWohnsitzAnteilVater() != null ? personInAusbildung.getWohnsitzAnteilVater().intValue() : null)
            .wohnsitzAnteilMutter(personInAusbildung.getWohnsitzAnteilMutter() != null ? personInAusbildung.getWohnsitzAnteilMutter().intValue() : null)
            .build();
    }
}
