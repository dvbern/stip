package ch.dvbern.stip.ausbildung.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.UUID;

@Value
public class AusbildungContainerDTO {
    @NotNull
    private UUID id;

    private AusbildungDTO ausbildungGS;

    private AusbildungDTO ausbildungSB;
}