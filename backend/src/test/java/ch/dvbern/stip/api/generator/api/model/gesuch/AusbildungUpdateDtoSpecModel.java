package ch.dvbern.stip.api.generator.api.model.gesuch;

import java.time.LocalDate;

import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.AusbildungUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.AusbildungsPensumDtoSpec;

import static ch.dvbern.stip.api.util.TestUtil.DATE_TIME_FORMATTER;
import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

public final class AusbildungUpdateDtoSpecModel {
    public static AusbildungUpdateDtoSpec ausbildungUpdateDtoSpec() {
        return TestUtil.createUpdateDtoSpec(AusbildungUpdateDtoSpec::new, (model) -> {
            model.setAusbildungBegin(LocalDate.now().plusYears(1).with(firstDayOfYear()).format(DATE_TIME_FORMATTER));
            model.setAusbildungEnd(LocalDate.now().plusYears(1).with(lastDayOfYear()).format(DATE_TIME_FORMATTER));
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
