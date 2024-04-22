package ch.dvbern.stip.api.generator.api.model.gesuch;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.LebenslaufAusbildungsArtDtoSpec;
import ch.dvbern.stip.generated.dto.LebenslaufItemUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.WohnsitzKantonDtoSpec;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

public final class LebenslaufItemUpdateDtoSpecModel {
    public static final List<LebenslaufItemUpdateDtoSpec> lebenslaufItemUpdateDtoSpecs =
        TestUtil.createUpdateDtoSpecs(LebenslaufItemUpdateDtoSpec::new, (model, faker) -> {
            model.setWohnsitz(TestUtil.getRandomElementFromArray(WohnsitzKantonDtoSpec.values()));
            model.setAusbildungAbgeschlossen(faker.bool().bool());
            model.setFachrichtung(faker.educator().course());
            model.setTaetigkeitsBeschreibung(faker.job().field());
            model.setVon(LocalDate.now().minusMonths(3).with(firstDayOfMonth())
                .format(DateTimeFormatter.ofPattern("MM.yyyy", Locale.GERMAN)));
            model.setBis(LocalDate.now().minusMonths(2).with(lastDayOfMonth())
                .format(DateTimeFormatter.ofPattern("MM.yyyy", Locale.GERMAN)));
            model.setBildungsart(LebenslaufAusbildungsArtDtoSpec.MASTER);
        }, 1);

    public static final GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecLebenslauf =
        TestUtil.createUpdateDtoSpec(GesuchFormularUpdateDtoSpec::new, (model, faker) -> model.setLebenslaufItems(lebenslaufItemUpdateDtoSpecs));
}
