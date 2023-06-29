package ch.dvbern.stip.arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;

public final class ArchTestUtil {

    public static JavaClasses ALL_CLASSES = new ClassFileImporter().importPackages("ch.dvbern.stip");
}
