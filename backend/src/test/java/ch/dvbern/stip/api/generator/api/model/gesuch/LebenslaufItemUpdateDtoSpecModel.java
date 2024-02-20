package ch.dvbern.stip.api.generator.api.model.gesuch;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.LebenslaufAusbildungsArtDtoSpec;
import ch.dvbern.stip.generated.dto.LebenslaufItemUpdateDtoSpec;
import org.instancio.Instancio;
import org.instancio.Model;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static org.instancio.Select.field;

public final class LebenslaufItemUpdateDtoSpecModel {

    public static final Model<List<LebenslaufItemUpdateDtoSpec>> lebenslaufItemUpdateDtoSpecModel =
        Instancio.ofList(LebenslaufItemUpdateDtoSpec.class).size(1)
            .ignore(field(LebenslaufItemUpdateDtoSpec::getId))
            .ignore(field(LebenslaufItemUpdateDtoSpec::getTaetigskeitsart))
            .set(
                field(LebenslaufItemUpdateDtoSpec::getVon),
                LocalDate.now().minusMonths(3).with(firstDayOfMonth())
                    .format(DateTimeFormatter.ofPattern("MM.yyyy", Locale.GERMAN))
            )
            .set(
                field(LebenslaufItemUpdateDtoSpec::getBis),
                LocalDate.now().minusMonths(2).with(lastDayOfMonth())
                    .format(DateTimeFormatter.ofPattern("MM.yyyy", Locale.GERMAN))
            )
            .set(field(LebenslaufItemUpdateDtoSpec::getBildungsart), LebenslaufAusbildungsArtDtoSpec.MASTER)
            .ignore(field(LebenslaufItemUpdateDtoSpec::getBerufsbezeichnung))
            .ignore(field(LebenslaufItemUpdateDtoSpec::getTitelDesAbschlusses))
            .toModel();

    public static final Model<GesuchFormularUpdateDtoSpec> gesuchFormularUpdateDtoSpecLebenslaufModel =
        Instancio.of(
                GesuchFormularUpdateDtoSpec.class)
            .set(
                field(GesuchFormularUpdateDtoSpec::getLebenslaufItems),
                Instancio.create(lebenslaufItemUpdateDtoSpecModel)
            )
            .ignore(field(GesuchFormularUpdateDtoSpec::getFamiliensituation))
            .ignore(field(GesuchFormularUpdateDtoSpec::getElterns))
            .ignore(field(GesuchFormularUpdateDtoSpec::getAuszahlung))
            .ignore(field(GesuchFormularUpdateDtoSpec::getGeschwisters))
            .ignore(field(GesuchFormularUpdateDtoSpec::getEinnahmenKosten))
            .ignore(field(GesuchFormularUpdateDtoSpec::getAusbildung))
            .ignore(field(GesuchFormularUpdateDtoSpec::getPersonInAusbildung))
            .ignore(field(GesuchFormularUpdateDtoSpec::getKinds))
            .ignore(field(GesuchFormularUpdateDtoSpec::getPartner))
            .toModel();
}
