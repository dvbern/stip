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

package ch.dvbern.stip.api.common.authorization.util;

import java.util.function.BooleanSupplier;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DokumentAuthorizerUtil {
    public boolean isDelegiertAndCanUploadOrDelete(
        final Gesuch gesuch,
        final Benutzer currentBenutzer,
        final BooleanSupplier canUploadOrDeleteCheck,
        final Runnable onForbidden,
        final SozialdienstService sozialdienstService
    ) {
        if (AuthorizerUtil.isDelegiert(gesuch)) {
            if (AuthorizerUtil.isGesuchstellerOfGesuch(currentBenutzer, gesuch)) {
                onForbidden.run();
            }

            final var isNotGesuchstellerButDelegierter =
                !AuthorizerUtil.isGesuchstellerOfGesuch(currentBenutzer, gesuch)
                && AuthorizerUtil
                    .hasDelegierungAndIsCurrentBenutzerMitarbeiterOfSozialdienst(gesuch, sozialdienstService);

            return canUploadOrDeleteCheck.getAsBoolean() && isNotGesuchstellerButDelegierter;
        }

        return false;
    }
}
