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

package ch.dvbern.stip.api.partner.util;

import ch.dvbern.stip.api.common.util.AbstractPersonCopyUtil;
import ch.dvbern.stip.api.partner.entity.Partner;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PartnerCopyUtil {
    public Partner createCopyIgnoreReferences(final Partner other) {
        if (other == null) {
            return null;
        }

        final var copy = new Partner();

        AbstractPersonCopyUtil.copy(other, copy);
        copy.setSozialversicherungsnummer(other.getSozialversicherungsnummer());
        copy.setAusbildungMitEinkommenOderErwerbstaetig(other.isAusbildungMitEinkommenOderErwerbstaetig());
        copy.setJahreseinkommen(other.getJahreseinkommen());
        copy.setVerpflegungskosten(other.getVerpflegungskosten());
        copy.setFahrkosten(other.getFahrkosten());

        return copy;
    }
}
