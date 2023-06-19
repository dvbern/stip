package ch.dvbern.stip.ausbildung.dto;


import ch.dvbern.stip.ausbildung.model.Ausbildung;
import ch.dvbern.stip.ausbildung.model.AusbildungsPensum;
import ch.dvbern.stip.ausbildung.model.Ausbildungsland;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.time.LocalDate;
import java.util.UUID;

@Value
public class AusbildungDTO {

    @NotNull
    private UUID id;

    @NotNull
    private UUID ausbildungsgangId;

    @NotNull
    private UUID ausbildungstaetteId;

    @NotNull
    private Ausbildungsland ausbildungsland;

    private String alternativeAusbildungsgang;

    private String alternativeAusbildungstaette;

    @NotNull
    private String fachrichtung;

    private boolean ausbildungNichtGefunden;

    @NotNull
    private LocalDate ausbildungBegin;

    @NotNull
    private LocalDate ausbildungEnd;

    @NotNull
    private AusbildungsPensum pensum;

    public static AusbildungDTO from(Ausbildung ausbildung) {
        return ausbildung == null ? null : new AusbildungDTO(ausbildung.getId(),
                ausbildung.getAusbildungsgang().getId(),
                ausbildung.getAusbildungstaette().getId(),
                ausbildung.getAusbildungsland(),
                ausbildung.getAlternativeAusbildungsgang(),
                ausbildung.getAlternativeAusbildungstaette(),
                ausbildung.getFachrichtung(),
                ausbildung.isAusbildungNichtGefunden(),
                ausbildung.getAusbildungBegin(),
                ausbildung.getAusbildungEnd(),
                ausbildung.getPensum());
    }

    public void apply(Ausbildung ausbildung) {
        ausbildung.setAusbildungEnd(ausbildungEnd);
        ausbildung.setAusbildungBegin(ausbildungBegin);
        ausbildung.setAusbildungNichtGefunden(ausbildungNichtGefunden);
        ausbildung.setPensum(pensum);
        ausbildung.setFachrichtung(fachrichtung);
        ausbildung.setAusbildungsland(ausbildungsland);
        ausbildung.setAlternativeAusbildungsgang(alternativeAusbildungsgang);
        ausbildung.setAlternativeAusbildungstaette(alternativeAusbildungstaette);
    }
}
