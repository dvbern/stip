package ch.dvbern.stip.dokument.dto;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.util.List;

public final class MultipartBody {
    @Schema(type = SchemaType.STRING, implementation = String.class, format = "binary")
    public static class UploadItemSchema {

    }
    @Schema(implementation = UploadItemSchema[].class)
    @RestForm("files")
    public List<FileUpload> files;
}
