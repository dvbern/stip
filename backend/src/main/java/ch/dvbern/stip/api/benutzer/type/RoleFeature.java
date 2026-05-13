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

package ch.dvbern.stip.api.benutzer.type;

import ch.dvbern.stip.api.common.util.OidcConstants;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleFeature<T> {
    private final String role;
    private final T[] feature;

    public static <T> RoleFeature<T> forSachbearbeiter(T... values) {
        return new RoleFeature<>(OidcConstants.ROLE_SACHBEARBEITER, values);
    }

    public static <T> RoleFeature<T> forFreigabe(T... values) {
        return new RoleFeature<>(OidcConstants.ROLE_FREIGABESTELLE, values);
    }

    public static <T> RoleFeature<T> forJurist(T... values) {
        return new RoleFeature<>(OidcConstants.ROLE_JURIST, values);
    }

    public static <T> RoleFeature<T> forAdmin(T... values) {
        return new RoleFeature<>(OidcConstants.ROLE_SACHBEARBEITER_ADMIN, values);
    }
}
