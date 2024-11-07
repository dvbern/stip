/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

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

    private FileUtil() {}

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
