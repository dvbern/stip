package ch.dvbern.stip.test.arch.util;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;

public final class ArchTestUtil {

    public static JavaClasses APP_CLASSES = new ClassFileImporter()
            .importPackages("ch.dvbern.stip.api");
}
