package ch.dvbern.stip.ausbildung.dto;

import ch.dvbern.stip.ausbildung.model.Aubildungsland;
import ch.dvbern.stip.ausbildung.model.Ausbildungstaette;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
public class AusbildungstaetteDTO {

    @NotNull
    private UUID id;

    private List<AusbildungsgangDTO> ausbildungsgaenge;

    @NotNull
    private Aubildungsland ausbildungsland;

    @NotNull
    private String name;

    public static AusbildungstaetteDTO from(Ausbildungstaette ausbildungstaette) {
        return new AusbildungstaetteDTO(ausbildungstaette.getId(),
                ausbildungstaette.getAusbildungsgaenge().stream().map(ausbildungsgang -> AusbildungsgangDTO.from(ausbildungsgang)).toList(),
                ausbildungstaette.getAusbildungsland(),
                ausbildungstaette.getName());
    }
}
