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

package ch.dvbern.stip.api.zahlungsverbindung.util;

import ch.dvbern.stip.api.zahlungsverbindung.entity.Zahlungsverbindung;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ZahlungsverbindungCopyUtil {
    public Zahlungsverbindung createCopyIgnoreReferences(final Zahlungsverbindung other) {
        final var copy = new Zahlungsverbindung();
        copyZahlungsverbindungValues(other, copy);
        return copy;
    }

    public void copyZahlungsverbindungValues(final Zahlungsverbindung source, final Zahlungsverbindung target) {
        target.setVorname(source.getVorname());
        target.setNachname(source.getNachname());
        target.setIban(source.getIban());
        target
            .setSapBusinessPartnerId(source.getSapBusinessPartnerId());
    }

}
