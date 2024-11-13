/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.generator.api.model;

import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.GesuchsjahrCreateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchsjahrUpdateDtoSpec;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GesuchsjahrTestSpecGenerator {
    public static GesuchsjahrCreateDtoSpec gesuchsjahrCreateDtoSpec =
        TestUtil.createUpdateDtoSpec(GesuchsjahrCreateDtoSpec::new, (model) -> {
            model.bezeichnungDe("Test Gesuchsjahr Create DE");
            model.bezeichnungFr("Test Gesuchsjahr Create FR");
            model.setTechnischesJahr(2024);
        });

    public static GesuchsjahrUpdateDtoSpec gesuchsjahrUpdateDtoSpec =
        TestUtil.createUpdateDtoSpec(GesuchsjahrUpdateDtoSpec::new, (model) -> {
            model.bezeichnungDe("Test Gesuchsjahr Update DE");
            model.bezeichnungFr("Test Gesuchsjahr Update FR");
            model.setTechnischesJahr(2024);
        });
}
