package ch.dvbern.stip.test.arch.util;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.core.importer.Location;

public class ExcludeGeneratedClasses implements ImportOption {
    @Override
    public boolean includes(Location location) {
        return !location.contains("ch/dvbern/stip/generated");
    }
}
