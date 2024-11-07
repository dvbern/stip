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

package ch.dvbern.stip.api.benutzer.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.benutzer.entity.Rolle;

public final class TestRollen {
    private static final String[] ALL_ROLLEN;

    public static final String ADMIN = "Admin";
    public static final String[] ADMIN_COMPOSITE;

    public static final String SACHBEARBEITER = "Sachbearbeiter";
    public static final String[] SACHBEARBEITER_COMPOSITE;

    public static final String GESUCHSTELLER = "Gesuchsteller";
    public static final String[] GESUCHSTELLER_COMPOSITE;

    public static final String JURIST = "Jurist";
    public static final String[] JURIST_COMPOSITE;

    static {
        ALL_ROLLEN = new String[] {
            "Admin",
            "AUSBILDUNG_CREATE",
            "AUSBILDUNG_DELETE",
            "AUSBILDUNG_READ",
            "AUSBILDUNG_UPDATE",
            "default-roles-bern",
            "FALL_CREATE",
            "FALL_DELETE",
            "FALL_READ",
            "FALL_UPDATE",
            "GESUCH_CREATE",
            "GESUCH_DELETE",
            "GESUCH_READ",
            "GESUCHSPERIODE_CREATE",
            "GESUCHSPERIODE_DELETE",
            "GESUCHSPERIODE_READ",
            "GESUCHSPERIODE_UPDATE",
            "Gesuchsteller",
            "GESUCH_UPDATE",
            "offline_access",
            "Sachbearbeiter",
            "STAMMDATEN_CREATE",
            "STAMMDATEN_DELETE",
            "STAMMDATEN_READ",
            "STAMMDATEN_UPDATE",
            "uma_authorization"
        };

        ADMIN_COMPOSITE = new String[] {
            "GESUCHSPERIODE_UPDATE",
            "AUSBILDUNG_UPDATE",
            "AUSBILDUNG_CREATE",
            "GESUCH_CREATE",
            "FALL_CREATE",
            "GESUCH_READ",
            "SEND_EMAIL",
            "AUSBILDUNG_READ",
            "GESUCH_DELETE",
            "FALL_DELETE",
            "STAMMDATEN_READ",
            "GESUCH_UPDATE",
            "GESUCHSPERIODE_CREATE",
            "FALL_UPDATE",
            "GESUCHSPERIODE_READ",
            "STAMMDATEN_DELETE",
            "GESUCHSPERIODE_DELETE",
            "STAMMDATEN_CREATE",
            "AUSBILDUNG_DELETE",
            "STAMMDATEN_UPDATE",
            "FALL_READ"
        };

        GESUCHSTELLER_COMPOSITE = new String[] {
            "Gesuchsteller",
            "GESUCH_READ",
            "GESUCH_UPDATE",
            "FALL_UPDATE",
            "GESUCH_CREATE",
            "GESUCH_DELETE",
            "FALL_CREATE",
            "GESUCHSPERIODE_READ",
            "FALL_READ",
            "AUSBILDUNG_READ",
            "STAMMDATEN_READ"
        };

        SACHBEARBEITER_COMPOSITE = new String[] {
            "Sachbearbeiter",
            "GESUCH_READ",
            "GESUCH_UPDATE",
            "FALL_UPDATE",
            "GESUCH_CREATE",
            "FALL_CREATE",
            "GESUCHSPERIODE_READ",
            "GESUCH_DELETE",
            "FALL_DELETE",
            "FALL_READ",
            "AUSBILDUNG_READ",
            "STAMMDATEN_READ"
        };

        JURIST_COMPOSITE = new String[] {
            "Jurist",
            "AUSBILDUNG_CREATE",
            "AUSBILDUNG_READ",
            "AUSBILDUNG_UPDATE",
            "AUSBILDUNG_DELETE"
        };
    }

    public static Set<Rolle> getComposite(final String composite) {
        return switch (composite) {
            case ADMIN -> createRollen(ADMIN_COMPOSITE);
            case SACHBEARBEITER -> createRollen(SACHBEARBEITER_COMPOSITE);
            case GESUCHSTELLER -> createRollen(GESUCHSTELLER_COMPOSITE);
            default -> Set.of();
        };
    }

    private static Set<Rolle> createRollen(final String[] rollen) {
        return Arrays.stream(rollen).map(TestRollen::copyRolle).collect(Collectors.toSet());
    }

    private static Rolle copyRolle(final String rolle) {
        return new Rolle().setKeycloakIdentifier(rolle);
    }
}
