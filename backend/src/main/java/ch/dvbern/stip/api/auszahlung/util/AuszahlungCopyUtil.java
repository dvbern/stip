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

package ch.dvbern.stip.api.auszahlung.util;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AuszahlungCopyUtil {
    public Auszahlung createCopyIgnoreReferences(final Auszahlung other) {
        final var copy = new Auszahlung();
        copyValues(other, copy);
        return copy;
    }

    public void copyValues(final Auszahlung source, final Auszahlung target) {
        // target.setKontoinhaber(source.getKontoinhaber());
        target.getZahlungsverbindung().setVorname(source.getZahlungsverbindung().getVorname());
        target.getZahlungsverbindung().setNachname(source.getZahlungsverbindung().getNachname());
        target.getZahlungsverbindung().setIban(source.getZahlungsverbindung().getIban());
        target.setSapBusinessPartnerId(source.getSapBusinessPartnerId());
        // target.setAuszahlungAnSozialdienst(source.isAuszahlungAnSozialdienst());
    }
}
