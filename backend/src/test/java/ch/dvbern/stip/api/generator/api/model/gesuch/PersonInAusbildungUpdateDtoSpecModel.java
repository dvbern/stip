package ch.dvbern.stip.api.generator.api.model.gesuch;

import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.LandDtoSpec;
import ch.dvbern.stip.generated.dto.PersonInAusbildungUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.WohnsitzDtoSpec;
import org.instancio.Assign;
import org.instancio.Instancio;
import org.instancio.Model;

import java.math.BigDecimal;
import java.time.LocalDate;

import static ch.dvbern.stip.api.util.TestConstants.AHV_NUMMER_VALID_PERSON_IN_AUSBILDUNG;
import static org.instancio.Select.field;

public final class PersonInAusbildungUpdateDtoSpecModel {

    public static final Model<PersonInAusbildungUpdateDtoSpec> personInAusbildungUpdateDtoSpecModel =
        Instancio.of(PersonInAusbildungUpdateDtoSpec.class)
            .set(field(PersonInAusbildungUpdateDtoSpec::getAdresse), Instancio.create(AdresseSpecModel.adresseSpecModel))
            .set(field(PersonInAusbildungUpdateDtoSpec::getIdentischerZivilrechtlicherWohnsitz), false)
            .set(field(PersonInAusbildungUpdateDtoSpec::getNationalitaet), LandDtoSpec.CH)
            .set(field(PersonInAusbildungUpdateDtoSpec::getWohnsitz), WohnsitzDtoSpec.MUTTER_VATER)
            .set(field(PersonInAusbildungUpdateDtoSpec::getNiederlassungsstatus), null)
            .set(field(PersonInAusbildungUpdateDtoSpec::getEmail), "valid@mailbucket.dvbern.ch")
            .set(
                field(PersonInAusbildungUpdateDtoSpec::getSozialversicherungsnummer),
                AHV_NUMMER_VALID_PERSON_IN_AUSBILDUNG
            )
            .generate(
                field(PersonInAusbildungUpdateDtoSpec::getVorname),
                gen -> gen.oneOf("Sarah", "Elijah", "Nu'ab", "André", "Özgür")
            )
            .generate(
                field(PersonInAusbildungUpdateDtoSpec::getGeburtsdatum),
                gen -> gen.temporal().localDate().range(LocalDate.of(1920, 1, 1), LocalDate.of(2002, 1,
                    1
                ))
            )
            .generate(
                field(PersonInAusbildungUpdateDtoSpec::getNachname),
                gen -> gen.oneOf("Müller", "Sánchez", "Sato", "Singh", "Li", "García", "Nguyen")
            )
            .generate(
                field(PersonInAusbildungUpdateDtoSpec::getWohnsitzAnteilMutter),
                gen -> gen.ints().range(0, 100).as(BigDecimal::valueOf)
            )
            .assign(Assign.valueOf(PersonInAusbildungUpdateDtoSpec::getWohnsitzAnteilMutter)
                .to(PersonInAusbildungUpdateDtoSpec::getWohnsitzAnteilVater)
                .as((BigDecimal i) -> BigDecimal.valueOf(100).subtract(i)))
            .toModel();
    public static final Model<GesuchFormularUpdateDtoSpec> gesuchFormularUpdateDtoSpecPersonInAusbildungModel =
        Instancio.of(
                GesuchFormularUpdateDtoSpec.class)
            .set(
                field(GesuchFormularUpdateDtoSpec::getPersonInAusbildung),
                Instancio.create(personInAusbildungUpdateDtoSpecModel)
            )
            .ignore(field(GesuchFormularUpdateDtoSpec::getAusbildung))
            .ignore(field(GesuchFormularUpdateDtoSpec::getElterns))
            .ignore(field(GesuchFormularUpdateDtoSpec::getGeschwisters))
            .ignore(field(GesuchFormularUpdateDtoSpec::getLebenslaufItems))
            .ignore(field(GesuchFormularUpdateDtoSpec::getEinnahmenKosten))
            .ignore(field(GesuchFormularUpdateDtoSpec::getAuszahlung))
            .ignore(field(GesuchFormularUpdateDtoSpec::getFamiliensituation))
            .ignore(field(GesuchFormularUpdateDtoSpec::getKinds))
            .ignore(field(GesuchFormularUpdateDtoSpec::getPartner))
            .toModel();
}
