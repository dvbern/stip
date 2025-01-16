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

import java.util.Objects;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthorizerUtil {
    public boolean isGesuchstellerOfGesuch(final Benutzer currentBenutzer, final Gesuch gesuch) {
        return Objects.equals(
            gesuch.getAusbildung().getFall().getGesuchsteller().getId(),
            currentBenutzer.getId()
        );
    }

    public boolean isGesuchstellerOfFall(final Benutzer currentBenutzer, final Fall fall) {
        return Objects.equals(
            fall.getGesuchsteller().getId(),
            currentBenutzer.getId()
        );
    }

    public boolean hasDelegierungAndIsCurrentBenutzerMitarbeiter(
        final Gesuch gesuch,
        final SozialdienstService sozialdienstService
    ) {
        return hasDelegierungAndIsCurrentBenutzerMitarbeiter(gesuch.getAusbildung(), sozialdienstService);
    }

    public boolean hasDelegierungAndIsCurrentBenutzerMitarbeiter(
        final Ausbildung ausbildung,
        final SozialdienstService sozialdienstService
    ) {
        return hasDelegierungAndIsCurrentBenutzerMitarbeiter(ausbildung.getFall(), sozialdienstService);
    }

    public boolean hasDelegierungAndIsCurrentBenutzerMitarbeiter(
        final Fall fall,
        final SozialdienstService sozialdienstService
    ) {
        final var delegierung = fall.getDelegierung();
        if (delegierung == null) {
            return false;
        }

        return sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(
            delegierung.getSozialdienst().getId()
        );
    }
}
