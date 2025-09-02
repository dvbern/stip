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

package ch.dvbern.stip.berechnung.dto.v1;

import java.math.BigDecimal;
import java.util.Objects;

import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.berechnung.dto.DmnRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@Value
@JsonIgnoreProperties
public class PersonenImHaushaltRequestV1 implements DmnRequest {
    @JsonProperty
    PersonenImHaushaltInputV1 personenImHaushaltInput;

    @AllArgsConstructor
    @Builder
    @Jacksonized
    public static class PersonenImHaushaltInputV1 {
        @JsonProperty
        FamiliensituationV1 familiensituation;

        @JsonProperty
        PersonInAusbildungV1 personInAusbildung;

        @JsonProperty
        String elternToPrioritize;

        @JsonProperty
        int geschwisterTeilzeit;

        @JsonProperty
        int geschwisterVaterVollzeit;

        @JsonProperty
        int geschwisterMutterVollzeit;
    }

    @Override
    public int majorVersion() {
        return 1;
    }

    @Override
    public int minorVersion() {
        return 0;
    }

    public static PersonenImHaushaltRequestV1 createRequest(
        final GesuchFormular gesuchFormular,
        final ElternTyp elternToPrioritize
    ) {
        final var geschwisterOhneEigenerHaushalt = gesuchFormular.getGeschwisters()
            .stream()
            .filter(geschwister -> geschwister.getWohnsitz() != Wohnsitz.EIGENER_HAUSHALT)
            .toList();
        final int geschwisterTeilzeit = (int) geschwisterOhneEigenerHaushalt.stream()
            .filter(
                geschwister -> Objects.requireNonNullElse(geschwister.getWohnsitzAnteilMutter(), BigDecimal.ZERO)
                    .intValue() > 0
                &&
                Objects.requireNonNullElse(geschwister.getWohnsitzAnteilVater(), BigDecimal.ZERO).intValue() > 0
            )
            .count();
        final int geschwisterVaterVollzeit = (int) geschwisterOhneEigenerHaushalt.stream()
            .filter(
                geschwister -> Objects.requireNonNullElse(geschwister.getWohnsitzAnteilVater(), BigDecimal.valueOf(100))
                    .intValue() == 100
            )
            .count();
        final int geschwisterMutterVollzeit = (int) geschwisterOhneEigenerHaushalt.stream()
            .filter(
                geschwister -> Objects.requireNonNullElse(geschwister.getWohnsitzAnteilMutter(), BigDecimal.valueOf(0))
                    .intValue() == 100
            )
            .count();
        return new PersonenImHaushaltRequestV1(
            new PersonenImHaushaltInputV1(
                FamiliensituationV1.fromFamiliensituation(gesuchFormular.getFamiliensituation()),
                PersonInAusbildungV1.fromPersonInAusbildung(gesuchFormular.getPersonInAusbildung()),
                elternToPrioritize.toString(),
                geschwisterTeilzeit,
                geschwisterVaterVollzeit,
                geschwisterMutterVollzeit
            )
        );
    }
}
