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

package ch.dvbern.stip.api.demo.type;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum;
import ch.dvbern.stip.api.common.validation.AhvValidator;
import ch.dvbern.stip.api.lebenslauf.type.WohnsitzKanton;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import ch.dvbern.stip.generated.dto.DemoDataDto;

public final class DemoDataDefaults {
    public static final Sprache SPRACHE = Sprache.DEUTSCH;
    public static final String ZAHLUNGSVERBINBDUNG_IBAN = "CH3908704016075473007";
    public static final AusbildungsPensum AUSBILDUNG_PENSUM = AusbildungsPensum.VOLLZEIT;
    public static final boolean AUSBILDUNG_BESUCHT_BMS = false;
    public static final boolean PIA_VORMUNDSCHAFT = false;
    public static final WohnsitzKanton LEBENSLAUF_WOHNSITZ_KANTON = WohnsitzKanton.BE;
    public static final int EK_EINKOMMEN = 0;
    public static final int EK_FAHRKOSTEN = 0;
    public static final int EK_VERMOEGEN = 0;
    public static final int EK_VERPFLEGUNGSKOSTEN = 0;
    public static final int EK_WG_ANZAHL_PERSONEN = 2;
    public static final int EK_ARBEITSPENSUM = 100;
    public static final int STEUERDATEN_EIGENMIETWERT = 0;
    public static final int STEUERDATEN_VERMOEGEN = 0;
    public static final int STEUERDATEN_STEUERN_KANTON_GEMEINDE = 0;
    public static final int STEUERDATEN_STEUERN_BUND = 0;
    public static final String STEUERDATEN_VERANLAGUNGSSTATUS = "Testcase";

    private static final int[] SVN_COUNTRY_CODE = new int[] { 7, 5, 6 };

    public static int getSteuerjahr(LocalDate referenceDate) {
        return referenceDate.getYear() - 1;
    }

    public static String generateSVN() {
        final var svnWithoutCheckDigit = IntStream.concat(Arrays.stream(SVN_COUNTRY_CODE), randomDigits(9)).toArray();
        final var checkDigit =
            AhvValidator.getCheckDigit(concatArrayToString(Arrays.stream(svnWithoutCheckDigit)).toCharArray());
        final var allDigits = IntStream.concat(
            Arrays.stream(svnWithoutCheckDigit),
            IntStream.of(checkDigit)
        ).toArray();

        return "%s.%s.%s.%s".formatted(
            concatArrayToString(Arrays.stream(allDigits, 0, 3)),
            concatArrayToString(Arrays.stream(allDigits, 3, 7)),
            concatArrayToString(Arrays.stream(allDigits, 7, 11)),
            concatArrayToString(Arrays.stream(allDigits, 11, 13))
        );
    }

    public static BigDecimal bigDecimalNullable(Integer number) {
        if (Objects.isNull(number)) {
            return null;
        }

        return new BigDecimal(number);
    }

    public static Integer defaultByKindsIfNull(Integer value, DemoDataDto demoDataDto) {
        if (Objects.nonNull(value)) {
            return value;
        }
        if (demoDataDto.getKinder().isEmpty()) {
            return null;
        }
        return 0;
    }

    private static String concatArrayToString(IntStream array) {
        return array.mapToObj(String::valueOf).collect(Collectors.joining());
    }

    private static IntStream randomDigits(int amountOfDigits) {
        final var random = new Random();
        final var digits = new int[amountOfDigits];
        for (int i = 0; i < amountOfDigits; i++) {
            digits[i] = random.nextInt(10);
        }
        return Arrays.stream(digits);
    }
}
