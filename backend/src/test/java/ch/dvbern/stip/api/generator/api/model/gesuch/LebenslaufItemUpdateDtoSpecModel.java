package ch.dvbern.stip.api.generator.api.model.gesuch;

import java.time.LocalDate;
import java.util.List;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.LebenslaufAusbildungsArtDtoSpec;
import ch.dvbern.stip.generated.dto.LebenslaufItemUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.WohnsitzKantonDtoSpec;

import static ch.dvbern.stip.api.util.TestUtil.DATE_TIME_FORMATTER;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

public final class LebenslaufItemUpdateDtoSpecModel {
    public static List<LebenslaufItemUpdateDtoSpec> lebenslaufItemUpdateDtoSpecs() {
        return TestUtil.createUpdateDtoSpecs(LebenslaufItemUpdateDtoSpec::new, (model) -> {
            model.setWohnsitz(TestUtil.getRandomElementFromArray(WohnsitzKantonDtoSpec.values()));
            model.setAusbildungAbgeschlossen(true);
            model.setFachrichtung("Testrichtung");
            model.setTaetigkeitsBeschreibung("Ein Test");
            model.setVon(LocalDate.now().withMonth(8).withDayOfMonth(1).minusYears(1).format(DATE_TIME_FORMATTER));
            model.setBis(LocalDate.now().with(lastDayOfYear()).format(DATE_TIME_FORMATTER));
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
