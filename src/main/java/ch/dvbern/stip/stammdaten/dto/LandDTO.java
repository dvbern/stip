package ch.dvbern.stip.stammdaten.dto;

import ch.dvbern.stip.shared.enums.Land;
import lombok.Value;

@Value

public class LandDTO {

    private String landCode;

    private String landDE;

    private String landFR;

    public static LandDTO from(Land land) {
        return new LandDTO(land.name(), land.getLandDE(), land.getLandFR());
    }
}
