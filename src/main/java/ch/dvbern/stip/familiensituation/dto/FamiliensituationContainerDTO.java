package ch.dvbern.stip.familiensituation.dto;

import ch.dvbern.stip.familiensituation.model.Familiensituation;
import ch.dvbern.stip.familiensituation.model.FamiliensituationContainer;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.UUID;

@Value
public class FamiliensituationContainerDTO {
    @NotNull
    private UUID id;

    private FamiliensituationDTO familiensituationGS;

    private FamiliensituationDTO familiensituationSB;

    public static FamiliensituationContainerDTO from(FamiliensituationContainer familiensituationContainer) {
        return familiensituationContainer == null ? null : new FamiliensituationContainerDTO(familiensituationContainer.getId(),
                FamiliensituationDTO.from(familiensituationContainer.getFamiliensituationGS()),
                FamiliensituationDTO.from(familiensituationContainer.getFamiliensituationSB()));
    }

    public void apply(FamiliensituationContainer familiensituationContainer) {
        Familiensituation familiensituation = familiensituationContainer.getFamiliensituationSB() != null ? familiensituationContainer.getFamiliensituationSB() : new Familiensituation();
        familiensituationSB.apply(familiensituation);
        familiensituationContainer.setFamiliensituationSB(familiensituation);
    }
}
