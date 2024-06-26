package ch.dvbern.stip.api.common.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.common.exception.ValidationsException;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;

public final class FileUtil {

    private FileUtil() {
    }

    public static String generateUUIDWithFileExtension(String filename) {
        return UUID.randomUUID() + "." + FilenameUtils.getExtension(filename);
    }

    public static boolean checkFileExtensionAllowed(final Path filePath, final Set<String> extensions) {
        Tika tika = new Tika();
        try {
            String mimeType = tika.detect(filePath);
            if (extensions.contains(mimeType)) {
                return true;
            }
        } catch (IOException e) {
            throw new ValidationsException("checkFileExtension failed: " + e.getMessage(), null);
        }
        return false;
    }

    public static String getFileMimeType(File file) {
        Tika tika = new Tika();
        return tika.detect(file.getPath());
    }

}
