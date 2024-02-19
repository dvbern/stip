package ch.dvbern.stip.arch.util;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption.Predefined;

public final class ArchTestUtil {

    public static JavaClasses APP_CLASSES = new ClassFileImporter()
        .withImportOption(Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("ch.dvbern.stip.api");
}
