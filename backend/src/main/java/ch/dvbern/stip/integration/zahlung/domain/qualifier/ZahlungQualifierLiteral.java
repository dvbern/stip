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

package ch.dvbern.stip.integration.zahlung.domain.qualifier;

import ch.dvbern.stip.integration.zahlung.domain.model.ZahlungAdapterType;
import jakarta.enterprise.util.AnnotationLiteral;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ZahlungQualifierLiteral extends AnnotationLiteral<ZahlungQualifier> implements ZahlungQualifier {

    private final ZahlungAdapterType value;

    @Override
    public ZahlungAdapterType value() {
        return this.value;
    }
}
