package ch.dvbern.stip.api.dokument.type;

import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;

import java.io.File;

public class FormData {
	@RestForm("file")
	public File data;

	@RestForm
	@PartType(MediaType.TEXT_PLAIN)
	public String filename;

	@RestForm
	@PartType(MediaType.TEXT_PLAIN)
	public String mimetype;

}
