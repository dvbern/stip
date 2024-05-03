package ch.dvbern.stip.api.generator.api.model;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.GesuchsjahrCreateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchsjahrUpdateDtoSpec;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GesuchsjahrTestSpecGenerator {
    public static GesuchsjahrCreateDtoSpec gesuchsjahrCreateDtoSpec =
        TestUtil.createUpdateDtoSpec(GesuchsjahrCreateDtoSpec::new, (model, faker) -> {
            model.bezeichnungDe(faker.lorem().word());
            model.bezeichnungFr(faker.lorem().word());
            model.setTechnischesJahr(2024);
        });

    public static GesuchsjahrUpdateDtoSpec gesuchsjahrUpdateDtoSpec =
        TestUtil.createUpdateDtoSpec(GesuchsjahrUpdateDtoSpec::new, (model, faker) -> {
            model.bezeichnungDe(faker.lorem().word());
            model.bezeichnungFr(faker.lorem().word());
            model.setTechnischesJahr(2024);
        });
}
