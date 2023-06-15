package ch.dvbern.stip.dokument.dto;

import ch.dvbern.stip.dokument.model.DokumentTyp;
import ch.dvbern.stip.dokument.model.GesuchDokument;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.List;

@Value
public class GesuchDokumentDTO {

    @NotNull
    private DokumentTyp dokumentTyp;

    private List<DokumentDTO> dokumente;

    public static GesuchDokumentDTO from(GesuchDokument gesuchDokument) {
        return new GesuchDokumentDTO(gesuchDokument.getDokumentTyp(), gesuchDokument.getDokumente().stream().map(dokument -> DokumentDTO.from(dokument)).toList());
    }
}
