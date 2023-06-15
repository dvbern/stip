package ch.dvbern.stip.dokument.dto;

import jakarta.ws.rs.core.MediaType;
import lombok.Value;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;

import java.io.File;

@Value
public class DownloadFileData {

    @RestForm
    String name;

    @RestForm
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    File file;
}
