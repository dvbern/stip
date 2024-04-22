package ch.dvbern.stip.api.generator.api.model.benutzer;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.SachbearbeiterZuordnungStammdatenDtoSpec;
import ch.dvbern.stip.generated.dto.SachbearbeiterZuordnungStammdatenListDtoSpec;

import java.util.List;
import java.util.UUID;

public class SachbearbeiterZuordnungStammdatenDtoSpecModel {
    public static List<SachbearbeiterZuordnungStammdatenListDtoSpec> sachbearbeiterZuordnungStammdatenListDtoSpecs(final int amount) {
        return TestUtil.createUpdateDtoSpecs(SachbearbeiterZuordnungStammdatenListDtoSpec::new, (model, faker) -> {
            model.setSachbearbeiter(UUID.fromString(faker.internet().uuid()));
            model.setZuordnung(sachbearbeiterZuordnungStammdatenDtoSpec);
        }, amount);
    }

    public static final SachbearbeiterZuordnungStammdatenDtoSpec sachbearbeiterZuordnungStammdatenDtoSpec =
        TestUtil.createUpdateDtoSpec(SachbearbeiterZuordnungStammdatenDtoSpec::new, (model, faker) -> {
            model.setBuchstabenDe("A-D");
            model.setBuchstabenFr("A-C,E");
        });

    public static final List<SachbearbeiterZuordnungStammdatenListDtoSpec> sachbearbeiterZuordnungStammdatenListDtoSpecs = sachbearbeiterZuordnungStammdatenListDtoSpecs(1);
}
