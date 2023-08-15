package ch.dvbern.stip.api.common.util;

import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public final class FileUtil {

	private FileUtil() {
	}

	public static String generateUUIDWithFileExtension(String filename){
		return UUID.randomUUID() + "." + FilenameUtils.getExtension(filename);
	}

	public static boolean checkFileExtensionAllowed(Path filePath, List<String> extensions) {
		Tika tika = new Tika();
		try {
			String mimeType = tika.detect(filePath);
			if (extensions.contains(mimeType)) {
				return true;
			}
		}
		catch (IOException e) {
			throw new RuntimeException("checkFileExtension failed: " + e.getMessage());
		}
		return false;
	}

	public static String getFileMimeType(File file) {
		Tika tika = new Tika();
		return tika.detect(file.getPath());
	}

}
