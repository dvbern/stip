package ch.dvbern.stip.api.generator.api.model.gesuch;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.AusbildungUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.AusbildungsPensumDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.LandDtoSpec;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

public final class AusbildungUpdateDtoSpecModel {
    public static final AusbildungUpdateDtoSpec ausbildungUpdateDtoSpec =
        TestUtil.createUpdateDtoSpec(AusbildungUpdateDtoSpec::new, (model, faker) -> {
            model.setAusbildungBegin(LocalDate.now().minusMonths(1).with(firstDayOfMonth())
                .format(DateTimeFormatter.ofPattern("MM.yyyy", Locale.GERMAN)));
            model.setAusbildungEnd(LocalDate.now().plusMonths(1).with(lastDayOfMonth())
                .format(DateTimeFormatter.ofPattern("MM.yyyy", Locale.GERMAN)));
            model.setAusbildungNichtGefunden(false);
            model.setAusbildungsgangId(UUID.fromString("3a8c2023-f29e-4466-a2d7-411a7d032f42"));
            model.setFachrichtung(faker.educator().course());
            model.setPensum(TestUtil.getRandomElementFromArray(AusbildungsPensumDtoSpec.values()));
            model.setAlternativeAusbildungsland(TestUtil.getRandomElementFromArray(LandDtoSpec.values()).getValue());
        });

    public static final GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecAusbildung =
        TestUtil.createUpdateDtoSpec(GesuchFormularUpdateDtoSpec::new, (model, faker) -> model.setAusbildung(ausbildungUpdateDtoSpec));
}
