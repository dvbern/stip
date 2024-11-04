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

package ch.dvbern.stip.api.generator.api;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;

import ch.dvbern.stip.generated.dto.GesuchsperiodeCreateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchsperiodeUpdateDtoSpec;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GesuchsperiodeTestSpecGenerator {
    public GesuchsperiodeCreateDtoSpec gesuchsperiodeCreateDtoSpec()
    throws InvocationTargetException, IllegalAccessException {
        final var model = new GesuchsperiodeCreateDtoSpec();
        setValues(model);
        return model;
    }

    public GesuchsperiodeUpdateDtoSpec gesuchsperiodeUpdateDtoSpec()
    throws InvocationTargetException, IllegalAccessException {
        final var model = new GesuchsperiodeUpdateDtoSpec();
        setValues(model);
        return model;
    }

    private <T> void setValues(T model)
    throws InvocationTargetException, IllegalAccessException {
        final var currentDate = LocalDate.now();
        for (final var method : model.getClass().getMethods()) {
            if (method.getName().startsWith("set")) {
                final var params = method.getParameters();
                if (params.length != 1) {
                    continue;
                }

                final var paramType = params[0].getType();
                if (paramType.equals(String.class)) {
                    method.invoke(model, "");
                } else if (paramType.equals(Integer.class)) {
                    method.invoke(model, 1);
                } else if (paramType.equals(LocalDate.class)) {
                    method.invoke(model, currentDate);
                }
            }
        }
    }
}
