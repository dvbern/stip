package ch.dvbern.stip.api.generator.api.model.benutzer;

import ch.dvbern.stip.generated.dto.SachbearbeiterZuordnungStammdatenDtoSpec;
import ch.dvbern.stip.generated.dto.SachbearbeiterZuordnungStammdatenListDtoSpec;
import org.instancio.Instancio;
import org.instancio.Model;

import static org.instancio.Select.field;

public class SachbearbeiterZuordnungStammdatenDtoSpecModel {
    public static final Model<SachbearbeiterZuordnungStammdatenDtoSpec> sachbearbeiterZuordnungStammdatenDtoSpecModel =
        Instancio.of(SachbearbeiterZuordnungStammdatenDtoSpec.class)
            .set(field(SachbearbeiterZuordnungStammdatenDtoSpec::getBuchstabenDe), "A-D")
            .set(field(SachbearbeiterZuordnungStammdatenDtoSpec::getBuchstabenFr), "A-C,E")
            .toModel();

    public static final Model<SachbearbeiterZuordnungStammdatenListDtoSpec>
        sachbearbeiterZuordnungStammdatenListDtoSpecModel =
        Instancio.of(SachbearbeiterZuordnungStammdatenListDtoSpec.class)
            .set(
                field(SachbearbeiterZuordnungStammdatenListDtoSpec::getZuordnung),
                Instancio.of(sachbearbeiterZuordnungStammdatenDtoSpecModel).create()
            )
            .toModel();
}
