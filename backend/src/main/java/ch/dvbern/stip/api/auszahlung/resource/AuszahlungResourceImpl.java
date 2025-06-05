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

package ch.dvbern.stip.api.auszahlung.resource;

import java.util.UUID;

import ch.dvbern.stip.generated.api.AuszahlungResource;
import ch.dvbern.stip.generated.dto.AuszahlungDto;
import ch.dvbern.stip.generated.dto.AuszahlungUpdateDto;

public class AuszahlungResourceImpl implements AuszahlungResource {
    @Override
    public UUID createAuszahlungForGesuch(UUID gesuchId, AuszahlungDto auszahlungDto) {
        return null;
    }

    @Override
    public AuszahlungDto getAuszahlungForGesuch(UUID gesuchId) {
        return null;
    }

    @Override
    public UUID updateAuszahlungForGesuch(UUID gesuchId, AuszahlungUpdateDto auszahlungUpdateDto) {
        return null;
    }
}
