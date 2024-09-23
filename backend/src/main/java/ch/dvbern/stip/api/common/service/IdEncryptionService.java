package ch.dvbern.stip.api.common.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ch.dvbern.stip.api.common.util.Constants;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IdEncryptionService {
    private final Character[][] charSetLengthSix;
    private final Character[][] charSetLengthFive;
    private static final Character[] baseCharSet = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

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
        final Character[][] charSet,
        final int maxInt,
        final int length
    ) {
        if (idToEncode > maxInt) {
            throw new IllegalArgumentException("idToEncode is too large");
        }

        final String paddedId = String.format("%0" + length + "d", idToEncode);

        StringBuilder encryptedId = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int digitIndex = Character.getNumericValue(paddedId.charAt(i));

            encryptedId.append(charSet[i][digitIndex]);
        }
        if (encryptedId.length() > length) {
            throwEncodedTooLong(idToEncode, encryptedId.toString(), length);
        }
        return encryptedId.toString();
    }
//
    private static void throwEncodedTooLong(final int idToEncode, final String encoded, final int length) {
        throw new IllegalStateException(String.format(
            "This should be unreachable, trying to encode %s resulted in %s, length: %s",
            idToEncode,
            encoded,
            length
        ));
    }
//
    public String encryptLengthFive(int idToEncode) {
        return encodeWithMax(idToEncode, charSetLengthFive, 99_999, 5);
    }

    public String encryptLengthSix(int idToEncode) {
        return encodeWithMax(idToEncode, charSetLengthSix, 999_999, 6);
    }
}
