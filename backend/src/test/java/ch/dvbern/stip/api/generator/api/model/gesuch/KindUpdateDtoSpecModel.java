package ch.dvbern.stip.api.generator.api.model.gesuch;

import java.math.BigDecimal;
import java.util.List;

import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.KindUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.WohnsitzDtoSpec;
import org.instancio.Assign;
import org.instancio.Instancio;
import org.instancio.Model;

import static org.instancio.Select.field;

public class KindUpdateDtoSpecModel {
    public static final Model<List<KindUpdateDtoSpec>> kinderUpdateDtoSpecModel =
        Instancio.ofList(KindUpdateDtoSpec.class).size(1)
            .ignore(field(KindUpdateDtoSpec::getId))
            .set(field(KindUpdateDtoSpec::getWohnsitz), WohnsitzDtoSpec.MUTTER_VATER)
            .generate(
                field(KindUpdateDtoSpec::getWohnsitzAnteilMutter),
                gen -> gen.ints().range(0, 100).as(BigDecimal::valueOf)
            )
            .assign(Assign.valueOf(KindUpdateDtoSpec::getWohnsitzAnteilMutter)
                .to(KindUpdateDtoSpec::getWohnsitzAnteilVater)
                .as((BigDecimal i) -> BigDecimal.valueOf(100).subtract(i)))
            .toModel();

    public static final Model<GesuchFormularUpdateDtoSpec> gesuchFormularUpdateDtoSpecKinderModel =
        Instancio.of(
                GesuchFormularUpdateDtoSpec.class)
            .set(
                field(GesuchFormularUpdateDtoSpec::getKinds),
                Instancio.create(kinderUpdateDtoSpecModel)
            )
            .ignore(field(GesuchFormularUpdateDtoSpec::getFamiliensituation))
            .ignore(field(GesuchFormularUpdateDtoSpec::getElterns))
            .ignore(field(GesuchFormularUpdateDtoSpec::getAuszahlung))
            .ignore(field(GesuchFormularUpdateDtoSpec::getLebenslaufItems))
            .ignore(field(GesuchFormularUpdateDtoSpec::getEinnahmenKosten))
            .ignore(field(GesuchFormularUpdateDtoSpec::getAusbildung))
            .ignore(field(GesuchFormularUpdateDtoSpec::getPersonInAusbildung))
            .ignore(field(GesuchFormularUpdateDtoSpec::getGeschwisters))
            .ignore(field(GesuchFormularUpdateDtoSpec::getPartner))
            .toModel();
}
