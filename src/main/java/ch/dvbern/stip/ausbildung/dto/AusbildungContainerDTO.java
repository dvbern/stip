package ch.dvbern.stip.ausbildung.dto;

import ch.dvbern.stip.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.ausbildung.entity.AusbildungContainer;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.UUID;

@Value
public class AusbildungContainerDTO {
    @NotNull
    private UUID id;

    private AusbildungDTO ausbildungGS;

    private AusbildungDTO ausbildungSB;

    public static AusbildungContainerDTO from(AusbildungContainer ausbildungContainer) {
        return ausbildungContainer == null ? null : new AusbildungContainerDTO(ausbildungContainer.getId(), AusbildungDTO.from(ausbildungContainer.getAusbildungGS()), AusbildungDTO.from(ausbildungContainer.getAusbildungSB()));

    }

    public void apply(AusbildungContainer ausbildungContainer) {
        Ausbildung ausbildungSBFromGesuch = ausbildungContainer.getAusbildungSB() != null ? ausbildungContainer.getAusbildungSB() : new Ausbildung();
        ausbildungSB.apply(ausbildungSBFromGesuch);
        ausbildungContainer.setAusbildungSB(ausbildungSBFromGesuch);
    }
}