package ch.dvbern.stip.api.generator.api.model.gesuch;

import java.time.LocalDate;
import java.util.List;

import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.generated.dto.ElternTypDtoSpec;
import ch.dvbern.stip.generated.dto.ElternUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;
import org.instancio.Assign;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.When;

import static org.instancio.Select.field;

public class ElternUpdateDtoSpecModel {

    public static final Model<List<ElternUpdateDtoSpec>> elternUpdateDtoSpecModel =
        Instancio.ofList(ElternUpdateDtoSpec.class).size(1)
            .ignore(field(ElternUpdateDtoSpec::getId))
            .set(field(ElternUpdateDtoSpec::getAdresse), Instancio.create(AdresseSpecModel.adresseSpecModel))
            .set(
                field(ElternUpdateDtoSpec::getSozialversicherungsnummer),
                TestConstants.AHV_NUMMER_VALID_VATTER
            )
            .set(field(ElternUpdateDtoSpec::getElternTyp), ElternTypDtoSpec.VATER)
            .generate(
                field(ElternUpdateDtoSpec::getGeburtsdatum),
                gen -> gen.temporal().localDate().range(LocalDate.of(1920, 1, 1), LocalDate.of(1980, 1,
                    1
                ))
            )
            .assign(Assign.given(
                    field(ElternUpdateDtoSpec::getIdentischerZivilrechtlicherWohnsitz),
                    field(ElternUpdateDtoSpec::getIdentischerZivilrechtlicherWohnsitzOrt))
                .set(When.is(false), "Bern")
                .set(When.is(true), null))
            .assign(Assign.given(
                    field(ElternUpdateDtoSpec::getIdentischerZivilrechtlicherWohnsitz),
                    field(ElternUpdateDtoSpec::getIdentischerZivilrechtlicherWohnsitzPLZ))
                .set(When.is(false), "3000")
                .set(When.is(true), null))
            .toModel();

    public static final Model<GesuchFormularUpdateDtoSpec> gesuchFormularUpdateDtoSpecElternsModel =
        Instancio.of(
                GesuchFormularUpdateDtoSpec.class)
            .set(
                field(GesuchFormularUpdateDtoSpec::getElterns),
                Instancio.create(elternUpdateDtoSpecModel)
            )
            .ignore(field(GesuchFormularUpdateDtoSpec::getFamiliensituation))
            .ignore(field(GesuchFormularUpdateDtoSpec::getGeschwisters))
            .ignore(field(GesuchFormularUpdateDtoSpec::getAuszahlung))
            .ignore(field(GesuchFormularUpdateDtoSpec::getLebenslaufItems))
            .ignore(field(GesuchFormularUpdateDtoSpec::getEinnahmenKosten))
            .ignore(field(GesuchFormularUpdateDtoSpec::getAusbildung))
            .ignore(field(GesuchFormularUpdateDtoSpec::getPersonInAusbildung))
            .ignore(field(GesuchFormularUpdateDtoSpec::getKinds))
            .ignore(field(GesuchFormularUpdateDtoSpec::getPartner))
            .toModel();
}
