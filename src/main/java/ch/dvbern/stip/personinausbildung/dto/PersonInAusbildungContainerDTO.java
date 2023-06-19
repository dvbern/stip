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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.personinausbildung.dto;

import ch.dvbern.stip.personinausbildung.model.PersonInAusbildung;
import ch.dvbern.stip.personinausbildung.model.PersonInAusbildungContainer;
import lombok.Value;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Value
public class PersonInAusbildungContainerDTO {

    @NotNull
    private UUID id;

    private PersonInAusbildungDTO personInAusbildungGS;

    private PersonInAusbildungDTO personInAusbildungSB;

    public static PersonInAusbildungContainerDTO from(PersonInAusbildungContainer changed) {
        return changed == null ? null : new PersonInAusbildungContainerDTO(changed.getId(), PersonInAusbildungDTO.from(changed.getPersonInAusbildungGS()), PersonInAusbildungDTO.from(changed.getPersonInAusbildungSB()));
    }

    public void apply(PersonInAusbildungContainer personInAusbildungContainer) {
        PersonInAusbildung personInAusbildungSBFromGesuch = personInAusbildungContainer.getPersonInAusbildungSB() != null ? personInAusbildungContainer.getPersonInAusbildungSB() : new PersonInAusbildung();
        personInAusbildungSB.apply(personInAusbildungSBFromGesuch);
        personInAusbildungContainer.setPersonInAusbildungSB(personInAusbildungSBFromGesuch);
    }
}
