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

import java.util.List;
import java.util.Objects;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import io.quarkus.security.ForbiddenException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthorizerUtil {
    public boolean hasDelegierungAndIsCurrentBenutzerMitarbeiterOfSozialdienst(
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

    public static void gesuchStatusOneOfOrElseThrow(final Gesuch gesuch, final List<Gesuchstatus> statesToVerify) {
        if (!statesToVerify.contains(gesuch.getGesuchStatus())) {
            throw new ForbiddenException(
                "Gesuchstatus " + gesuch.getGesuchStatus().toString() + " not " + statesToVerify.toString()
            );
        }
    }

    public static void gesuchTrancheStatusOneOfOrElseThrow(
        final GesuchTranche gesuchTranche,
        final List<GesuchTrancheStatus> statesToVerify
    ) {
        if (!statesToVerify.contains(gesuchTranche.getStatus())) {
            throw new ForbiddenException(
                "GesuchTrancheStatus " + gesuchTranche.getStatus().toString() + " not " + statesToVerify.toString()
            );
        }
    }

    public boolean isGesuchstellerOfWithoutDelegierung(final Benutzer currentBenutzer, final Fall fall) {
        return Objects.equals(
            fall.getGesuchsteller().getId(),
            currentBenutzer.getId()
        )
        && fall.getDelegierung() == null;
    }

    public boolean isGesuchstellerOfOrDelegatedToSozialdienst(
        final Gesuch gesuch,
        final Benutzer currentBenutzer,
        final SozialdienstService sozialdienstService
    ) {
        return isGesuchstellerOfOrDelegatedToSozialdienst(
            gesuch.getAusbildung().getFall(),
            currentBenutzer,
            sozialdienstService
        );
    }

    public boolean isGesuchstellerOfOrDelegatedToSozialdienst(
        final Ausbildung ausbildung,
        final Benutzer currentBenutzer,
        final SozialdienstService sozialdienstService
    ) {
        return isGesuchstellerOfOrDelegatedToSozialdienst(
            ausbildung.getFall(),
            currentBenutzer,
            sozialdienstService
        );
    }

    public boolean isGesuchstellerOfOrDelegatedToSozialdienst(
        final Fall fall,
        final Benutzer currentBenutzer,
        final SozialdienstService sozialdienstService
    ) {
        return hasDelegierungAndIsCurrentBenutzerMitarbeiterOfSozialdienst(fall, sozialdienstService)
        || isGesuchstellerOfWithoutDelegierung(currentBenutzer, fall);
    }
}
