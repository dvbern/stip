package ch.dvbern.stip.api.generator.api.model.gesuch;

import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.AusbildungUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.AusbildungsPensumDtoSpec;

import static ch.dvbern.stip.api.util.TestConstants.GUELTIGKEIT_PERIODE_23_24;
import static ch.dvbern.stip.api.util.TestConstants.GUELTIGKEIT_PERIODE_FIXED;
import static ch.dvbern.stip.api.util.TestUtil.DATE_TIME_FORMATTER;

public final class AusbildungUpdateDtoSpecModel {
    public static AusbildungUpdateDtoSpec ausbildungUpdateDtoSpec() {
        final DateRange gueltigkeitsRange;
        if (GUELTIGKEIT_PERIODE_23_24 != null) {
            gueltigkeitsRange = GUELTIGKEIT_PERIODE_23_24;
        } else {
            gueltigkeitsRange = GUELTIGKEIT_PERIODE_FIXED;
        }
        return TestUtil.createUpdateDtoSpec(AusbildungUpdateDtoSpec::new, (model) -> {
            model.setAusbildungBegin(gueltigkeitsRange.getGueltigAb().format(DATE_TIME_FORMATTER));
            model.setAusbildungEnd(gueltigkeitsRange.getGueltigBis().format(DATE_TIME_FORMATTER));
            model.setAusbildungNichtGefunden(false);
            model.setAusbildungsgangId(TestConstants.TEST_AUSBILDUNGSGANG_ID);
            model.setFachrichtung("Informatik");
            model.setPensum(TestUtil.getRandomElementFromArray(AusbildungsPensumDtoSpec.values()));
            model.setIsAusbildungAusland(false);
            model.setAusbildungsort("Bern");
        });
    }

//    public static GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecAusbildung() {
//        return TestUtil.createUpdateDtoSpec(
//            GesuchFormularUpdateDtoSpec::new,
//            (model) -> model.setAusbildung(ausbildungUpdateDtoSpec())
//        );
//    }
}
