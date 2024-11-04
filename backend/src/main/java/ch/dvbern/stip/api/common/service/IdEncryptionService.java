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

package ch.dvbern.stip.api.common.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ch.dvbern.stip.api.common.util.Constants;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IdEncryptionService {
    private static final Character[] baseCharSet = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
    private final Character[][] charSetLengthSix;
    private final Character[][] charSetLengthFive;

    public IdEncryptionService() {
        charSetLengthSix = new Character[6][baseCharSet.length];
        charSetLengthFive = new Character[5][baseCharSet.length];

        final Random random = new Random(Constants.FALL_GESUCH_NUMBER_GEN_SEED);

        generateCharSet(charSetLengthSix, random);
        generateCharSet(charSetLengthFive, random);
    }

    private static void generateCharSet(final Character[][] charSet, final Random random) {
        for (int i = 0; i < charSet.length; i++) {
            final List<Character> charList = Arrays.asList(baseCharSet.clone());
            Collections.shuffle(charList, random);

            charSet[i] = charList.toArray(new Character[0]);
        }
    }

    private static String encodeWithMax(
        final int idToEncode,
        final Character[][] charSet
    ) {
        if (idToEncode > (Math.pow(10, charSet.length) - 1)) {
            throw new IllegalArgumentException("idToEncode is too large");
        }

        final String paddedIdFormatString = "%0" + charSet.length + "d";
        final String paddedId = String.format(paddedIdFormatString, idToEncode);

        StringBuilder encryptedId = new StringBuilder();

        for (int i = 0; i < charSet.length; i++) {
            int digitIndex = Character.getNumericValue(paddedId.charAt(i));

            encryptedId.append(charSet[i][digitIndex]);
        }
        if (encryptedId.length() > charSet.length) {
            throwEncodedTooLong(idToEncode, encryptedId.toString(), charSet.length);
        }
        return encryptedId.toString();
    }

    private static void throwEncodedTooLong(final int idToEncode, final String encoded, final int length) {
        throw new IllegalStateException(
            String.format(
                "This should be unreachable, trying to encode %s resulted in %s, length: %s",
                idToEncode,
                encoded,
                length
            )
        );
    }

    public String encryptLengthFive(int idToEncode) {
        return encodeWithMax(idToEncode, charSetLengthFive);
    }

    public String encryptLengthSix(int idToEncode) {
        return encodeWithMax(idToEncode, charSetLengthSix);
    }
}
