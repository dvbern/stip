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

package ch.dvbern.stip.berechnung.dto;

import java.util.ArrayList;
import java.util.Objects;

import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.generated.dto.PersonValueItemDto;

public class PersonValueList {
    private PersonValueItemDto person;
    private PersonValueItemDto partner;
    private final ArrayList<PersonValueItemDto> kinderValueList = new ArrayList<>();

    public void setPersonValue(String name, Integer value) {
        person = new PersonValueItemDto(name, Objects.requireNonNullElse(value, 0));
    }

    public void setPartnerValue(String partnerName, Integer value) {
        partner = new PersonValueItemDto(partnerName, Objects.requireNonNullElse(value, 0));
    }

    public void addKindValue(Kind kind, Integer value) {
        if (Objects.isNull(value)) {
            return;
        }
        kinderValueList.add(new PersonValueItemDto(kind.getVorname(), value));
    }

    public ArrayList<PersonValueItemDto> toList() {
        final var list = new ArrayList<PersonValueItemDto>();
        if (Objects.isNull(person)) {
            if (Objects.nonNull(partner) || !kinderValueList.isEmpty()) {
                throw new IllegalStateException("No Person value given, but Partner or Kinds");
            }
            return list;
        }

        list.add(person);

        if (Objects.nonNull(partner)) {
            list.add(partner);
        }

        if (!kinderValueList.isEmpty()) {
            list.addAll(kinderValueList);
        }

        return list;
    }
}
