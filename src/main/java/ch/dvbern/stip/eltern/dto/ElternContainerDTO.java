package ch.dvbern.stip.eltern.dto;

import ch.dvbern.stip.eltern.model.ElternContainer;
import ch.dvbern.stip.personinausbildung.model.PersonInAusbildungContainer;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.UUID;

@Value
public class ElternContainerDTO {

    //TODO Gesuch ID or not ? Pass ID des Gesuchs in Service ?
    // Resource would take as parameter for getall gesuch id
    // save will take the dto and the gesuch id as parameter

    @NotNull
    private UUID id;

    private ElternDTO elternGS;

    private ElternDTO elternSB;

    public static ElternContainerDTO from(ElternContainer changed) {
        return changed == null ? null : new ElternContainerDTO(changed.getId(), ElternDTO.from(changed.getElternGS()), ElternDTO.from(changed.getElternSB()));
    }

    public void apply(PersonInAusbildungContainer personInAusbildungContainer) {
        //Todo noch bestimmen was man genau hier kopiert, GS sollte aus meiner Sicht gar nicht vorkommen
    }
}
