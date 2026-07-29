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

package ch.dvbern.stip.berechnung.domain.util;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ch.dvbern.stip.berechnung.domain.type.PersonenHaushalt;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatDto;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatDto;
import ch.dvbern.stip.generated.dto.PersonenHaushaltGruppeDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BerechnungUtil {
    public static final int MONTH_LIMIT_AUSBILDUNG_TERTIAERSTUFE = 36;
    public static final int DARLEHEN_LIMIT = 50000;

    public List<PersonenHaushaltGruppeDto> getPersonenHaushaltGroups(
        final PersoenlichesBudgetresultatDto persoenlichesBudgetresultatDto,
        final List<FamilienBudgetresultatDto> familienBudgetresultatList
    ) {
        final var personenHaushaltGroups = new ArrayList<PersonenHaushaltGruppeDto>();

        personenHaushaltGroups.add(
            new PersonenHaushaltGruppeDto(
                PersonenHaushalt.PIA,
                persoenlichesBudgetresultatDto.getHaushaltNames()
            )
        );
        personenHaushaltGroups.addAll(
            familienBudgetresultatList.stream()
                .map(
                    resultatDto -> new PersonenHaushaltGruppeDto(
                        switch (resultatDto.getSteuerdatenTyp()) {
                            case FAMILIE -> PersonenHaushalt.FAMILIE;
                            case MUTTER -> PersonenHaushalt.MUTTER;
                            case VATER -> PersonenHaushalt.VATER;
                        },
                        resultatDto.getHaushaltNames()
                    )
                )
                .toList()
        );

        return personenHaushaltGroups;
    }

    public static String serializeBerechnungresultatDto(final BerechnungsresultatDto berechnungsresultatDto) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        StringWriter writer = new StringWriter();
        try {
            mapper.writeValue(writer, berechnungsresultatDto);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return writer.toString();
    }

    public static int subtractGesezlichesDarlehen(final int total, final int monateMitDarlehen) {
        if (monateMitDarlehen == 0) {
            return total;
        }

        final var monateOhneDarlehen = 12 - monateMitDarlehen;

        final var stipendiumOfMonateOhneDarlehen = total * monateOhneDarlehen / 12;
        final var stipendiumOfMonateMitDarlehen = BigDecimal.valueOf(total)
            .multiply(BigDecimal.valueOf(monateMitDarlehen))
            .divide(BigDecimal.valueOf(12), RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(2))
            .divide(BigDecimal.valueOf(3), RoundingMode.HALF_UP)
            .intValue();

        return stipendiumOfMonateOhneDarlehen + stipendiumOfMonateMitDarlehen;
    }

    public static int roundGesetzlichesDarlehen(final int total) {
        return BigDecimal.valueOf(total)
            .divide(BigDecimal.valueOf(100), 0, RoundingMode.UP)
            .multiply(BigDecimal.valueOf(100))
            .intValue();
    }

    public int calculateGesetzlichesDarlehen(final int total) {
        // divide by 300 then round and multiply by 100 to get a rounded (to the nearest 100) third of the
        // stipendium
        return BigDecimal.valueOf(total)
            .divide(BigDecimal.valueOf(300), 0, RoundingMode.UP)
            .multiply(BigDecimal.valueOf(100))
            .intValue();
    }

    public boolean nullableCompare(final Integer value1, final Integer value2, final int defaultValue) {
        if ((Objects.isNull(value1) || value1 == defaultValue) && (Objects.isNull(value2) || value2 == defaultValue)) {
            return true;
        }

        return Objects.equals(value1, value2);
    }

}
