package ch.dvbern.stip.api.generator.api.model.benutzer;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.SachbearbeiterZuordnungStammdatenDtoSpec;
import ch.dvbern.stip.generated.dto.SachbearbeiterZuordnungStammdatenListDtoSpec;

public class SachbearbeiterZuordnungStammdatenDtoSpecModel {
    public static List<SachbearbeiterZuordnungStammdatenListDtoSpec> sachbearbeiterZuordnungStammdatenListDtoSpecs(final int amount) {
        return TestUtil.createUpdateDtoSpecs(SachbearbeiterZuordnungStammdatenListDtoSpec::new, (model, faker) -> {
            model.setSachbearbeiter(UUID.fromString(faker.internet().uuid()));
            model.setZuordnung(sachbearbeiterZuordnungStammdatenDtoSpec());
        }, amount);
    }

    public static SachbearbeiterZuordnungStammdatenDtoSpec sachbearbeiterZuordnungStammdatenDtoSpec() {
        return TestUtil.createUpdateDtoSpec(SachbearbeiterZuordnungStammdatenDtoSpec::new, (model, faker) -> {
            model.setBuchstabenDe("A-D");
            model.setBuchstabenFr("A-C,E");
        });
    }

    public static List<SachbearbeiterZuordnungStammdatenListDtoSpec> sachbearbeiterZuordnungStammdatenListDtoSpecs() {
        return sachbearbeiterZuordnungStammdatenListDtoSpecs(1);
    }
}
