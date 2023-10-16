package ch.dvbern.stip.test.generator.api.model.benutzer;

import ch.dvbern.oss.stip.contract.test.dto.SachbearbeiterZuordnungStammdatenDtoSpec;
import org.instancio.Instancio;
import org.instancio.Model;

import static org.instancio.Select.field;

public class SachbearbeiterZuordnungStammdatenDtoSpecModel {
	public static final Model<SachbearbeiterZuordnungStammdatenDtoSpec> sachbearbeiterZuordnungStammdatenDtoSpecModel =
			Instancio.of(SachbearbeiterZuordnungStammdatenDtoSpec.class)
					.set(field(SachbearbeiterZuordnungStammdatenDtoSpec::getBuchstabenDe), "A-D")
					.set(field(SachbearbeiterZuordnungStammdatenDtoSpec::getBuchstabenFr), "A-C,E")
					.toModel();
}
