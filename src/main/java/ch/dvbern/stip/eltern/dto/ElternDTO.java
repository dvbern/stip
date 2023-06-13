package ch.dvbern.stip.eltern.dto;

import ch.dvbern.stip.eltern.model.Eltern;
import ch.dvbern.stip.shared.enums.Anrede;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.time.LocalDate;
import java.util.UUID;

@Value
public class ElternDTO {

    @NotNull
    private UUID id;

    @NotNull
    private String sozialversicherungsnummer;

    @NotNull
    private String name;

    @NotNull
    private String vorname;

    @NotNull
    private Anrede geschlecht;

    @NotNull
    private String telefonnummer;

    @NotNull
    private LocalDate geburtsdatum;

    @NotNull
    private Boolean sozialhilfebeitraegeAusbezahlt;

    @NotNull
    private Boolean ausweisbFluechtling;

    @NotNull
    private Boolean ergaenzungsleistungAusbezahlt;

    public static ElternDTO from(Eltern changed) {
        return new ElternDTO(changed.getId(), changed.getSozialversicherungsnummer(), changed.getName(), changed.getVorname(), changed.getGeschlecht(), changed.getTelefonnummer(), changed.getGeburtsdatum(), changed.getSozialhilfebeitraegeAusbezahlt(), changed.getAusweisbFluechtling(), changed.getErgaenzungsleistungAusbezahlt());
    }

    public void apply(Eltern eltern) {
        eltern.setName(name);
        eltern.setVorname(vorname);
        eltern.setSozialversicherungsnummer(sozialversicherungsnummer);
        eltern.setGeburtsdatum(geburtsdatum);
        eltern.setGeschlecht(geschlecht);
        eltern.setTelefonnummer(telefonnummer);
        eltern.setAusweisbFluechtling(ausweisbFluechtling);
        eltern.setErgaenzungsleistungAusbezahlt(ergaenzungsleistungAusbezahlt);
        eltern.setSozialhilfebeitraegeAusbezahlt(sozialhilfebeitraegeAusbezahlt);
    }
}
