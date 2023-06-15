package ch.dvbern.stip.dokument.dto;

import ch.dvbern.stip.dokument.model.Dokument;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class DokumentDTO {

    @NotNull
    private String filename;

    @NotNull
    private String filepfad;

    @NotNull
    private String filesize;

    public static DokumentDTO from(Dokument dokument) {
        return new DokumentDTO(dokument.getFilename(), dokument.getFilepfad(), dokument.getFilesize());
    }
}
