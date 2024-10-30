package ch.dvbern.stip.api.generator.api.model.gesuch;

import java.util.List;

import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.LebenslaufAusbildungsArtDtoSpec;
import ch.dvbern.stip.generated.dto.LebenslaufItemUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.WohnsitzKantonDtoSpec;

import static ch.dvbern.stip.api.util.TestConstants.GUELTIGKEIT_PERIODE_23_24;
import static ch.dvbern.stip.api.util.TestConstants.GUELTIGKEIT_PERIODE_FIXED;
import static ch.dvbern.stip.api.util.TestUtil.DATE_TIME_FORMATTER;

public final class LebenslaufItemUpdateDtoSpecModel {
    public static List<LebenslaufItemUpdateDtoSpec> lebenslaufItemUpdateDtoSpecs() {
        return TestUtil.createUpdateDtoSpecs(LebenslaufItemUpdateDtoSpec::new, (model) -> {
            final DateRange gueltigkeitsRange;
            if (GUELTIGKEIT_PERIODE_23_24 != null) {
                gueltigkeitsRange = GUELTIGKEIT_PERIODE_23_24;
            } else {
                gueltigkeitsRange = GUELTIGKEIT_PERIODE_FIXED;
            }
            model.setWohnsitz(TestUtil.getRandomElementFromArray(WohnsitzKantonDtoSpec.values()));
            model.setAusbildungAbgeschlossen(true);
            model.setFachrichtung("Testrichtung");
            model.setTaetigkeitsBeschreibung("Ein Test");
            model.setVon(gueltigkeitsRange.getGueltigAb().minusDays(1).minusYears(1).format(DATE_TIME_FORMATTER));
            model.setBis(gueltigkeitsRange.getGueltigAb().minusDays(1).format(DATE_TIME_FORMATTER));
//            model.setVon(LocalDate.now().withMonth(8).withDayOfMonth(1).minusYears(1).format(DATE_TIME_FORMATTER));
//            model.setBis(LocalDate.now().with(lastDayOfYear()).format(DATE_TIME_FORMATTER));
            model.setBildungsart(LebenslaufAusbildungsArtDtoSpec.MASTER);
        }, 1);
    }

    public static GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecLebenslauf() {
        return TestUtil.createUpdateDtoSpec(
            GesuchFormularUpdateDtoSpec::new,
            (model) -> model.setLebenslaufItems(lebenslaufItemUpdateDtoSpecs())
        );
    }
}
