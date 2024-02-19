package ch.dvbern.stip.api.generator.api.model.gesuch;

import ch.dvbern.stip.generated.dto.GeschwisterUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.WohnsitzDtoSpec;
import org.instancio.Assign;
import org.instancio.Instancio;
import org.instancio.Model;

import java.math.BigDecimal;
import java.util.List;

import static org.instancio.Select.field;

public final class GeschwisterUpdateDtoSpecModel {

    public static final Model<List<GeschwisterUpdateDtoSpec>> geschwisterUpdateDtoSpecModel =
        Instancio.ofList(GeschwisterUpdateDtoSpec.class).size(1)
            .ignore(field(GeschwisterUpdateDtoSpec::getId))
            .set(field(GeschwisterUpdateDtoSpec::getWohnsitz), WohnsitzDtoSpec.MUTTER_VATER)
            .generate(
                field(GeschwisterUpdateDtoSpec::getWohnsitzAnteilMutter),
                gen -> gen.ints().range(0, 100).as(BigDecimal::valueOf)
            )
            .assign(Assign.valueOf(GeschwisterUpdateDtoSpec::getWohnsitzAnteilMutter)
                .to(GeschwisterUpdateDtoSpec::getWohnsitzAnteilVater)
                .as((BigDecimal i) -> BigDecimal.valueOf(100).subtract(i)))
            .toModel();

    public static final Model<GesuchFormularUpdateDtoSpec> gesuchFormularUpdateDtoSpecGeschwistersModel =
        Instancio.of(
                GesuchFormularUpdateDtoSpec.class)
            .set(
                field(GesuchFormularUpdateDtoSpec::getGeschwisters),
                Instancio.create(geschwisterUpdateDtoSpecModel)
            )
            .ignore(field(GesuchFormularUpdateDtoSpec::getFamiliensituation))
            .ignore(field(GesuchFormularUpdateDtoSpec::getElterns))
            .ignore(field(GesuchFormularUpdateDtoSpec::getAuszahlung))
            .ignore(field(GesuchFormularUpdateDtoSpec::getLebenslaufItems))
            .ignore(field(GesuchFormularUpdateDtoSpec::getEinnahmenKosten))
            .ignore(field(GesuchFormularUpdateDtoSpec::getAusbildung))
            .ignore(field(GesuchFormularUpdateDtoSpec::getPersonInAusbildung))
            .ignore(field(GesuchFormularUpdateDtoSpec::getKinds))
            .ignore(field(GesuchFormularUpdateDtoSpec::getPartner))
            .toModel();
}
