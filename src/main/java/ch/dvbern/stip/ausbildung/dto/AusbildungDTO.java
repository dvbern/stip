package ch.dvbern.stip.ausbildung.dto;

import ch.dvbern.stip.ausbildung.model.Aubildungsland;
import ch.dvbern.stip.ausbildung.model.AusbildungsPensum;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.time.LocalDate;
import java.util.UUID;

@Value
public class AusbildungDTO {

    @NotNull
    private UUID id;

    private AusbildungsgangDTO ausbildungsgang;

    private AusbildungstaetteDTO ausbildungstaette;

    @NotNull
    private Aubildungsland ausbildungsland;

    private String alternativeAusbildungsgang;

    private String AlternativeAusbildungstaette;

    @NotNull
    private String fachrichtung;

    private boolean ausbildungNichtGefunden;

    @NotNull
    private LocalDate ausbildungBegin;

    @NotNull
    private LocalDate ausbildungEnd;

    @NotNull
    private AusbildungsPensum pensum;
}
