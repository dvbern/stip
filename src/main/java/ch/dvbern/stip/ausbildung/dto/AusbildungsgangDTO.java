package ch.dvbern.stip.ausbildung.dto;

import ch.dvbern.stip.ausbildung.model.Ausbildungsgang;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.UUID;

@Value
public class AusbildungsgangDTO {

    @NotNull
    private UUID id;

    @NotNull
    private String bezeichnungDe;

    private String bezeichnungFr;

    public static AusbildungsgangDTO from(Ausbildungsgang ausbildungsgang) {
        return new AusbildungsgangDTO(ausbildungsgang.getId(),ausbildungsgang.getBezeichnungDe(),ausbildungsgang.getBezeichnungFr());
    }
}
